package com.kstor.homeawaytest.view

import android.app.Application
import android.content.Context
import com.kstor.homeawaytest.view.utils.AppSchedulerProvider
import com.kstor.homeawaytest.view.utils.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {
    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()
}
