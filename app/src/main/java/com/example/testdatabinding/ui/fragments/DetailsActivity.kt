package com.example.testdatabinding.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.testdatabinding.App


import com.example.testdatabinding.repository.TripRepository
import com.example.testdatabinding.data.view_model.TripViewModel
import com.example.testdatabinding.data.view_model.TripViewModelFactory

import com.example.testdatabinding.data.model.TripModel
import com.example.testdatabinding.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityDetailsBinding

    private val tripViewModel: TripViewModel by viewModels {
        TripViewModelFactory(application as App)
    }


 var trip : TripModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)



         trip = intent.getParcelableExtra<TripModel>("trip")

        binding.trip=trip

        binding.fabMap.setOnClickListener {
            openMapsActivity(trip?:TripModel("","","",0.0,0.0))
        }

        binding.btnBack.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("trip_result", trip)
            setResult(RESULT_OK, resultIntent)
            finish() // close the activity
        }


    }
    private val mapsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val updatedTrip = result.data?.getParcelableExtra<TripModel>("trip_result")
                updatedTrip?.let {
                    binding.trip= it
                    trip=it
                }
            }
        }
    fun openMapsActivity(trip: TripModel) {
        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra("trip", trip)
        mapsLauncher.launch(intent)
    }


}
