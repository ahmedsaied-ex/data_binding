package com.example.testdatabinding.navigation

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.testdatabinding.constants.Constants
import com.example.testdatabinding.data.model.TripModel
import com.example.testdatabinding.ui.fragments.DetailsActivity

class DetailsNavigationLauncher : ITripNavigatorDetails {

    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    fun register(activity: AppCompatActivity, onResult: (TripModel) -> Unit) {
        resultLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val updatedTrip = result.data?.getParcelableExtra<TripModel>(Constants.TRIP_RESULT)
                updatedTrip?.let(onResult)
            }
        }
    }


    override fun openActivity(
        activity: AppCompatActivity,
        trip: TripModel,
        callback: (TripModel) -> Unit
    ) {
        val intent = Intent(activity, DetailsActivity::class.java)
        intent.putExtra(Constants.TRIP, trip)
        resultLauncher?.launch(intent)
    }

    override fun returnResult(
        activity: AppCompatActivity,
        trip: TripModel?
    ) {
        val resultIntent = Intent().apply {
            putExtra(Constants.TRIP_RESULT, trip)
        }
        activity.setResult(AppCompatActivity.RESULT_OK, resultIntent)
        activity.finish()
    }

}