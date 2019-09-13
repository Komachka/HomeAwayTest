package com.kstor.homeawaytest.data.network

import com.kstor.homeawaytest.data.VenusData
import com.kstor.homeawaytest.data.mapToVenuesData
import io.reactivex.Single

class VenuesRepositoryImp(
    private val remoteData: RemoteData
)
{
    fun getClosedVenueses(limit:Int, query:String):Single<VenusData>
    {
        return remoteData.closedVenuses(limit, query).map<VenusData> {
            return@map it.mapToVenuesData()
        }
    }
}