package com.kstor.homeawaytest.data

import android.util.Log
import com.kstor.homeawaytest.data.network.model.NetworkCategory
import com.kstor.homeawaytest.data.network.model.NetworkVenue
import com.kstor.homeawaytest.data.network.model.NetworkVenuesModel
import kotlin.math.*

fun NetworkVenuesModel.mapToVenuesData(): VenusData {
    val centerLat = response?.geocode?.feature?.geometry?.center?.lat ?: CENTER_LAT
    val centerLng = response?.geocode?.feature?.geometry?.center?.lng ?: CENTER_LNG
    return VenusData(createListOfCategories(this.response?.venues, centerLat, centerLng), centerLat, centerLng)
}

private fun createListOfCategories(venues: List<NetworkVenue>?, centerLat: Double, centerLng: Double): List<Venue> {
    return venues?.let {
        it.map {
            Venue().apply {
                id = it.id
                name = it.name
                categories = mapToCategory(it.categories!!)
                address = it.location?.address
                distance = calcDistance(it.location?.lat, it.location?.lng, centerLat, centerLng)
                lat = it.location?.lat ?: 0.0
                lng = it.location?.lng ?: 0.0
            }
        }
    }?: emptyList<Venue>()
}

fun calcDistance(lat: Double?, lng: Double?, centerLat: Double, centerLng: Double): Int {
    var d = 0.0
    lat?.let {lat ->
        lng?.let {lng ->
            val φ1 = lat.toRadians()
            val φ2 = centerLat.toRadians()
            val Δφ = (centerLat - lat).toRadians()

            val Δλ = (centerLng - lng).toRadians()
            val a = sin(Δφ / 2) * sin(Δφ / 2) +
                    cos(φ1) * cos(φ2) *
                    sin(Δλ / 2) * sin(Δλ / 2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            d = RADIUS * c
        }
    }

    return d.roundToInt()
}

private fun Double.toRadians(): Double {
    return this * Math.PI / HALF_OF_CIRCLE_DEGREE
}

private fun mapToCategory(categories: List<NetworkCategory>): List<VenueCategory> {
    return categories
        .map {
            VenueCategory(it.id, it.name, it.icon?.prefix + SIZE_32 + it.icon?.suffix)
        }
}

fun log(message: String) {
    Log.d("MainActivity", message)
}
