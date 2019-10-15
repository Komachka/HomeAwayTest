package com.kstor.homeawaytest.view.di

import android.app.Application
import android.content.Context
import com.kstor.homeawaytest.view.utils.AppDispatcherProvider
import com.kstor.homeawaytest.view.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {
    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    fun provideSchedulerProvider(): DispatcherProvider = AppDispatcherProvider()
}
