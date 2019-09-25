package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.data.LOAD_LIMIT
import com.kstor.homeawaytest.domain.model.Venues
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class VenuesUseCase @Inject constructor(private val repository: VenuesRepository) {

    fun loadVenuesData(query: String): Observable<List<Venues>> {
        return repository.getClosestVenuses(LOAD_LIMIT, query)
    }

    fun addToFavorite(venues: Venues): Completable {
        return repository.saveToFavorite(venues)
    }

    fun getFavorites(): Single<List<Venues>> {
        return repository.getFavorites()
    }

    fun removeFromFavorite(venues: Venues) : Completable {
        return repository.removeFromFavorite(venues)
    }

    fun getCityCenter():Pair<Float, Float> {
        return repository.getCityCenter()
    }
}
