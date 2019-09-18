package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.domain.model.VenuesParcelize
import io.reactivex.Observable

class GenerateStaticMapUrlUseCase(val repository: StaticMapRepository) {

    fun createStaticMapUrl(venuesParcelize: VenuesParcelize): Observable<String> {
        return repository.createStaticMapUrl(venuesParcelize)
    }
}
