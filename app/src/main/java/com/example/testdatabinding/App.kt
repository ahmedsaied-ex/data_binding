package com.example.testdatabinding


import android.app.Application
import com.example.testdatabinding.repository.TripRepository

class App : Application() {
    lateinit var tripRepository: TripRepository
        private set

    override fun onCreate() {
        super.onCreate()
        tripRepository = TripRepository(applicationContext)
    }
}