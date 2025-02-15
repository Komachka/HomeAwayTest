package com.kstor.homeawaytest.view.detailscreen

import android.content.Context
import androidx.navigation.NavController
import com.kstor.homeawaytest.domain.model.Venue

interface DetailsPresenter {
    fun createStaticMapUrl(venues: Venue)
    fun setFavorite(venues: Venue)
    fun addAndRemoveFromFavorites(venues: Venue)
    fun getVenueDetails(venues: Venue)
    fun navigateBack(findNavController: NavController)
    fun openBrowser(context: Context, url: String)
    fun fillDetailsScreen(venues: Venue)
}
