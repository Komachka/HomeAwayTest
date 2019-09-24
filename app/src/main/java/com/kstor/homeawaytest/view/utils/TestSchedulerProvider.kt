package com.kstor.homeawaytest.view.utils

import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler

class TestSchedulerProvider(private val testScheduler: TestScheduler) : SchedulerProvider {
    override fun ui(): Scheduler  = testScheduler
    override fun io(): Scheduler = testScheduler
}