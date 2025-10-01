package com.example.testdatabinding.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.testdatabinding.App
import com.example.testdatabinding.MyOpject
import com.example.testdatabinding.data.model.TripModel
import com.example.testdatabinding.data.view_model.TripViewModel
import com.example.testdatabinding.data.view_model.TripViewModelFactory
import com.example.test_trip_logic.ui.MyBottomSheet
import com.example.testdatabinding.databinding.ActivityMapsBinding
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var osmMap: MapView
    private var locationOverlay: MyLocationNewOverlay? = null

    private val tripViewModel: TripViewModel by viewModels {
        TripViewModelFactory(application as App)
    }
    private var currentMarker: Marker? = null

    private var trip: TripModel? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                enableMyLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        trip = intent.getParcelableExtra("trip")


        osmMap = binding.osmMap
        osmMap.setMultiTouchControls(true)
        osmMap.controller.setZoom(12.0)


        val tripPoint = GeoPoint(trip?.lat ?: 0.0, trip?.lng ?: 0.0)
        osmMap.controller.setCenter(tripPoint)

        val tripMarker = Marker(osmMap).apply {
            position = tripPoint
            title = trip?.name ?: "Trip"
        }
        osmMap.overlays.add(tripMarker)


        val mapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                return false
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                p?.let { geoPoint ->
                    if (MyOpject.isAdjustable) {

                        currentMarker?.let { osmMap.overlays.remove(it) }
                         currentMarker = Marker(osmMap).apply {
                            position = geoPoint
                            title = "New Marker"
                        }
                        osmMap.overlays.add(currentMarker)
                        osmMap.controller.setCenter(geoPoint)

                        Toast.makeText(
                            this@MapsActivity,
                            "New location: ${geoPoint.latitude}, ${geoPoint.longitude}",
                            Toast.LENGTH_SHORT
                        ).show()
                        trip = trip?.copy(lat = geoPoint.latitude , lng = geoPoint.longitude)
                        Log.d("TRIP_ID_LOG",geoPoint.latitude.toString())
                    } else {
                        Toast.makeText(this@MapsActivity, "Can't be edited", Toast.LENGTH_SHORT).show()
                    }
                }
                return true
            }
        }
        val mapEventsOverlay = MapEventsOverlay(mapEventsReceiver)
        osmMap.overlays.add(0, mapEventsOverlay)

        binding.fabToggleEdit.setOnClickListener {
            val bottomSheet = MyBottomSheet()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }


        checkLocationPermission()

        binding.btnBack.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("trip_result", trip)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

    }



    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                enableMyLocation()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                Toast.makeText(this, "Location permission needed", Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun enableMyLocation() {
        locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), osmMap)
        locationOverlay?.enableMyLocation()
        osmMap.overlays.add(locationOverlay)
    }

    override fun onDestroy() {
        MyOpject.isAdjustable = false
        super.onDestroy()
    }
}
