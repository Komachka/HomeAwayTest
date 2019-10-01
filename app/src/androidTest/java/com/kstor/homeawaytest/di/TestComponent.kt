package com.kstor.homeawaytest.di

import com.kstor.homeawaytest.DetailsTest
import com.kstor.homeawaytest.VenuesListTest
import com.kstor.homeawaytest.data.di.DbModule
import com.kstor.homeawaytest.data.di.NetworkModule
import com.kstor.homeawaytest.data.di.SharedPrefModule
import com.kstor.homeawaytest.view.di.AppComponent
import com.kstor.homeawaytest.view.di.AppModule
import com.kstor.homeawaytest.view.di.PresentersModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestVenuesRepositoryModule::class, TestStaticMapRepositoryModule::class,
    NetworkModule::class,
    AppModule::class,
    SharedPrefModule::class,
    DbModule::class,
    PresentersModule::class])
interface TestComponent : AppComponent{
    fun inject(test: VenuesListTest)
    fun inject(test: DetailsTest)
}