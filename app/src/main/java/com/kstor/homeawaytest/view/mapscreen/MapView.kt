package com.kstor.homeawaytest.view.mapscreen

import com.google.android.gms.maps.model.LatLng
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.domain.model.VenusData
import com.kstor.homeawaytest.view.BaseView

interface MapView : BaseView {
    fun showVenuesOnTheMap(venues: Map<LatLng, Venues>)
    fun showCenterOnTheMap(venusData: VenusData)
}
