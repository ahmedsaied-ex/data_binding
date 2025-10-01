package com.example.testdatabinding.data.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.testdatabinding.repository.TripRepository
import com.example.testdatabinding.data.model.TripModel

class TripViewModel(private val repo: TripRepository) : ViewModel() {
    private val _trips = MutableLiveData<List<TripModel>>()
    val trips: LiveData<List<TripModel>> = _trips

    private val _selectedTrip = MutableLiveData<TripModel?>()


    init {
        _trips.value = safeStart()
    }

    private fun safeStart(): List<TripModel> {
        return try {
            repo.loadTrips()
        } catch (e: Exception) {
            e.printStackTrace()
            mutableListOf()
        }
    }


    fun updateTripLocation(id: String, newLat: Double, newLng: Double) {
        try {
            val currentList = _trips.value?.toMutableList() ?: return
            val index = currentList.indexOfFirst { it.id == id }
            if (index != -1) {
                val updated = currentList[index].copy(lat = newLat, lng = newLng)
                currentList[index] = updated
                _trips.value = currentList
                _selectedTrip.value =
                    if (_selectedTrip.value?.id == id) updated else _selectedTrip.value
                repo.saveTrips(currentList)
                Log.d("TRIP_ID_LOG", "view model " + _trips.value.toString())
            } else {
                Log.e("TripViewModel", "TripModel with ID $id not found")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("TripViewModel", "Error updating trip location: ${e.message}")
        }
    }




}