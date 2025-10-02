package com.example.testdatabinding.data.map.controller

import android.view.View
import com.example.testdatabinding.data.map.interfaces.ILocationHandler
import com.example.testdatabinding.data.map.interfaces.IMapLifecycle
import com.example.testdatabinding.data.map.interfaces.IMarkerHandler
import com.example.testdatabinding.data.model.TripModel
import com.example.testdatabinding.data.view_model.MapViewModel
import com.google.android.material.snackbar.Snackbar
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class TripMapController(
    private val mapView: MapView,
    private val rootView: View,
    private val onTripUpdated: (TripModel) -> Unit,
    private val mapViewModel: MapViewModel
) : IMarkerHandler, ILocationHandler, IMapLifecycle {

    private var locationOverlay: MyLocationNewOverlay? = null
    private var currentMarker: Marker? = null

    override fun showTrip(trip: TripModel) {
        val point = GeoPoint(trip.lat, trip.lng)
        mapView.controller.setZoom(12.0)
        mapView.controller.setCenter(point)

        currentMarker = Marker(mapView).apply {
            position = point
            title = trip.name
        }
        mapView.overlays.add(currentMarker)

        addLongPressListener(trip)
    }

    private fun addLongPressListener(trip: TripModel) {
        val overlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean = false

            override fun longPressHelper(p: GeoPoint?): Boolean {
                val isEditable = mapViewModel.isAdjustable.value ?: false
                if (!isEditable) {
                    Snackbar.make(rootView, "Can't be edited", Snackbar.LENGTH_SHORT).show()
                    return false   // 👈 let zoom/pan still work
                }

                p?.let { geoPoint ->
                    // remove old marker
                    currentMarker?.let { mapView.overlays.remove(it) }

                    // add new marker
                    currentMarker = Marker(mapView).apply {
                        position = geoPoint
                        title = "New Marker"
                    }
                    mapView.overlays.add(currentMarker)
                    mapView.controller.setCenter(geoPoint)

                    Snackbar.make(
                        rootView,
                        "lat: ${geoPoint.latitude}, lng: ${geoPoint.longitude}",
                        Snackbar.LENGTH_SHORT
                    ).show()

                    // update trip
                    onTripUpdated(trip.copy(lat = geoPoint.latitude, lng = geoPoint.longitude))
                }
                return true   // 👈 consume only when editing
            }
        })
        mapView.overlays.add(0, overlay)
    }

    override fun enableMyLocation() {
        locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(mapView.context), mapView)
        locationOverlay?.enableMyLocation()
        mapView.overlays.add(locationOverlay)
    }

    override fun cleanup() {
        locationOverlay = null
        mapViewModel.disableEditing() // reset edit mode when cleaning up
    }
}
