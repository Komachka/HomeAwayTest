package com.kstor.homeawaytest.view.di

import com.kstor.homeawaytest.view.mainscreen.VenuesListFragment
import com.kstor.homeawaytest.view.mainscreen.VenuesListView
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModule {
    @Binds
    abstract fun provideVenuesListView(fragment:VenuesListFragment) : VenuesListView
}