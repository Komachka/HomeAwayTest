package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.domain.model.VenueDetails
import javax.inject.Inject

class VenueDetailsUseCase @Inject constructor(private val detailsRepository: VenueDetailsRepository) {

    suspend fun getVenueDetails(venueId: String): RepoResult<VenueDetails> {
        return detailsRepository.getVenueDetails(venueId)
    }
}
