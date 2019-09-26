package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.domain.model.Venues
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class FavoriteUseCase @Inject constructor(private val repository: VenuesRepository) {

    fun addToFavorite(venues: Venues): Completable {
        return repository.saveToFavorite(venues)
    }

    fun getFavorites(): Single<List<Venues>> {
        return repository.getFavorites()
    }

    fun removeFromFavorite(venues: Venues): Completable {
        return repository.removeFromFavorite(venues)
    }
}
