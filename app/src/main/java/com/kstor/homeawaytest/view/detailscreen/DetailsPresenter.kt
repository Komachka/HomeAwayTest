package com.kstor.homeawaytest.view.detailscreen

import com.kstor.homeawaytest.domain.model.VenuesParcelize

interface DetailsPresenter {
    fun createStaticMapUrl(venues: VenuesParcelize)
}
