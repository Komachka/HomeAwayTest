package com.kstor.homeawaytest.view.utils

import com.kstor.homeawaytest.data.log
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.domain.model.VenuesCategory
import com.kstor.homeawaytest.domain.model.VenuesCategoryParcelize
import com.kstor.homeawaytest.domain.model.VenuesParcelize

interface VenuesMapper {

    fun mapToPaprelize(venues: Venues): VenuesParcelize? {
        return try {
            VenuesParcelize(
                requireNotNull(venues.id),
                requireNotNull(venues.name),
                mapToPaprelize(venues.categories),
                venues.address,
                requireNotNull(venues.distance),
                requireNotNull(venues.lat),
                requireNotNull(venues.lng),
                venues.isFavorite
            )
        } catch (e: Throwable) {
            log(e.message!!)
            null
        }
    }

    fun mapToPaprelize(it: VenuesCategory?): VenuesCategoryParcelize? {
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

    fun mapToVenues(venues: VenuesParcelize): Venues? {
        return try {
            Venues(
                requireNotNull(venues.id),
                requireNotNull(venues.name),
                mapToVenuesCategory(venues.categories),
                venues.address,
                requireNotNull(venues.distance),
                requireNotNull(venues.lat),
                requireNotNull(venues.lng),
                venues.isFavorite
            )
        } catch (e: Throwable) {
            log(e.message!!)
            null
        }
    }

    fun mapToVenuesCategory(it: VenuesCategoryParcelize?): VenuesCategory? {
        return try {
            VenuesCategory(
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
