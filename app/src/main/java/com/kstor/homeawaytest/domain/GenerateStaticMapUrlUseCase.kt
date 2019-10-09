package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.domain.model.Venue
import io.reactivex.Observable
import javax.inject.Inject

class GenerateStaticMapUrlUseCase @Inject constructor(private val repository: StaticMapRepository) {

    fun createStaticMapUrl(venuesParcelize: Venue): Observable<String> {
        return repository.createStaticMapUrl(venuesParcelize)
    }
}
