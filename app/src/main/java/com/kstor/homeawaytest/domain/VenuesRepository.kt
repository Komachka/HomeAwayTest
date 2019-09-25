package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.domain.model.Venues
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface VenuesRepository {
    fun getClosestVenuses(limit: Int, query: String): Observable<List<Venues>>
    fun saveToFavorite(venues: Venues): Completable
    fun removeFromFavorite(venues: Venues) : Completable
    fun getFavorites(): Single<List<Venues>>
    fun getCityCenter(): Pair<Float, Float>
}
