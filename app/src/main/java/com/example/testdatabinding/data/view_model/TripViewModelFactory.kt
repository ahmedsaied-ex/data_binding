package com.example.testdatabinding.data.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testdatabinding.App
import com.example.testdatabinding.repository.TripRepository

class TripViewModelFactory (private val app: App) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TripViewModel(app.tripRepository) as T

        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}