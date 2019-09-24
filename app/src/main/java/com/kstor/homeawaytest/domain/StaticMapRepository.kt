package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.domain.model.VenuesParcelize
import io.reactivex.Observable

interface StaticMapRepository {
    fun createStaticMapUrl(venues: VenuesParcelize): Observable<String>
}
