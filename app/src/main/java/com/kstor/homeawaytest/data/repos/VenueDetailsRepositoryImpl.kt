package com.kstor.homeawaytest.data.repos

import com.kstor.homeawaytest.data.mapToVenueDetails
import com.kstor.homeawaytest.data.network.RemoteData
import com.kstor.homeawaytest.domain.VenueDetailsRepository
import com.kstor.homeawaytest.domain.model.HoursPerDay
import com.kstor.homeawaytest.domain.model.VenueDetails
import io.reactivex.Single
import javax.inject.Inject

class VenueDetailsRepositoryImpl @Inject constructor(private val remoteData: RemoteData) : VenueDetailsRepository {
    override fun getVenueDetails(id: String): Single<VenueDetails> {
        return remoteData.getDetailsOfAVenue(id).map {
            mapToVenueDetails(it)
        }
    }
}
