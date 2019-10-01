package com.kstor.homeawaytest.fake

import com.kstor.homeawaytest.data.*
import com.kstor.homeawaytest.domain.StaticMapRepository
import com.kstor.homeawaytest.domain.model.Venues
import io.reactivex.Observable

class FakeStaticMapRepository : StaticMapRepository {

    override fun createStaticMapUrl(venues: Venues): Observable<String> {
        val (latCenter, lngCenter) = CENTER_LAT to CENTER_LNG
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
        return Observable.just(url)
    }
}
