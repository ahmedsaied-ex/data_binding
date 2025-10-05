package com.example.testdatabinding.data.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testdatabinding.data.model.TripModel


class MapViewModel : ViewModel()  {
    private val _isAdjustable = MutableLiveData(false)
    val isAdjustable: LiveData<Boolean> = _isAdjustable

    private val _trip = MutableLiveData<TripModel>()
    val trip: LiveData<TripModel> = _trip

    fun enableEditing() {
        _isAdjustable.value = true
    }
    fun disableEditing() {
        _isAdjustable.value = false


    }

    fun updateTrip(updated: TripModel) {
        _trip.value = updated
    }

    fun initTrip(initial: TripModel?) {
        if (_trip.value == null) {
            _trip.value = initial ?: TripModel("", "", "", 0.0, 0.0)
        }
    }
}