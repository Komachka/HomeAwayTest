package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.domain.model.VenuesData
import io.reactivex.Single

interface VenuesRepository {
    fun getClosestVenuses(limit: Int, query: String): Single<VenuesData>
}
