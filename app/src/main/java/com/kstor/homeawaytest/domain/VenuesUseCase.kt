package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.data.LOAD_LIMIT
import com.kstor.homeawaytest.domain.model.VenuesData
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class VenuesUseCase @Inject constructor(private val repository: VenuesRepository) {

    fun loadVenuesData(query: String): Single<VenuesData> {
        return repository.getClosestVenuses(LOAD_LIMIT, query)
    }
}
