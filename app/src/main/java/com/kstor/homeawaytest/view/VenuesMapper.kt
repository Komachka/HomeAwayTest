package com.kstor.homeawaytest.view

import com.kstor.homeawaytest.data.log
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.domain.model.VenuesCategory
import com.kstor.homeawaytest.domain.model.VenuesCategoryParcelize
import com.kstor.homeawaytest.domain.model.VenuesParcelize

interface VenuesMapper {

    fun map(venues: Venues): VenuesParcelize? {
        return try {
            VenuesParcelize(
                requireNotNull(venues.id),
                requireNotNull(venues.name),
                map(venues.categories),
                venues.address,
                requireNotNull(venues.distance),
                requireNotNull(venues.lat),
                requireNotNull(venues.lng)
            )
        } catch (e: Throwable) {
            log(e.message!!)
            null
        }
    }

    fun map(it: VenuesCategory?): VenuesCategoryParcelize? {
        return try {
            VenuesCategoryParcelize(
                requireNotNull(it?.id),
                requireNotNull(it?.name),
                requireNotNull(it?.iconPath)
            )
        } catch (e: IllegalArgumentException) {
            log(e.message!!)
            null
        }
    }
}
