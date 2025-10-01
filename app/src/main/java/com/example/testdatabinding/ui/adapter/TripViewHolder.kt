package com.example.testdatabinding.ui.adapter

import androidx.recyclerview.widget.RecyclerView

import com.example.testdatabinding.data.model.TripModel
import com.example.testdatabinding.databinding.ItemTripBinding

class TripViewHolder(
    private val binding: ItemTripBinding,
    private val onItemClick: (TripModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(trip: TripModel) {
        binding.trip  = trip

        binding.root.setOnClickListener {
            onItemClick(trip)
        }
    }
}