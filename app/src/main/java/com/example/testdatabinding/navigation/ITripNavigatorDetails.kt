package com.example.testdatabinding.navigation

import androidx.appcompat.app.AppCompatActivity
import com.example.testdatabinding.data.model.TripModel

interface ITripNavigatorDetails {
    fun openActivity(activity: AppCompatActivity, trip: TripModel, callback: (TripModel) -> Unit)
    fun returnResult(activity: AppCompatActivity, trip: TripModel?)
}

