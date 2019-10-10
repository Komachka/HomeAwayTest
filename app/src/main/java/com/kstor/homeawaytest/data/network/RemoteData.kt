package com.kstor.homeawaytest.data.network

import com.kstor.homeawaytest.data.*
import com.kstor.homeawaytest.data.network.model.NetworkDetailsModel
import com.kstor.homeawaytest.data.network.model.NetworkVenuesModel
import java.io.IOException
import java.lang.Exception

class RemoteData(private val venuesService: VenuesService) {
    suspend fun closedVenues(limit: Int, query: String): ApiResult<NetworkVenuesModel> {
        try {
            val responce = venuesService.getVenusesNetworkData(
                CLIENT_ID,
                CLIENT_SECRET,
                NEAR,
                query,
                V,
                limit
            )
            return if (responce.isSuccessful) ApiResult.Succsses(responce.body()!!)
            else ApiResult.Error<NetworkVenuesModel>(IOException(responce.errorBody().toString()))
        } catch (ex: Exception) {
            return ApiResult.Error<NetworkVenuesModel>(ex)
        }
    }

    suspend fun getDetailsOfAVenue(id: String): ApiResult<NetworkDetailsModel> {
        try {
          val responce = venuesService.getDetailsOfAVenue(id, CLIENT_ID, CLIENT_SECRET, V)
          return if (responce.isSuccessful) ApiResult.Succsses(responce.body()!!)
            else ApiResult.Error<NetworkDetailsModel>(IOException(responce.errorBody().toString()))
        } catch (ex: Exception) {
            return ApiResult.Error<NetworkVenuesModel>(ex)
        }
    }
}
