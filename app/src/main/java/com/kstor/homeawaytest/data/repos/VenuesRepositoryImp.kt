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
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.domain.model.VenuesData
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class VenuesRepositoryImp(
    private val remoteData: RemoteData,
    private val preferenceData: SharedPreferenceData,
    private val localData: LocalData
) : VenuesRepository {

    override fun getCityCenter(): Pair<Float, Float> {
        return preferenceData.getCityCenterInfo()
    }

    override fun removeFromFavorite(venues: Venues): Completable {
        return Completable.fromRunnable {
            mapToDBVenuesModel(venues)?.let {
                localData.removeFromFavorite(it)
            }
        }
    }

    override fun getFavorites(): Single<List<Venues>> {
        return localData.getFavorites().map {
            return@map mapToListOfVenues(it)
        }
    }

    override fun saveToFavorite(venues: Venues): Completable {
        return Completable.fromRunnable {
            mapToDBVenuesModel(venues)?.let {
                localData.addToFavorites(it)
            }
        }
    }

    override fun getClosestVenuses(limit: Int, query: String): Observable<List<Venues>> {
        return getRemoteData(limit, query)
            .flatMap { list ->
                localData.removeANdSaveVenues(mapToDBVenuesModelList(list))
                getLocalData()
            }
    }

    private fun mapToDBVenuesModelList(list: List<Venues>): List<DBVenuesModel> {
        val newList = mutableListOf<DBVenuesModel>()
        list.forEach {
            mapToDBVenuesModel(it)?.let { newModel -> newList.add(newModel) }
        }
        return newList
    }

    private fun getLocalData(): Observable<List<Venues>> {
        return localData.getAllVenues().map {
            return@map mapToListOfVenues(it)
        }.toObservable()
    }

    private fun getRemoteData(limit: Int, query: String): Observable<List<Venues>> {
        return remoteData.closedVenues(limit, query).map<VenuesData> {
            val venuesData = it.mapToVenuesData()
            log(venuesData.citCenterlat.toString())
            log(venuesData.citCenterlng.toString())
            preferenceData.setCityCenterInfo(venuesData.citCenterlat, venuesData.citCenterlng)
            val (l1, l2) = preferenceData.getCityCenterInfo()
            log("get remote data $l1 $l2")
            return@map venuesData
        }.map {
            it.venues
        }.toObservable()
    }
}
