package com.kstor.homeawaytest.view.mainscreen

import android.view.View
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.domain.model.VenuesParcelize

interface VenuesListPresenter {
    fun getVenues(query: String)
    fun navigateToMapScreen(view: View)
    fun navigateToDetailsScreen(view: View, venuesParcelize: VenuesParcelize)
    fun showError(throwable: Throwable)
    fun showProgress()
    fun navigateToDetailScreen(view: View, venue: Venues)
    fun hideMupButton()
    fun addToFavorite(venues: Venues)
    fun getFavorites()
}
