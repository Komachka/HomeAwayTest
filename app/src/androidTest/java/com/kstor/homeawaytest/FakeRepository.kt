package com.kstor.homeawaytest.view.di.mock

import com.kstor.homeawaytest.domain.VenuesRepository
import com.kstor.homeawaytest.domain.model.Venues
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single


class FakeRepository : VenuesRepository {

    val venues = listOf<Venues>()

    val favorives = mutableListOf<Venues>()

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