package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.domain.model.Venue
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class FavoriteUseCase @Inject constructor(private val repository: VenuesRepository) {

    fun addToFavorite(venues: Venue): Completable {
        return repository.saveToFavorite(venues)
    }

    fun getFavorites(): Single<List<Venue>> {
        return repository.getFavorites()
    }

    fun removeFromFavorite(venues: Venue): Completable {
        return repository.removeFromFavorite(venues)
    }
}
