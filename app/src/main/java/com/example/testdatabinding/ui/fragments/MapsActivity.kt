package com.example.testdatabinding.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.testdatabinding.constants.Constants
import com.example.testdatabinding.data.manager.LocationPermissionManager
import com.example.testdatabinding.data.map.controller.TripMapController
import com.example.testdatabinding.data.model.TripModel
import com.example.testdatabinding.data.view_model.MapViewModel
import com.example.testdatabinding.ui.MyBottomSheet
import com.example.testdatabinding.databinding.ActivityMapsBinding
import com.google.android.material.snackbar.Snackbar
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView

class MapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var permissionManager: LocationPermissionManager
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

        initialization()

        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        Configuration.getInstance().userAgentValue = packageName

       val  initTrip = intent.getParcelableExtra<TripModel>(Constants.TRIP)

        mapViewModel.initTrip(initTrip)

        mapViewModel.trip.observe(this){tripObserved ->
            trip = tripObserved
            trip?.let { tripMapController.showTrip(trip=it) }
        }
        osmMap = binding.osmMap
        osmMap.setMultiTouchControls(true)
        osmMap.setTileSource(TileSourceFactory.MAPNIK)

       initTripController()


        callBacks()
        permissionManager.checkAndRequestLocation {
            tripMapController.enableMyLocation()
        }


    }

    private fun callBacks() {
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

    }

    private fun initTripController() {
        tripMapController = TripMapController(
            mapView = osmMap,
            rootView = binding.root,
            onTripUpdated = { updatedTrip ->
                mapViewModel.updateTrip(updatedTrip)
            },
            mapViewModel = mapViewModel
        )
    }

    private fun initialization() {
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionManager= LocationPermissionManager(
            context = this,
            permissionLauncher = requestPermissionLauncher,
        )

    }

    override fun onDestroy() {
        tripMapController.cleanup()
        super.onDestroy()
    }
}
