package com.example.testdatabinding.ui.adapter
import androidx.recyclerview.widget.DiffUtil
import com.example.testdatabinding.data.model.TripModel

class TripDiffCallback : DiffUtil.ItemCallback<TripModel>() {
    override fun areItemsTheSame(oldItem: TripModel, newItem: TripModel): Boolean {
        // Uniquely identify trip
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TripModel, newItem: TripModel): Boolean {
        // Compare all fields (data class already implements equals correctly)
        return oldItem == newItem
    }
}
