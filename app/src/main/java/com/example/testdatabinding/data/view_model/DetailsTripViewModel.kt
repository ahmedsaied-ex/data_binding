package com.example.testdatabinding.data.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testdatabinding.data.model.TripModel

class DetailsTripViewModel : ViewModel() {

    private val _trip = MutableLiveData<TripModel>()
    val trip: LiveData<TripModel> = _trip
    fun initTrip(initial: TripModel?) {
        if (_trip.value == null) {
            _trip.value = initial ?: TripModel("", "", "", 0.0, 0.0)
        }
    }

    fun updateTrip(updated: TripModel) {
        _trip.value = updated
    }
}