package com.kstor.homeawaytest.view.utils

import kotlinx.coroutines.Dispatchers

class AppDispatcherProvider : DispatcherProvider {
    override fun ui()  = Dispatchers.Main
    override fun io() =  Dispatchers.IO
}
