package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.data.LOAD_LIMIT
import com.kstor.homeawaytest.domain.model.Venue
import io.reactivex.Observable
import javax.inject.Inject

class VenuesUseCase @Inject constructor(private val repository: VenuesRepository) {

    fun loadVenuesDataFromApi(query: String): Observable<List<Venue>> {
        return repository.getClosestVenuses(LOAD_LIMIT, query)
    }

    fun loadVenuesCache(): Observable<List<Venue>> {
        return repository.getClosestVenusesCache()
    }

    fun getCityCenter(): Pair<Float, Float> {
        return repository.getCityCenter()
    }
}
