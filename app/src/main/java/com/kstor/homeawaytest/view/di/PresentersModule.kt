package com.kstor.homeawaytest.view.di

import com.kstor.homeawaytest.domain.FavoriteUseCase
import com.kstor.homeawaytest.domain.GenerateStaticMapUrlUseCase
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.view.detailscreen.DetailsPresenter
import com.kstor.homeawaytest.view.detailscreen.DetailsPresenterImpl
import com.kstor.homeawaytest.view.mainscreen.VenuesListPresenter
import com.kstor.homeawaytest.view.mainscreen.VenuesListPresenterImpl
import com.kstor.homeawaytest.view.mapscreen.MapPresenter
import com.kstor.homeawaytest.view.mapscreen.MapPresenterImpl
import com.kstor.homeawaytest.view.utils.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
class PresentersModule {
    @Provides
    @Singleton
    fun provideDetailsPresenter(
        useCase: GenerateStaticMapUrlUseCase,
        compositeDisposable: CompositeDisposable,
        favoriteUseCase: FavoriteUseCase,
        schedulerProvider: SchedulerProvider
    ): DetailsPresenter =
        DetailsPresenterImpl(compositeDisposable, useCase, schedulerProvider, favoriteUseCase)

    @Provides
    fun provideCompositeCompositeDisposable() = CompositeDisposable()

    @Provides
    @Singleton
    fun provideVenuesListPresenter(
        useCase: VenuesUseCase,
        compositeDisposable: CompositeDisposable,
        favoriteUseCase: FavoriteUseCase,
        schedulerProvider: SchedulerProvider
    ): VenuesListPresenter {
        return VenuesListPresenterImpl(
            compositeDisposable,
            useCase,
            schedulerProvider,
            favoriteUseCase
        )
    }

    @Provides
    @Singleton
    fun provideMapPresenter(
        useCase: VenuesUseCase,
        compositeDisposable: CompositeDisposable,
        favoriteUseCase: FavoriteUseCase,
        schedulerProvider: SchedulerProvider
    ): MapPresenter {
        return MapPresenterImpl(
            compositeDisposable,
            useCase,
            schedulerProvider
        )
    }
}
