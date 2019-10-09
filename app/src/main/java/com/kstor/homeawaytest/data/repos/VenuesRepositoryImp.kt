package com.kstor.homeawaytest.data.repos

import com.kstor.homeawaytest.data.db.LocalData
import com.kstor.homeawaytest.data.db.model.DBVenuesModel
import com.kstor.homeawaytest.data.log
import com.kstor.homeawaytest.data.mapToDBVenuesModel
import com.kstor.homeawaytest.data.mapToListOfVenues
import com.kstor.homeawaytest.data.mapToVenuesData
import com.kstor.homeawaytest.data.network.RemoteData
import com.kstor.homeawaytest.data.sp.SharedPreferenceData
import com.kstor.homeawaytest.domain.VenuesRepository
import com.kstor.homeawaytest.domain.model.Venue
import com.kstor.homeawaytest.domain.model.VenuesData
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class VenuesRepositoryImp(
    private val remoteData: RemoteData,
    private val preferenceData: SharedPreferenceData,
    private val localData: LocalData
) : VenuesRepository {

    override fun getClosestVenusesCache(): Observable<List<Venue>> {
        return getLocalData()
    }

    override fun getCityCenter(): Pair<Float, Float> {
        return preferenceData.getCityCenterInfo()
    }

    override fun removeFromFavorite(venues: Venue): Completable {
        return Completable.fromRunnable {
            mapToDBVenuesModel(venues)?.let {
                localData.removeFromFavorite(it)
            }
        }
    }

    override fun getFavorites(): Single<List<Venue>> {
        return localData.getFavorites().map {
            return@map mapToListOfVenues(it)
        }
    }

    override fun saveToFavorite(venues: Venue): Completable {
        return Completable.fromRunnable {
            mapToDBVenuesModel(venues)?.let {
                localData.addToFavorites(it)
            }
        }
    }

    override fun getClosestVenuses(limit: Int, query: String): Observable<List<Venue>> {
        return Observable.concatArray(getLocalData(), getRemoteData(limit, query)
            .flatMap { list ->
                localData.removeANdSaveVenues(mapToDBVenuesModelList(list))
                getLocalData()
            }
            .doOnError {
                log(it.toString())
            }
        )
    }

    private fun mapToDBVenuesModelList(list: List<Venue>): List<DBVenuesModel> {
        val newList = mutableListOf<DBVenuesModel>()
        list.forEach {
            mapToDBVenuesModel(it)?.let { newModel -> newList.add(newModel) }
        }
        return newList
    }

    private fun getLocalData(): Observable<List<Venue>> {
        return localData.getAllVenues().map {
            return@map mapToListOfVenues(it)
        }.toObservable()
    }

    private fun getRemoteData(limit: Int, query: String): Observable<List<Venue>> {
        return remoteData.closedVenues(limit, query).map<VenuesData> {
            val venuesData = it.mapToVenuesData()
            preferenceData.setCityCenterInfo(venuesData.citCenterlat, venuesData.citCenterlng)
            return@map venuesData
        }.map {
            it.venues
        }.toObservable()
    }
}
