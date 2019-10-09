package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.domain.model.VenueDetails
import io.reactivex.Single
import javax.inject.Inject

class VenueDetailsUseCase @Inject constructor(private val detailsRepository: VenueDetailsRepository) {

    fun getVenueDetails(venueId: String): Single<VenueDetails> {
        return detailsRepository.getVenueDetails(venueId)
    }
}
