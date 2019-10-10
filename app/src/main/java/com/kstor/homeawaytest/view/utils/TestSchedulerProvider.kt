package com.kstor.homeawaytest.view.utils


import kotlinx.coroutines.Dispatchers

class TestSchedulerProvider : DispatcherProvider {
    override fun ui() = Dispatchers.Unconfined
    override fun io() = Dispatchers.Unconfined
}
