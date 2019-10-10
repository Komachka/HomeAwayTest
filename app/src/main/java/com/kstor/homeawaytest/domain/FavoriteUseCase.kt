package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.domain.model.Venue
import javax.inject.Inject

class FavoriteUseCase @Inject constructor(private val repository: VenuesRepository) {

    suspend fun addToFavorite(venues: Venue): RepoResult<Boolean> {
        return repository.saveToFavorite(venues)
    }

    suspend fun getFavorites(): RepoResult<List<Venue>> {
        return repository.getFavorites()
    }

    suspend fun removeFromFavorite(venues: Venue): RepoResult<Boolean> {
        return repository.removeFromFavorite(venues)
    }
}
