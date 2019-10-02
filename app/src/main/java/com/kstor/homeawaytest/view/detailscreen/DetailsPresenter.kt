package com.kstor.homeawaytest.view.detailscreen

import com.kstor.homeawaytest.domain.model.Venues

interface DetailsPresenter {
    fun createStaticMapUrl(venues: Venues)
    fun setFavorite(venues: Venues)
    // fun addAndRemoveFromFavorites(venues: Venues)
}
