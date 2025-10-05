package com.example.testdatabinding.ui.adapter
import androidx.recyclerview.widget.DiffUtil
import com.example.testdatabinding.data.model.TripModel

class TripDiffCallback : DiffUtil.ItemCallback<TripModel>() {
    override fun areItemsTheSame(oldItem: TripModel, newItem: TripModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TripModel, newItem: TripModel): Boolean {
        return oldItem == newItem
    }
}
