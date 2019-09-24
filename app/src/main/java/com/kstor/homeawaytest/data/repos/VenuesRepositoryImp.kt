package com.kstor.homeawaytest.data.repos

import com.kstor.homeawaytest.data.mapToVenuesData
import com.kstor.homeawaytest.data.network.RemoteData
import com.kstor.homeawaytest.data.sp.SharedPreferenceData
import com.kstor.homeawaytest.domain.VenuesRepository
import com.kstor.homeawaytest.domain.model.VenuesData
import io.reactivex.Single

class VenuesRepositoryImp(
    private val remoteData: RemoteData,
    private val preferenceData: SharedPreferenceData
) : VenuesRepository {
    override fun getClosestVenuses(limit: Int, query: String): Single<VenuesData> {
        return remoteData.closedVenues(limit, query).map<VenuesData> {
            val venuesData = it.mapToVenuesData()
            preferenceData.setCityCenterInfo(venuesData.citCenterlat, venuesData.citCenterlat)
            return@map venuesData
        }
    }
}
