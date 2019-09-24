package com.kstor.homeawaytest.view.utils

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun ui() : Scheduler
    fun io() : Scheduler
}