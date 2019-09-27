package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.domain.model.Venues
import io.reactivex.Observable

interface StaticMapRepository {
    fun createStaticMapUrl(venues: Venues): Observable<String>
}
