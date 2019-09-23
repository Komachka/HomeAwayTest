package com.kstor.homeawaytest.view.mainscreen

import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.BaseView

interface VenuesListView : BaseView {
    fun displayVenues(results: List<Venues>)
    fun showProgress()
    fun hideProgress()
}
