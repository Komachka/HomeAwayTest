package com.kstor.homeawaytest.data.repos

import com.kstor.homeawaytest.data.db.LocalData
import com.kstor.homeawaytest.data.mapToListOfVenues
import com.kstor.homeawaytest.data.mapToVenuesData
import com.kstor.homeawaytest.data.network.RemoteData
import com.kstor.homeawaytest.data.sp.SharedPreferenceData
import com.kstor.homeawaytest.domain.VenuesRepository
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.domain.model.VenuesData
import io.reactivex.Observable

class VenuesRepositoryImp(
    private val remoteData: RemoteData,
    private val preferenceData: SharedPreferenceData,
    private val localData: LocalData
) : VenuesRepository {
    override fun getClosestVenuses(limit: Int, query: String): Observable<List<Venues>> {

        return Observable.concatArray(
            getLocalData(),
            getRemoteData(limit, query)
        )
    }

    private fun getLocalData(): Observable<List<Venues>> { return localData.getFavorites().map {
            return@map mapToListOfVenues(it)
        }.toObservable()
    }

    private fun getRemoteData(limit: Int, query: String): Observable<List<Venues>> {
        return remoteData.closedVenues(limit, query).map<VenuesData> {
            val venuesData = it.mapToVenuesData()
            preferenceData.setCityCenterInfo(venuesData.citCenterlat, venuesData.citCenterlat)
            return@map venuesData
        }.map {
            it.venues
        }.toObservable()
    }
}
