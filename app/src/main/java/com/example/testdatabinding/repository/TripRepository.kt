package com.example.testdatabinding.repository

import android.content.Context
import com.example.testdatabinding.data.MyTrips


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
        return try {
            val json = prefs.read(KEY_TRIPS, "null")
             if (json == "null") {
                val seed = MyTrips.myList
                saveTrips(seed)
                seed
            } else {
                gson.fromJson(json, listType) ?: mutableListOf()
            }
        }catch (e : Exception){
            e.printStackTrace()
            // fallback if something goes wrong
            mutableListOf()
        }
    }

    fun saveTrips(list: List<TripModel>) {
        try {
            val json = gson.toJson(list)
            prefs.write(KEY_TRIPS, json)
        }catch ( e: Exception){
            e.printStackTrace()
        }
    }
}