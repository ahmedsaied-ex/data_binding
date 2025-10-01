package com.example.testdatabinding.repository

import android.content.Context


import com.example.testdatabinding.data.model.TripModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TripRepository(context: Context) {
    companion object {
        private const val KEY_TRIPS = "trips_key"
    }
    private val prefs = SharedPrefs.Companion.getInstance(context)
    private val gson = Gson()
    private val listType = object : TypeToken<MutableList<TripModel>>() {}.type

    fun loadTrips(): MutableList<TripModel> {
        val json = prefs.read(KEY_TRIPS, "null")
        return if (json == "null") {
            val seed = mutableListOf(
                TripModel("1", "Alexandria", "10:00 AM", 31.2001, 29.9187),
                TripModel("2", "Hurghada", "11:30 AM", 27.2579, 33.8116),
                TripModel("3", "Luxor", "03:15 PM", 25.68, 32.71)
            )
            saveTrips(seed)
            seed
        } else {
            gson.fromJson(json, listType) ?: mutableListOf()
        }
    }

    fun saveTrips(list: List<TripModel>) {
        val json = gson.toJson(list)
        prefs.write(KEY_TRIPS, json)
    }
}