package com.kstor.homeawaytest.view.detailscreen

import com.kstor.homeawaytest.view.base.ViewWithFavorites

interface DetailsView : ViewWithFavorites {
    fun loadMap(url: String?)
    fun setIfFavorite(resFavorites: Int)
}
