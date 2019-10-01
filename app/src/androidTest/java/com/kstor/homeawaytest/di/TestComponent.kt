package com.kstor.homeawaytest.di

import com.kstor.homeawaytest.DetailsTest
import com.kstor.homeawaytest.VenuesListTest
import com.kstor.homeawaytest.view.di.AppComponent
import com.kstor.homeawaytest.view.di.AppModule
import com.kstor.homeawaytest.view.di.PresentersModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    TestVenuesRepositoryModule::class,
    TestStaticMapRepositoryModule::class,
    AppModule::class,
    PresentersModule::class])
interface TestComponent : AppComponent {
    fun inject(test: VenuesListTest)
    fun inject(test: DetailsTest)
}
