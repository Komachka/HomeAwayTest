package com.kstor.homeawaytest.fake

import com.kstor.homeawaytest.domain.RepoResult
import com.kstor.homeawaytest.domain.VenueDetailsRepository
import com.kstor.homeawaytest.domain.model.VenueDetails
import io.reactivex.Single

class FakeDetailsRepository : VenueDetailsRepository {
    override suspend fun getVenueDetails(id: String): RepoResult<VenueDetails> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
