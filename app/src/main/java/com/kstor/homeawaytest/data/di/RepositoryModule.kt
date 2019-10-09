package com.kstor.homeawaytest.data.di

import com.kstor.homeawaytest.data.db.LocalData
import com.kstor.homeawaytest.data.network.RemoteData
import com.kstor.homeawaytest.data.repos.StaticMapRepositoryImpl
import com.kstor.homeawaytest.data.repos.VenueDetailsRepositoryImpl
import com.kstor.homeawaytest.data.repos.VenuesRepositoryImp
import com.kstor.homeawaytest.data.sp.SharedPreferenceData
import com.kstor.homeawaytest.domain.StaticMapRepository
import com.kstor.homeawaytest.domain.VenueDetailsRepository
import com.kstor.homeawaytest.domain.VenuesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideVenusRepository(remoteData: RemoteData, seredPrefData: SharedPreferenceData, localData: LocalData): VenuesRepository =
        VenuesRepositoryImp(remoteData, seredPrefData, localData)

    @Provides
    @Singleton
    fun provideStaticMapRepository(seredPrefData: SharedPreferenceData): StaticMapRepository =
            StaticMapRepositoryImpl(seredPrefData)

    @Provides
    @Singleton
    fun provideVenuDetailsRepository(remoteData: RemoteData): VenueDetailsRepository =
        VenueDetailsRepositoryImpl(remoteData)
}
