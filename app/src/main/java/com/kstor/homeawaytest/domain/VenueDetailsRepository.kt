package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.domain.model.VenueDetails

interface VenueDetailsRepository {
    suspend fun getVenueDetails(id: String): RepoResult<VenueDetails>
}
