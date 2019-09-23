package com.kstor.homeawaytest.view.mapscreen

import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.domain.model.VenusData
import com.kstor.homeawaytest.view.BaseView

interface MapView:BaseView {
    fun showVenuesOnTheMap(venues:List<Venues>)
    fun showCenterOnTheMap(venusData: VenusData)
}