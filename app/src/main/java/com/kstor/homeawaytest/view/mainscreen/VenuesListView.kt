package com.kstor.homeawaytest.view.mainscreen

import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.base.ViewWithFavorites

interface VenuesListView : ViewWithFavorites {
    fun displayVenues(results: List<Venues>)
    fun showProgress()
    fun hideProgress()
    fun showMupButn()
    fun hideMupButn()
    fun showNoResult()
    fun hideNoResult()
}
