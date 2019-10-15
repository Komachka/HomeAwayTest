package com.kstor.homeawaytest.data.repos

import com.kstor.homeawaytest.data.ApiResult
import com.kstor.homeawaytest.data.mapToVenueDetails
import com.kstor.homeawaytest.data.network.RemoteData
import com.kstor.homeawaytest.domain.RepoResult
import com.kstor.homeawaytest.domain.VenueDetailsRepository
import com.kstor.homeawaytest.domain.model.VenueDetails
import javax.inject.Inject

class VenueDetailsRepositoryImpl @Inject constructor(private val remoteData: RemoteData) : VenueDetailsRepository {
    override suspend fun getVenueDetails(id: String): RepoResult<VenueDetails> {
        return when (val details = remoteData.getDetailsOfAVenue(id)) {
            is ApiResult.Succsses -> {
                val data = mapToVenueDetails(details.data)
                RepoResult.Success(data)
            }
            is ApiResult.Error<*> -> RepoResult.Error<VenueDetails>(details.throwable)
        }
    }
}
