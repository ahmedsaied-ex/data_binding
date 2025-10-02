package com.example.testdatabinding.data.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel()  {
    private val _isAdjustable = MutableLiveData(false)
    val isAdjustable: LiveData<Boolean> = _isAdjustable

    fun enableEditing() {
        _isAdjustable.value = true
    }

    fun disableEditing() {
        _isAdjustable.value = false
    }
}