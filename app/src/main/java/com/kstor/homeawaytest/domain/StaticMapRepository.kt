package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.domain.model.Venue
import io.reactivex.Observable

interface StaticMapRepository {
    fun createStaticMapUrl(venues: Venue): Observable<String>
}
