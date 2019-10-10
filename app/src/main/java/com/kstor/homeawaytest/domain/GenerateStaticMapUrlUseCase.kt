package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.domain.model.Venue
import io.reactivex.Observable
import javax.inject.Inject

class GenerateStaticMapUrlUseCase @Inject constructor(private val repository: StaticMapRepository) {

    suspend fun createStaticMapUrl(venuesParcelize: Venue): String {
        return repository.createStaticMapUrl(venuesParcelize)
    }
}
