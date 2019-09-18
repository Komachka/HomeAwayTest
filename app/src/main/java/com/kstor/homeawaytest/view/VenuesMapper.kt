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
                requireNotNull(map(venues.categories)),
                venues.address,
                requireNotNull(venues.distance),
                requireNotNull(venues.lat),
                requireNotNull(venues.lng)
            )
        } catch (e: IllegalArgumentException) {
            log(e.message!!)
            null
        }
    }

    fun map(categories: List<VenuesCategory>?): List<VenuesCategoryParcelize>? {
        return categories?.let {
            try {
                categories.map {
                    VenuesCategoryParcelize(
                        requireNotNull(it.id),
                        requireNotNull(it.name),
                        requireNotNull(it.iconPath)
                    )
                }
            } catch (e: IllegalArgumentException) {
                log(e.message!!)
                null
            }
        }
    }
}
