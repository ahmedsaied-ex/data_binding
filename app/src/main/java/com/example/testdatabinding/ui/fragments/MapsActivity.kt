package com.example.testdatabinding.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.testdatabinding.constants.Constants
import com.example.testdatabinding.data.map.controller.TripMapController
import com.example.testdatabinding.data.model.TripModel
import com.example.testdatabinding.data.view_model.MapViewModel
import com.example.test_trip_logic.ui.MyBottomSheet
import com.example.testdatabinding.databinding.ActivityMapsBinding
import com.google.android.material.snackbar.Snackbar
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView

class MapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var osmMap: MapView


    private val mapViewModel: MapViewModel by viewModels()

    private lateinit var tripMapController: TripMapController
    private var trip: TripModel? = null


    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                tripMapController.enableMyLocation()
            } else {
                Snackbar.make(binding.root, "Location permission denied", Snackbar.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        Configuration.getInstance().userAgentValue = packageName

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        trip = intent.getParcelableExtra(Constants.TRIP)


        osmMap = binding.osmMap
        osmMap.setMultiTouchControls(true)
        osmMap.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)

        tripMapController = TripMapController(
            mapView = osmMap,
            rootView = binding.root,
            onTripUpdated = { updatedTrip ->
                trip = updatedTrip
            },
            mapViewModel = mapViewModel
        )



        trip?.let { tripMapController.showTrip(it) }


        binding.fabToggleEdit.setOnClickListener {
            MyBottomSheet().show(supportFragmentManager, "EditOptions")
        }


        binding.btnBack.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra(Constants.TRIP_RESULT, trip)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }


        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                tripMapController.enableMyLocation()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    override fun onDestroy() {
        tripMapController.cleanup()
        super.onDestroy()
    }
}
