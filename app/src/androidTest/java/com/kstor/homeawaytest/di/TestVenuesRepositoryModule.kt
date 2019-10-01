package com.kstor.homeawaytest.di

import com.kstor.homeawaytest.domain.VenuesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestVenuesRepositoryModule(private val testRepository: VenuesRepository) {

    @Provides
    @Singleton
    fun provideVenusRepository(): VenuesRepository = testRepository


}
