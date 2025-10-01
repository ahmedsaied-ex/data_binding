package com.example.testdatabinding.data.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testdatabinding.data.model.TripModel

class DetailsTripViewModel : ViewModel() {

    private val _trip = MutableLiveData<TripModel>()
    val trip: LiveData<TripModel> = _trip

    fun setTrip(trip: TripModel) {
        _trip.value = trip
    }
}