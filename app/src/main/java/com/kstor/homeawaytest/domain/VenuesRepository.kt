package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.domain.model.VenusData
import io.reactivex.Observable

interface VenuesRepository {
    fun getClosestVenuses(limit: Int, query: String): Observable<VenusData>
}
