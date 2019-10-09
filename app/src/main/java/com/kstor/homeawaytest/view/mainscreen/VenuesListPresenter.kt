package com.kstor.homeawaytest.view.mainscreen

import androidx.navigation.NavController
import com.kstor.homeawaytest.domain.model.Venue

interface VenuesListPresenter {
    fun getVenues(query: String)
    fun navigateToMapScreen(navController: NavController, query: String)
    fun showError(throwable: Throwable)
    fun showProgress()
    fun navigateToDetailScreen(navController: NavController, venue: Venue)
    fun hideMupButton()
    fun getFavorites()
    fun addAndRemoveFromFavorites(venue: Venue)
}
