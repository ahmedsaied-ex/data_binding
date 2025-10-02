package com.example.testdatabinding.navigation

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.example.testdatabinding.constants.Constants
import com.example.testdatabinding.data.model.TripModel
import com.example.testdatabinding.ui.fragments.DetailsActivity


class HomeNavigationImpl(
    private val detailsLauncher: ActivityResultLauncher<Intent>
) : ITripNavigatorHome {



    override fun openActivity(context: Context, trip: TripModel, callback: (TripModel) -> Unit) {
        val intent = Intent(context, DetailsActivity::class.java).apply {
            putExtra(Constants.TRIP, trip)
        }
        detailsLauncher.launch(intent)
    }
}