package com.example.testdatabinding.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.testdatabinding.data.model.TripModel
import com.example.testdatabinding.databinding.ItemTripBinding

class TripAdapter(
    private val trips: List<TripModel>,
    private val onItemClick: (TripModel) -> Unit
) : RecyclerView.Adapter<TripViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val binding = ItemTripBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TripViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.bind(trips[position])
        Log.d("TRIP_ID_LOG  Binding",trips[position].lat.toString())
    }

    override fun getItemCount() = trips.size
}