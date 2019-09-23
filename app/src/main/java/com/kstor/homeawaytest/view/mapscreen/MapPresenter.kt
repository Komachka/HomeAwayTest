package com.kstor.homeawaytest.view.mapscreen

import android.view.View
import com.kstor.homeawaytest.domain.model.VenuesParcelize

interface MapPresenter {
    fun getVenues(query: String)
    fun navigateToDetailsScreen(view: View, venuesParcelize: VenuesParcelize)
}
