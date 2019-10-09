package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.domain.model.Venue
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface VenuesRepository {
    fun getClosestVenuses(limit: Int, query: String): Observable<List<Venue>>
    fun saveToFavorite(venues: Venue): Completable
    fun removeFromFavorite(venues: Venue): Completable
    fun getFavorites(): Single<List<Venue>>
    fun getCityCenter(): Pair<Float, Float>
    fun getClosestVenusesCache(): Observable<List<Venue>>
}
