package com.kstor.homeawaytest.data.network

import com.kstor.homeawaytest.data.*
import com.kstor.homeawaytest.data.network.model.NetworkDetailsModel
import com.kstor.homeawaytest.data.network.model.NetworkVenuesModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VenuesService {
    @GET(SEARCH_URL)
    suspend fun getVenusesNetworkData(
        @Query(CLIENT_ID_QUERY_PARAM) id: String,
        @Query(CLIENT_SECRET_QUERY_PARAM) secret: String,
        @Query(NEAR_QUERY_PARAM) near: String,
        @Query(SEARCH_QUERY_PARAM) query: String,
        @Query(VERSION_QUERY_PARAM) v: String,
        @Query(LIMIT_QUERY_PARAM) limit: Int
    ): Response<NetworkVenuesModel>

    @GET(DETAIL_URL)
    suspend fun getDetailsOfAVenue(
        @Path("id")id: String,
        @Query(CLIENT_ID_QUERY_PARAM)clientId: String,
        @Query(CLIENT_SECRET_QUERY_PARAM) clientSecret: String,
        @Query(VERSION_QUERY_PARAM)v: String
    ): Response<NetworkDetailsModel>
}
