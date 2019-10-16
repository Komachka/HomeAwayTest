package com.kstor.homeawaytest.data.network

import com.kstor.homeawaytest.data.CLIENT_ID_QUERY_PARAM
import com.kstor.homeawaytest.data.CLIENT_SECRET_QUERY_PARAM
import com.kstor.homeawaytest.data.DETAIL_URL
import com.kstor.homeawaytest.data.LIMIT_QUERY_PARAM
import com.kstor.homeawaytest.data.NEAR_QUERY_PARAM
import com.kstor.homeawaytest.data.SEARCH_QUERY_PARAM
import com.kstor.homeawaytest.data.SEARCH_URL
import com.kstor.homeawaytest.data.VERSION_QUERY_PARAM
import com.kstor.homeawaytest.data.network.model.NetworkDetailsModel
import com.kstor.homeawaytest.data.network.model.NetworkVenuesModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VenuesService {
    @GET(SEARCH_URL)
    fun getVenusesNetworkData(
        @Query(CLIENT_ID_QUERY_PARAM) id: String,
        @Query(CLIENT_SECRET_QUERY_PARAM) secret: String,
        @Query(NEAR_QUERY_PARAM) near: String,
        @Query(SEARCH_QUERY_PARAM) query: String,
        @Query(VERSION_QUERY_PARAM) v: String,
        @Query(LIMIT_QUERY_PARAM) limit: Int
    ): Single<NetworkVenuesModel>

    @GET(DETAIL_URL)
    fun getDetailsOfAVenue(
        @Path("id")id: String,
        @Query(CLIENT_ID_QUERY_PARAM)clientId: String,
        @Query(CLIENT_SECRET_QUERY_PARAM) clientSecret: String,
        @Query(VERSION_QUERY_PARAM)v: String
    ): Single<NetworkDetailsModel>
}
