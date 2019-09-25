package com.kstor.homeawaytest.view.detailscreen

import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.BaseView

interface DetailsView : BaseView {
    fun loadMap(url: String?)
    fun setIfFavorite(resFavorites: Int)
    fun updateItemView(venues: Venues)
}
