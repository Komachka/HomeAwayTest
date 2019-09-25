package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.domain.model.VenuesParcelize
import io.reactivex.Observable

interface StaticMapRepository {
    fun createStaticMapUrl(venues: Venues): Observable<String>
}
