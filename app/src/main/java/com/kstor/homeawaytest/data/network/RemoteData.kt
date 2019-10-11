package com.kstor.homeawaytest.data.network

import com.kstor.homeawaytest.data.*
import com.kstor.homeawaytest.data.network.model.NetworkDetailsModel
import com.kstor.homeawaytest.data.network.model.NetworkVenuesModel
import java.io.IOException
import java.lang.Exception
import retrofit2.Response

class RemoteData(private val venuesService: VenuesService) {
    suspend fun closedVenues(limit: Int, query: String): ApiResult<NetworkVenuesModel> {
        return apiCall {
            venuesService.getVenusesNetworkData(
                CLIENT_ID,
                CLIENT_SECRET,
                NEAR,
                query,
                V,
                limit
            )
        }
    }

    suspend fun getDetailsOfAVenue(id: String): ApiResult<NetworkDetailsModel> {
        return apiCall {
            venuesService.getDetailsOfAVenue(id, CLIENT_ID, CLIENT_SECRET, V)
        }
    }

    private suspend fun <T : Any> apiCall(call: suspend () -> (Response<T>)): ApiResult<T> {
        try {
            call.invoke().let { response ->
                return if (response.isSuccessful) ApiResult.Succsses(response.body()!!)
                else ApiResult.Error<T>(IOException(response.errorBody().toString()))
            }
        } catch (ex: Exception) {
            return ApiResult.Error<T>(ex)
        }
    }
}
