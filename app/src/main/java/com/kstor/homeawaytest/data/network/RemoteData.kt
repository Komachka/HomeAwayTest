package com.kstor.homeawaytest.data.network

import com.kstor.homeawaytest.data.CLIENT_ID
import com.kstor.homeawaytest.data.CLIENT_SECRET
import com.kstor.homeawaytest.data.NEAR
import com.kstor.homeawaytest.data.V
import com.kstor.homeawaytest.data.network.model.NetworkVenuesModel
import io.reactivex.Observable
import io.reactivex.Single

class RemoteData(private val venuesService: VenuesService) {
    fun closedVenues(limit: Int, query: String): Single<NetworkVenuesModel> {
        return venuesService.getVenusesNetworkData(
            CLIENT_ID,
            CLIENT_SECRET,
            NEAR,
            query,
            V,
            limit
        )
    }
}
