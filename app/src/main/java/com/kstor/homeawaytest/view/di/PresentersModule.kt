package com.kstor.homeawaytest.view.di

import com.kstor.homeawaytest.domain.FavoriteUseCase
import com.kstor.homeawaytest.domain.GenerateStaticMapUrlUseCase
import com.kstor.homeawaytest.domain.VenueDetailsUseCase
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.view.detailscreen.DetailsPresenter
import com.kstor.homeawaytest.view.detailscreen.DetailsPresenterImpl
import com.kstor.homeawaytest.view.mainscreen.VenuesListPresenter
import com.kstor.homeawaytest.view.mainscreen.VenuesListPresenterImpl
import com.kstor.homeawaytest.view.mapscreen.MapPresenter
import com.kstor.homeawaytest.view.mapscreen.MapPresenterImpl
import com.kstor.homeawaytest.view.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class PresentersModule {
    @Provides
    fun provideDetailsPresenter(
        useCase: GenerateStaticMapUrlUseCase,
        favoriteUseCase: FavoriteUseCase,
        dispatcherProvider: DispatcherProvider,
        detailsUseCase: VenueDetailsUseCase
    ): DetailsPresenter =
        DetailsPresenterImpl(dispatcherProvider, useCase, favoriteUseCase, detailsUseCase)

    @Provides
    fun provideCompositeCompositeDisposable() = CompositeDisposable()

    @Provides
    fun provideVenuesListPresenter(
        useCase: VenuesUseCase,
        favoriteUseCase: FavoriteUseCase,
        schedulerProvider: DispatcherProvider
    ): VenuesListPresenter {
        return VenuesListPresenterImpl(
            useCase,
            schedulerProvider,
            favoriteUseCase
        )
    }

    @Provides
    fun provideMapPresenter(
        useCase: VenuesUseCase,
        schedulerProvider: DispatcherProvider
    ): MapPresenter {
        return MapPresenterImpl(
            useCase,
            schedulerProvider
        )
    }
}
