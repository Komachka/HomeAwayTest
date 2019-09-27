package com.kstor.homeawaytest.view.mainscreen

import android.view.View
import androidx.navigation.NavController
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.domain.model.VenuesParcelize

interface VenuesListPresenter {
    fun getVenues(query: String)
    fun navigateToMapScreen(navController: NavController, query: String)
    fun showError(throwable: Throwable)
    fun showProgress()
    fun navigateToDetailScreen(navController: NavController , venue: Venues)
    fun hideMupButton()
    fun addToFavorite(venues: Venues)
    fun getFavorites()
}
