package com.kstor.homeawaytest.view.detailscreen

import com.kstor.homeawaytest.view.BaseView

interface DetailsView : BaseView {
    fun loadMap(url: String?)
    fun setIfFavorite(resFavorites: Int)
}
