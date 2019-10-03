package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.domain.model.VenueDetails
import io.reactivex.Single

interface VenueDetailsRepository {
    fun getVenueDetails(id: String): Single<VenueDetails>
}
