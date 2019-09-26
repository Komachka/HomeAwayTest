package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.domain.model.Venues
import io.reactivex.Observable
import javax.inject.Inject

class GenerateStaticMapUrlUseCase @Inject constructor(private val repository: StaticMapRepository) {

    fun createStaticMapUrl(venuesParcelize: Venues): Observable<String> {
        return repository.createStaticMapUrl(venuesParcelize)
    }
}
