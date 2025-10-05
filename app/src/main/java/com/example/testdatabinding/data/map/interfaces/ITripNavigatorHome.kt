package com.example.testdatabinding.data.map.interfaces

import android.content.Context
import com.example.testdatabinding.data.model.TripModel

interface ITripNavigatorHome{
    fun openActivity(
        context: Context,
        trip: TripModel,
        callback: (TripModel) -> Unit
    )
}