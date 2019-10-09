package com.kstor.homeawaytest.fake

import com.kstor.homeawaytest.data.API_KEY
import com.kstor.homeawaytest.data.CENTER
import com.kstor.homeawaytest.data.CENTER_LAT
import com.kstor.homeawaytest.data.CENTER_LNG
import com.kstor.homeawaytest.data.IMAGE_SIZE
import com.kstor.homeawaytest.data.KEY
import com.kstor.homeawaytest.data.MAPTYPE
import com.kstor.homeawaytest.data.MAP_TYPE_TERRIAN
import com.kstor.homeawaytest.data.MARKERS
import com.kstor.homeawaytest.data.SIZE
import com.kstor.homeawaytest.data.STATIC_MAP_BASE_URL
import com.kstor.homeawaytest.data.ZOOM
import com.kstor.homeawaytest.data.colour1
import com.kstor.homeawaytest.data.colour2
import com.kstor.homeawaytest.data.countZoom
import com.kstor.homeawaytest.domain.StaticMapRepository
import com.kstor.homeawaytest.domain.model.Venue
import io.reactivex.Observable

class FakeStaticMapRepository : StaticMapRepository {

    override fun createStaticMapUrl(venues: Venue): Observable<String> {
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
