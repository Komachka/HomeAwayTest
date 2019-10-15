package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.domain.model.Venue

interface VenuesRepository {
    suspend fun getClosestVenuses(limit: Int, query: String): RepoResult<List<Venue>>
    suspend fun saveToFavorite(venues: Venue): RepoResult<Boolean>
    suspend fun removeFromFavorite(venues: Venue): RepoResult<Boolean>
    suspend fun getFavorites(): RepoResult<List<Venue>>
    suspend fun getCityCenter(): RepoResult<Pair<Float, Float>>
    suspend fun getClosestVenusesCache(): RepoResult<List<Venue>>
}
