package com.kstor.homeawaytest.view.mainscreen

import android.view.View
import com.kstor.homeawaytest.domain.model.Venues

interface VenuesListPresenter {
    fun getVenues(query: String)
    fun showError(throwable: Throwable)
    fun showProgress()
    fun navigateToDetailScreen(view: View, venue: Venues)
}
