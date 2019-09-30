package com.kstor.homeawaytest.view.di.mock

import com.kstor.homeawaytest.domain.VenuesRepository
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.domain.model.VenuesCategory
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single


class FakeRepository : VenuesRepository {

    val venues = listOf<Venues>(
        Venues(
            "56d249bc498ef524220083f3",
            "Hoffman's House of Horrors",
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

    val favorives = mutableListOf<Venues>(
        Venues(
            "56d249bc498ef524220083f3",
            "Hoffman's House of Horrors",
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

    override fun getClosestVenuses(limit: Int, query: String): Observable<List<Venues>> {
        return Observable.just(venues.take(limit))
    }

    override fun saveToFavorite(venues: Venues): Completable {
        return Completable.fromRunnable {
            favorives.add(venues)
        }
    }

    override fun removeFromFavorite(venues: Venues): Completable {
        return Completable.fromRunnable {
            favorives.remove(venues)
        }
    }

    override fun getFavorites(): Single<List<Venues>> {
        return Observable.just(favorives as List<Venues>).firstOrError()
    }

    override fun getCityCenter(): Pair<Float, Float> {
        return 40.0F to 50.0F
    }

    override fun getClosestVenusesCache(): Observable<List<Venues>> {
        return Observable.just(venues)
    }
}