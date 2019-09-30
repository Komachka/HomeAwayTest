package com.kstor.homeawaytest

import com.kstor.homeawaytest.data.db.LocalData
import com.kstor.homeawaytest.data.network.RemoteData
import com.kstor.homeawaytest.data.repos.StaticMapRepositoryImpl
import com.kstor.homeawaytest.data.repos.VenuesRepositoryImp
import com.kstor.homeawaytest.data.sp.SharedPreferenceData
import com.kstor.homeawaytest.domain.StaticMapRepository
import com.kstor.homeawaytest.domain.VenuesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestRepositoryModule(val repository:VenuesRepository) {

    @Provides
    @Singleton
    fun provideVenusRepository(): VenuesRepository = repository as FakeRepository


}
