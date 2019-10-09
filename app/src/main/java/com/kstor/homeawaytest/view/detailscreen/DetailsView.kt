package com.kstor.homeawaytest.view.detailscreen

import com.kstor.homeawaytest.domain.model.VenueDetails
import com.kstor.homeawaytest.view.base.ViewWithFavorites

interface DetailsView : ViewWithFavorites {
    fun loadMap(url: String?)
    fun setFavoriteDrawableLevel(level:Int)
    fun updateInfo(it: VenueDetails)
}
