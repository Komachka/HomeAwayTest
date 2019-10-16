package com.kstor.homeawaytest.domain.di

import com.kstor.homeawaytest.domain.FavoriteUseCase
import com.kstor.homeawaytest.domain.GenerateStaticMapUrlUseCase
import com.kstor.homeawaytest.domain.StaticMapRepository
import com.kstor.homeawaytest.domain.VenueDetailsRepository
import com.kstor.homeawaytest.domain.VenueDetailsUseCase
import com.kstor.homeawaytest.domain.VenuesRepository
import com.kstor.homeawaytest.domain.VenuesUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCaseModule {

    @Provides
    @Singleton
    fun provideGenerateStaticMapUrlUseCase(repository: StaticMapRepository) =
        GenerateStaticMapUrlUseCase(repository)

    @Provides
    @Singleton
    fun provideLoadVenuesUseCase(repository: VenuesRepository) =
        VenuesUseCase(repository)

    @Provides
    @Singleton
    fun provideFavoritesUseCase(repository: VenuesRepository) =
        FavoriteUseCase(repository)

    @Provides
    @Singleton
    fun provideVenueDetailsUseCase(repository: VenueDetailsRepository) =
        VenueDetailsUseCase(repository)
}
