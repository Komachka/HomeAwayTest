package com.kstor.homeawaytest.view.detailscreen

import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.domain.model.VenuesParcelize

interface DetailsPresenter {
    fun createStaticMapUrl(venues: Venues)
    fun setFavorite(venues: Venues)
    fun addAndRemoveFromFavorites(venues: Venues)
}
