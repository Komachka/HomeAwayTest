package com.kstor.homeawaytest.data.repos

import com.kstor.homeawaytest.data.API_KEY
import com.kstor.homeawaytest.data.CENTER
import com.kstor.homeawaytest.data.IMAGE_SIZE
import com.kstor.homeawaytest.data.KEY
import com.kstor.homeawaytest.data.MAPTYPE
import com.kstor.homeawaytest.data.MAP_TYPE_TERRIAN
import com.kstor.homeawaytest.data.MARKERS
import com.kstor.homeawaytest.data.SIZE
import com.kstor.homeawaytest.data.STATIC_MAP_BASE_URL
import com.kstor.homeawaytest.data.colour1
import com.kstor.homeawaytest.data.colour2
import com.kstor.homeawaytest.data.sp.SharedPreferenceData
import com.kstor.homeawaytest.domain.StaticMapRepository
import com.kstor.homeawaytest.domain.model.Venue
import io.reactivex.Observable

class StaticMapRepositoryImpl(private val preferenceData: SharedPreferenceData) : StaticMapRepository {

    override fun createStaticMapUrl(venues: Venue): Observable<String> {
        val (latCenter, lngCenter) = preferenceData.getCityCenterInfo()
        val latPoint = venues.lat
        val lngPoint = venues.lng
        val url = STATIC_MAP_BASE_URL +
                "$CENTER=$latCenter,$lngCenter" +
                "&$SIZE=$IMAGE_SIZE" +
                "&$MAPTYPE=$MAP_TYPE_TERRIAN" +
                "&$MARKERS=size:mid%7Ccolor:$colour1%7Clabel:C%7C$latCenter,$lngCenter" +
                "&$MARKERS=size:mid%7Ccolor:$colour2%7C$latPoint,$lngPoint" +
                "&$KEY=$API_KEY"
        return Observable.just(url)
    }
}
