package com.kstor.homeawaytest.data.repos

import com.kstor.homeawaytest.data.*
import com.kstor.homeawaytest.data.sp.SharedPreferenceData
import com.kstor.homeawaytest.domain.StaticMapRepository
import com.kstor.homeawaytest.domain.model.VenuesParcelize
import io.reactivex.Observable

class StaticMapRepositoryImpl(private val preferenceData: SharedPreferenceData) : StaticMapRepository {

    override fun createStaticMapUrl(venues: VenuesParcelize): Observable<String> {
        val (latCenter, lngCenter) = preferenceData.getCityCenterInfo()
        log("create static map")
        log(latCenter.toString())
        log(lngCenter.toString())
        log("---------")
        val latPoint = venues.lat
        val lngPoint = venues.lng
        val zoom = countZoom(venues.distance)
        val url = STATIC_MAP_BASE_URL +
                "$CENTER=$latCenter,$lngCenter" +
                "&$ZOOM=$zoom" +
                "&$SIZE=$IMAGE_SIZE" +
                "&$MAPTYPE=$MAP_TYPE_TERRIAN" +
                "&$MARKERS=color:$colour1%7Clabel:C%7C$latCenter,$lngCenter" +
                "&$MARKERS=color:$colour2%7Clabel:P%7C$latPoint,$lngPoint" +
                "&$KEY=$API_KEY"
        log(url)
        return Observable.just(url)
    }
}
