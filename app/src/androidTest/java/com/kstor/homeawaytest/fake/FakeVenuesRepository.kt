package com.kstor.homeawaytest.view.di.mock

import com.kstor.homeawaytest.data.LOCAL_DATA_EMPTY
import com.kstor.homeawaytest.domain.RepoResult
import com.kstor.homeawaytest.domain.VenuesRepository
import com.kstor.homeawaytest.domain.model.Venue
import com.kstor.homeawaytest.domain.model.VenuesCategory

class FakeVenuesRepository : VenuesRepository {

    val venues = listOf<Venue>(
        Venue(
            "56d249bc498ef524220083f3",
            "Hoffman's House of Horrors",
            VenuesCategory("4bf58dd8d48988d117941735",
                "Beer Garden",
                "https://ss3.4sqi.net/img/categories_v2/nightlife/beergarden_32.png"),
            "7986 Emery Blvd NE",
            28882,
            47.633885804708434,
            -122.71479606628417,
            true
        ),
        Venue(
            "56d249bc498ef524220083f32",
            "Coffee Shop",
            VenuesCategory("4bf58dd8d48988d117941735",
                "Beer Garden",
                "https://ss3.4sqi.net/img/categories_v2/nightlife/beergarden_32.png"),
            "7986 Emery Blvd NE",
            28882,
            47.633885804708434,
            -122.71479606628417,
            true
        ),
        Venue(
            "56d249bc498ef524220083f32",
            "Zoo",
            VenuesCategory("4bf58dd8d48988d117941735",
                "Beer Garden",
                "https://ss3.4sqi.net/img/categories_v2/nightlife/beergarden_32.png"),
            "7986 Emery Blvd NE",
            28882,
            47.633885804708434,
            -122.71479606628417,
            false
        )
    )

    val favorives = mutableListOf<Venue>(
        Venue(
            "56d249bc498ef524220083f3",
            "Hoffman's House of Horrors",
            VenuesCategory("4bf58dd8d48988d117941735",
                "Beer Garden",
                "https://ss3.4sqi.net/img/categories_v2/nightlife/beergarden_32.png"),
            "7986 Emery Blvd NE",
            28882,
            47.633885804708434,
            -122.71479606628417,
            true
        )
    )

    override suspend fun getClosestVenuses(limit: Int, query: String): RepoResult<List<Venue>> {
        venues.take(limit).filter { it.name?.toLowerCase()?.contains(query.toLowerCase()) ?: false }.let {
            list ->
            return if (list.isNotEmpty()) RepoResult.Success(list)
            else RepoResult.Error<List<Venue>>(Throwable(LOCAL_DATA_EMPTY))
        }
    }

    override suspend fun saveToFavorite(venue: Venue): RepoResult<Boolean> {
        favorives.add(venue)
        return RepoResult.Success(true)
    }

    override suspend fun removeFromFavorite(venue: Venue): RepoResult<Boolean> {
        favorives.remove(venue)
        return RepoResult.Success(true)
    }

    override suspend fun getFavorites(): RepoResult<List<Venue>> {
        return RepoResult.Success(favorives)
    }

    override suspend fun getCityCenter(): RepoResult<Pair<Float, Float>> {
        return RepoResult.Success(40.0F to 50.0F)
    }

    override suspend fun getClosestVenusesCache(): RepoResult<List<Venue>> {
        return RepoResult.Success(venues)
    }
}
