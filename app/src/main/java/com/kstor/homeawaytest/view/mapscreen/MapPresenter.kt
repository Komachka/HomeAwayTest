package com.kstor.homeawaytest.view.mapscreen

import android.view.View
import com.google.android.gms.maps.model.LatLng

interface MapPresenter {
    fun getVenues(query: String)
    fun navigateToDetailsScreen(view: View, position: LatLng)
    fun setUpMapToCityCenter(lat: Float, lng: Float)
}
