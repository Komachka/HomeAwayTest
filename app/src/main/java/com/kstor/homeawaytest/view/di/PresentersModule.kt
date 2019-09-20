package com.kstor.homeawaytest.view.di

import com.kstor.homeawaytest.domain.GenerateStaticMapUrlUseCase
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.view.detailscreen.DetailsPresenter
import com.kstor.homeawaytest.view.detailscreen.DetailsPresenterImpl
import com.kstor.homeawaytest.view.mainscreen.VenuesListPresenter
import com.kstor.homeawaytest.view.mainscreen.VenuesListPresenterImpl
import com.kstor.homeawaytest.view.mainscreen.VenuesListView
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PresentersModule {
    @Provides
    @Singleton
    fun provideDetailsPresenter(useCase: GenerateStaticMapUrlUseCase): DetailsPresenter =
        DetailsPresenterImpl(useCase)

    @Provides
    @Singleton
    fun provideVenuesListPresenter(useCase: VenuesUseCase, view: VenuesListView): VenuesListPresenterImpl =
        VenuesListPresenterImpl(useCase, view)
}
