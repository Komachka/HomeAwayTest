package com.kstor.homeawaytest.view.utils

import com.kstor.homeawaytest.data.logError
import com.kstor.homeawaytest.domain.model.Venue
import com.kstor.homeawaytest.domain.model.VenuesCategory
import com.kstor.homeawaytest.domain.model.VenuesCategoryParcelize
import com.kstor.homeawaytest.domain.model.VenuesParcelize

interface VenuesMapper {

    fun mapToParcelize(venues: Venue): VenuesParcelize? {
        return try {
            VenuesParcelize(
                requireNotNull(venues.id),
                requireNotNull(venues.name),
                mapToParcelize(venues.categories),
                venues.address,
                requireNotNull(venues.distance),
                requireNotNull(venues.lat),
                requireNotNull(venues.lng),
                venues.isFavorite
            )
        } catch (e: Throwable) {
            logError(e.message!!)
            null
        }
    }

    fun mapToParcelize(it: VenuesCategory?): VenuesCategoryParcelize? {
        return try {
            VenuesCategoryParcelize(
                requireNotNull(it?.id),
                requireNotNull(it?.name),
                requireNotNull(it?.iconPath)
            )
        } catch (e: IllegalArgumentException) {
            logError(e.message!!)
            null
        }
    }

    fun mapToVenues(venues: VenuesParcelize): Venue? {
        return try {
            Venue(
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
            logError(e.message!!)
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
            logError(e.message!!)
            null
        }
    }
}
