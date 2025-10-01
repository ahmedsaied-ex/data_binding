package com.example.testdatabinding.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.testdatabinding.R
import com.example.testdatabinding.constants.Constants
import com.example.testdatabinding.data.model.TripModel
import com.example.testdatabinding.data.view_model.DetailsTripViewModel
import com.example.testdatabinding.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityDetailsBinding

    private val tripViewModel: DetailsTripViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)

        binding.viewModel= tripViewModel
        binding.lifecycleOwner=this

        val initTrip = intent.getParcelableExtra<TripModel>(Constants.TRIP)
        if(tripViewModel.trip.value==null){
            tripViewModel.setTrip(initTrip ?: TripModel(
                id = "",
                name ="",
                time = "",
                lat = 0.0,
                lng = 0.0
            ))

        }
        binding.fabMap.setOnClickListener {
            openMapsActivity(tripViewModel.trip.value ?: TripModel(
                id = "",
                name ="",
                time = "",
                lat = 0.0,
                lng = 0.0
            )
            )
        }
        binding.btnBack.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(Constants.TRIP_RESULT, tripViewModel.trip.value)
            setResult(RESULT_OK, resultIntent)
            finish()
        }


    }

    private val mapsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val updatedTrip = result.data?.getParcelableExtra<TripModel>(Constants.TRIP_RESULT)
                updatedTrip?.let {
                    tripViewModel.setTrip(it)

                }
            }
        }

    fun openMapsActivity(trip: TripModel) {
        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra(Constants.TRIP, trip)
        mapsLauncher.launch(intent)
    }


}
