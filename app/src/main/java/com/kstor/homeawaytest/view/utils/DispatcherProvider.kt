package com.kstor.homeawaytest.view.utils


import kotlin.coroutines.CoroutineContext

interface DispatcherProvider {
    fun ui(): CoroutineContext
    fun io(): CoroutineContext
}
