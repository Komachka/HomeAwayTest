package com.kstor.homeawaytest.view.di.mock

import com.kstor.homeawaytest.view.di.AppComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestRepositoryModule::class])
interface TestComponent : AppComponent {
    fun inject(test: com.kstor.homeawaytest.ExampleInstrumentedTest)
}