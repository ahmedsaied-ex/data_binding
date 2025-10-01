package com.example.testdatabinding.ui.fragments

import TripAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testdatabinding.App
import com.example.testdatabinding.constants.Constants
import com.example.testdatabinding.data.model.TripModel

import com.example.testdatabinding.data.view_model.TripViewModel
import com.example.testdatabinding.data.view_model.TripViewModelFactory

import com.example.testdatabinding.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val tripViewModel: TripViewModel by viewModels {
        TripViewModelFactory(requireActivity().application as App)
    }

    private lateinit var tripAdapter: TripAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tripAdapter = TripAdapter { trip ->
            openDetailsActivity(trip)
        }

        binding.rvTrips.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTrips.adapter = tripAdapter

        tripViewModel.trips.observe(viewLifecycleOwner) { trips ->
            tripAdapter.submitList(trips)
        }
    }

    private val detailsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val updatedTrip = result.data?.getParcelableExtra<TripModel>(Constants.TRIP_RESULT)
                updatedTrip?.let {
                    tripViewModel.updateTripLocation(id = it.id, newLat = it.lat, newLng = it.lng)
                }
            }
        }

    fun openDetailsActivity(trip: TripModel) {
        val intent = Intent(requireContext(), DetailsActivity::class.java)
        intent.putExtra(Constants.TRIP, trip)
        detailsLauncher.launch(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
