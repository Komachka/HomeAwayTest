package com.kstor.homeawaytest.view.mainscreen

import com.kstor.homeawaytest.domain.model.Venue
import com.kstor.homeawaytest.view.base.ViewWithFavorites

interface VenuesListView : ViewWithFavorites {
    fun displayVenues(results: List<Venue>)
    fun showProgress()
    fun hideProgress()
    fun showMupButn()
    fun hideMupButn()
}
