package com.kstor.homeawaytest.view.utils

import io.reactivex.Scheduler
import kotlinx.coroutines.Dispatchers

class TestSchedulerProvider(private val testScheduler: Scheduler) : DispatcherProvider {
    override fun ui() = Dispatchers.Main
    override fun io() =  Dispatchers.Unconfined
}
