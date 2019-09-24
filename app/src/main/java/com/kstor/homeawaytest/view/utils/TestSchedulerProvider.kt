package com.kstor.homeawaytest.view.utils

import io.reactivex.Scheduler

class TestSchedulerProvider(private val testScheduler: Scheduler) : SchedulerProvider {
    override fun ui(): Scheduler = testScheduler
    override fun io(): Scheduler = testScheduler
}
