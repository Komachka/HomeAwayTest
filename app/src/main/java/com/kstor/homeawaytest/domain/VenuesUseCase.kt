package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.data.LOAD_LIMIT
import com.kstor.homeawaytest.domain.model.Venue
import javax.inject.Inject

class VenuesUseCase @Inject constructor(private val repository: VenuesRepository) {

    suspend fun loadVenuesDataFromApi(query: String): RepoResult<List<Venue>> {
        return when(val result = repository.getClosestVenuses(LOAD_LIMIT, query))
        {
            is RepoResult.Success -> RepoResult.Success(result.data.sortedBy { it.distance })
            is RepoResult.Error<*> -> result

        }
    }

    suspend fun loadVenuesCache(): RepoResult<List<Venue>> {
        return repository.getClosestVenusesCache()
    }

    suspend fun getCityCenter(): RepoResult<Pair<Float, Float>> {
        return repository.getCityCenter()
    }
}
