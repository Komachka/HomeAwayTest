package com.kstor.homeawaytest.data.repos

import com.kstor.homeawaytest.data.*
import com.kstor.homeawaytest.data.sp.SharedPreferenceData
import com.kstor.homeawaytest.domain.StaticMapRepository
import com.kstor.homeawaytest.domain.model.Venue
import io.reactivex.Observable

class StaticMapRepositoryImpl(private val preferenceData: SharedPreferenceData) : StaticMapRepository {

    override suspend fun createStaticMapUrl(venues: Venue): String {
        val (latCenter, lngCenter) = preferenceData.getCityCenterInfo()
        val latPoint = venues.lat
        val lngPoint = venues.lng
        return STATIC_MAP_BASE_URL +
                "$CENTER=$latCenter,$lngCenter" +
                "&$SIZE=$IMAGE_SIZE" +
                "&$MAPTYPE=$MAP_TYPE_TERRIAN" +
                "&$MARKERS=size:mid%7Ccolor:$colour1%7Clabel:C%7C$latCenter,$lngCenter" +
                "&$MARKERS=size:mid%7Ccolor:$colour2%7C$latPoint,$lngPoint" +
                "&$KEY=$API_KEY"

    }
}
