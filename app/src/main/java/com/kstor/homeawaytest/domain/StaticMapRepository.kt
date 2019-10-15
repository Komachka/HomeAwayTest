package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.domain.model.Venue

interface StaticMapRepository {
    suspend fun createStaticMapUrl(venues: Venue): String
}
