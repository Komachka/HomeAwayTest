package com.kstor.homeawaytest.view.mapscreen

import com.google.android.gms.maps.model.LatLng
import com.kstor.homeawaytest.domain.model.Venue
import com.kstor.homeawaytest.view.base.BaseView

interface MapView : BaseView {
    fun showVenuesOnTheMap(venues: Map<LatLng, Venue>)
    fun showCenterOnTheMap(centerData: LatLng)
}
