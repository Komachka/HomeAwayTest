package com.kstor.homeawaytest.view.detailscreen

import com.kstor.homeawaytest.R
import com.kstor.homeawaytest.domain.GenerateStaticMapUrlUseCase
import com.kstor.homeawaytest.domain.model.VenuesParcelize
import com.kstor.homeawaytest.view.BasePresenter
import com.kstor.homeawaytest.view.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class DetailsPresenterImpl(compositeDisposable: CompositeDisposable,
    private val useCase: GenerateStaticMapUrlUseCase,
    private val schedulerProvider: SchedulerProvider

) : DetailsPresenter, BasePresenter<DetailsView>(compositeDisposable) {

    override fun setFavorite(venues: VenuesParcelize) {
        val imageFavorite = if (venues.isFavorite) R.drawable.ic_favorite_black_24dp else R.drawable.ic_favorite_border_black_24dp
        (view as DetailsView).setIfFavorite(imageFavorite)
    }

    override fun createStaticMapUrl(venues: VenuesParcelize) {
        compositeDisposable.add(useCase.createStaticMapUrl(venues)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe {
                (view as DetailsView).loadMap(it)
            })
    }
}
