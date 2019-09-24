package com.kstor.homeawaytest.data.network

import com.kstor.homeawaytest.data.*
import com.kstor.homeawaytest.data.network.model.NetworkVenuesModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface VenuesService {
    @GET(SEARCH_URL)
    fun getVenusesNetworkData(
        @Query(CLIENT_ID_QUERY_PARAM) id: String,
        @Query(CLIENT_SECRET_QUERY_PARAM) secret: String,
        @Query(NEAR_QUERY_PARAM) near: String,
        @Query(SEARCH_QUERY_PARAM) query: String,
        @Query(V_QUERY_PARAM) v: String,
        @Query(LIMIT_QUERY_PARAM) limit: Int
    ): Single<NetworkVenuesModel>
}
