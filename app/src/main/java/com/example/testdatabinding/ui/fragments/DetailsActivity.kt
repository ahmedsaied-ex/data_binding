package com.example.testdatabinding.ui.fragments

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.testdatabinding.R
import com.example.testdatabinding.constants.Constants
import com.example.testdatabinding.data.model.TripModel
import com.example.testdatabinding.data.view_model.DetailsTripViewModel
import com.example.testdatabinding.databinding.ActivityDetailsBinding
import com.example.testdatabinding.navigation.DetailsNavigationImpl


class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private val tripViewModel: DetailsTripViewModel by viewModels()
    private val navigator = DetailsNavigationImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)

        binding.viewModel = tripViewModel
        binding.lifecycleOwner = this

        val initTrip = intent.getParcelableExtra<TripModel>(Constants.TRIP)
        tripViewModel.initTrip(initTrip)

        navigator.register(this) { updatedTrip ->
            tripViewModel.updateTrip(updatedTrip)
        }

        binding.fabMap.setOnClickListener {
            tripViewModel.trip.value?.let { trip ->
                navigator.openActivity(this, trip) { updated ->
                    tripViewModel.updateTrip(updated)
                }
            }
        }

        binding.btnBack.setOnClickListener {
            navigator.returnResult(this, tripViewModel.trip.value)
        }
    }
}
