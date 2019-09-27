package com.kstor.homeawaytest.view.mainscreen

import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.base.BaseView

interface VenuesListView : BaseView {
    fun displayVenues(results: List<Venues>)
    fun showProgress()
    fun hideProgress()
    fun showMupButn()
    fun hideMupButn()
    fun updateItemView(venues: Venues)
}
