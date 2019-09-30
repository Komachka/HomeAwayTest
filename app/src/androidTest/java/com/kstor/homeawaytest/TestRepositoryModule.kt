package com.kstor.homeawaytest

import com.kstor.homeawaytest.data.db.LocalData
import com.kstor.homeawaytest.data.network.RemoteData
import com.kstor.homeawaytest.data.repos.StaticMapRepositoryImpl
import com.kstor.homeawaytest.data.repos.VenuesRepositoryImp
import com.kstor.homeawaytest.data.sp.SharedPreferenceData
import com.kstor.homeawaytest.domain.StaticMapRepository
import com.kstor.homeawaytest.domain.VenuesRepository
import com.kstor.homeawaytest.view.di.mock.FakeRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestRepositoryModule(private val testRepository: VenuesRepository) {

    @Provides
    @Singleton
    fun provideVenusRepository(): VenuesRepository = testRepository


    @Provides
    @Singleton
    fun provideStaticMapRepository(seredPrefData: SharedPreferenceData): StaticMapRepository =
        StaticMapRepositoryImpl(seredPrefData)


}
