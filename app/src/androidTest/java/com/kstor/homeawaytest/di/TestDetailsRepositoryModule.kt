package com.kstor.homeawaytest.di

import com.kstor.homeawaytest.domain.VenueDetailsRepository
import com.kstor.homeawaytest.domain.VenuesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestDetailsRepositoryModule(val testRepository:VenueDetailsRepository) {

    @Provides
    @Singleton
    fun provideVenusRepository(): VenueDetailsRepository = testRepository
}