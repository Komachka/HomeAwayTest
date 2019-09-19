package com.kstor.homeawaytest.view.mainscreen

interface VenuesListPresenter {
    fun setView(view: VenuesListView)
    fun getVenues(query: String)
}
