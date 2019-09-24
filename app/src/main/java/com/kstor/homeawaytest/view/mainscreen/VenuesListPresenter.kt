package com.kstor.homeawaytest.view.mainscreen

interface VenuesListPresenter {
    fun getVenues(query: String)
    fun showError(throwable: Throwable)
    fun showProgress()

}
