package com.kstor.homeawaytest.data

import com.kstor.homeawaytest.data.network.model.NetworkCategory
import com.kstor.homeawaytest.data.network.model.NetworkVenue
import com.kstor.homeawaytest.data.network.model.NetworkVenuesModel

fun NetworkVenuesModel. mapToVenuesData() = VenusData(
    createListOfCategories(this.response?.venues),
    response?.geocode?.feature?.geometry?.center?.lat?: 47.6062, // TODO move to const
    response?.geocode?.feature?.geometry?.center?.lng?: 122.3321
)

private fun createListOfCategories(venues: List<NetworkVenue>?): List<Venue> {
    venues?.let { venues ->
        return venues.filter {
            (it.id != null && it.name != null && it.categories != null && it.location != null
                    && it.location!!.address != null && it.location!!.lat != null && it.location!!.lat != null) }
            .map {
                Venue(it.id!!,
                    it.name!!,
                    mapToCategory(it.categories!!),
                    it.location!!.address!!,
                    it.location!!.lat!!,
                    it.location!!.lat!!)
        }
    }
    return emptyList()
}

fun mapToCategory(categories: List<NetworkCategory>): List<VenueCategory> {
    return categories.filter {
        (it.id != null && it.name != null && it.icon != null && it.icon!!.prefix != null
                && it.icon!!.prefix != null) }
        .map {
            VenueCategory(it.id!!, it.name!!, it.icon!!.prefix + it.icon!!.suffix)
        }
}


