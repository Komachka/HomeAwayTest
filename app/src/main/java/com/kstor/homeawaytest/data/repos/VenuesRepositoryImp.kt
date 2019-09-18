package com.kstor.homeawaytest.data.repos

import com.kstor.homeawaytest.data.mapToVenuesData
import com.kstor.homeawaytest.data.network.RemoteData
import com.kstor.homeawaytest.domain.VenuesRepository
import com.kstor.homeawaytest.domain.model.VenusData
import io.reactivex.Observable

class VenuesRepositoryImp(
    private val remoteData: RemoteData
) : VenuesRepository {
    override fun getClosedVenuses(limit: Int, query: String): Observable<VenusData> {
        return remoteData.closedVenues(limit, query).map<VenusData> {
            return@map it.mapToVenuesData()
        }
    }
}
