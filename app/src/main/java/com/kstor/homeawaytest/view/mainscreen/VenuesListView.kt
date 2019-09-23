package com.kstor.homeawaytest.view.mainscreen

import com.kstor.homeawaytest.domain.model.Venues

interface VenuesListView {
    fun displayVenues(results: List<Venues>)
    fun showProgress()
    fun hideProgress()
}
