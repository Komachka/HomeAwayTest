package com.kstor.homeawaytest.view.base

import com.kstor.homeawaytest.domain.model.Venue

interface ViewWithFavorites : BaseView {
    fun updateItemView(venues: Venue)
}
