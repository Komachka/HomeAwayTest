package com.kstor.homeawaytest.view.base

import com.kstor.homeawaytest.domain.model.Venues

interface ViewWithFavorites : BaseView {
    fun updateItemView(venues: Venues)
}