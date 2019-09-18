package com.kstor.homeawaytest

import com.kstor.homeawaytest.data.di.NetworkModule
import com.kstor.homeawaytest.data.di.RepositoryModule
import com.kstor.homeawaytest.view.AppModule
import com.kstor.homeawaytest.view.detailscreen.DetailFragment
import com.kstor.homeawaytest.view.di.ViewModelModule
import com.kstor.homeawaytest.view.mainscreen.VenuesListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, NetworkModule::class, AppModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(target: MainActivity)
    fun inject(target: VenuesListFragment)
    fun inject(target: DetailFragment)
}
