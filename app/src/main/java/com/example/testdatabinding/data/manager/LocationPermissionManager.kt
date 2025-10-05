package com.example.testdatabinding.data.manager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat


class LocationPermissionManager(
    private val context: Context,
    private val permissionLauncher: ActivityResultLauncher<String>,
) {
    fun checkAndRequestLocation(onGranted: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                onGranted()
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

}
