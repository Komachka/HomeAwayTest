package com.kstor.homeawaytest.view.base

import com.kstor.homeawaytest.domain.model.Venues

interface BaseView {
    fun showError(error: Throwable?)
}
