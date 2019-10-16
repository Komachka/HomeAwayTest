package com.kstor.homeawaytest.data

import android.util.Log
import com.kstor.homeawaytest.data.db.model.DBFavoriteModel
import com.kstor.homeawaytest.data.db.model.DBVenuesModel
import com.kstor.homeawaytest.data.network.model.BestPhoto
import com.kstor.homeawaytest.data.network.model.NetworkCategory
import com.kstor.homeawaytest.data.network.model.NetworkDetailsModel
import com.kstor.homeawaytest.data.network.model.NetworkVenue
import com.kstor.homeawaytest.data.network.model.NetworkVenuesModel
import com.kstor.homeawaytest.data.network.model.Timeframe
import com.kstor.homeawaytest.domain.model.HoursPerDay
import com.kstor.homeawaytest.domain.model.Venue
import com.kstor.homeawaytest.domain.model.VenueDetails
import com.kstor.homeawaytest.domain.model.VenuesCategory
import com.kstor.homeawaytest.domain.model.VenuesData
import java.lang.Exception
import java.lang.Math.atan2
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.Math.sqrt
import kotlin.math.roundToInt

fun NetworkVenuesModel.mapToVenuesData(): VenuesData {
    val centerLat = response?.geocode?.feature?.geometry?.center?.lat ?: CENTER_LAT
    val centerLng = response?.geocode?.feature?.geometry?.center?.lng ?: CENTER_LNG
    return VenuesData(
        createListOfCategories(
            this.response?.venues,
            centerLat,
            centerLng
        ), centerLat, centerLng
    )
}

fun mapToListOfVenues(list: List<DBVenuesModel>): List<Venue> {
    return list.map {
        Venue(
            it.id,
            it.name,
            VenuesCategory(it.categoryId, it.categoryName, it.iconPath),
            it.address,
            it.distance,
            it.lat, it.lng, it.isFavorite
        )
    }
}

fun mapToDBVenuesModel(venues: Venue): DBVenuesModel? {
        return try {
            DBVenuesModel(
                requireNotNull(venues.id),
                requireNotNull(venues.name),
                venues.categories?.id ?: "",
                venues.categories?.name ?: "",
                venues.categories?.iconPath ?: "",
                venues.address ?: "",
                requireNotNull(venues.distance),
                requireNotNull(venues.lat),
                requireNotNull(venues.lng),
                requireNotNull(venues.isFavorite))
        } catch (e: Exception) { null }
}

private fun createListOfCategories(venues: List<NetworkVenue>?, centerLat: Double, centerLng: Double): List<Venue> {
    return venues?.let {
        it.map {
            Venue().apply {
                id = it.id
                name = it.name
                categories = mapToCategory(it.categories)
                address = it.location?.address
                distance = calcDistance(it.location?.lat, it.location?.lng, centerLat, centerLng)
                lat = it.location?.lat ?: 0.0
                lng = it.location?.lng ?: 0.0
            }
        }
    } ?: emptyList()
}

fun calcDistance(lat: Double?, lng: Double?, centerLat: Double, centerLng: Double): Int {
    var d = 0.0
    lat?.let { lat ->
        lng?.let { lng ->
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

private fun mapToCategory(categories: List<NetworkCategory>?): VenuesCategory? {
    return categories?.let {
        it.first().let { category ->
            VenuesCategory(
                category.id,
                category.name,
                category.icon?.prefix + SIZE_32 + category.icon?.suffix
            )
        }
    }
}

fun mapToDBFavoriteModel(venues: DBVenuesModel) =
    DBFavoriteModel(venues.id, venues.name, venues.categoryId, venues.categoryName, venues.iconPath, venues.address, venues.distance, venues.lat, venues.lng)

fun log(message: String) {
    Log.d("MainActivity", message)
}

fun logError(message: String) {
    Log.e("MainActivity", message)
}

fun mapToVenueDetails(networkDetailsModel: NetworkDetailsModel): VenueDetails {
    return VenueDetails(
        networkDetailsModel.response?.venue?.id,
        networkDetailsModel.response?.venue?.name,
        networkDetailsModel.response?.venue?.contact?.formattedPhone,
        networkDetailsModel.response?.venue?.canonicalUrl,
        networkDetailsModel.response?.venue?.url,
        networkDetailsModel.response?.venue?.rating,
        networkDetailsModel.response?.venue?.ratingColor,
        networkDetailsModel.response?.venue?.description,
        networkDetailsModel.response?.venue?.shortUrl,
        networkDetailsModel.response?.venue?.hours?.isOpen,
        mapToListOfWorkingHours(networkDetailsModel.response?.venue?.hours?.timeframes),
        createBestPhotoUrl(networkDetailsModel.response?.venue?.bestPhoto)

    )
}

fun createBestPhotoUrl(bestPhoto: BestPhoto?): String? {
    return bestPhoto?.prefix + SIZE_400 + bestPhoto?.suffix
}

fun mapToListOfWorkingHours(timeframes: List<Timeframe>?): List<HoursPerDay>? {
    return timeframes?.let {
            it.map { timeframe ->
                HoursPerDay(timeframe.days, timeframe.open?.first()?.renderedTime)
            }
        }
}
