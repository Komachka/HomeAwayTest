package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.data.LOAD_LIMIT
import com.kstor.homeawaytest.domain.model.VenusData
import io.reactivex.Observable
import javax.inject.Inject

class VenuesUseCase @Inject constructor(private val repository: VenuesRepository) {

    fun loadVenuesData(query: String): Observable<VenusData> {
        return repository.getClosedVenuses(LOAD_LIMIT, query)
    }
}
