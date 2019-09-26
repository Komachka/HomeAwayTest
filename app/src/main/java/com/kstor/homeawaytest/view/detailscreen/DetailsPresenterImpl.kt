package com.kstor.homeawaytest.view.detailscreen

import com.kstor.homeawaytest.R
import com.kstor.homeawaytest.domain.FavoriteUseCase
import com.kstor.homeawaytest.domain.GenerateStaticMapUrlUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.BasePresenter
import com.kstor.homeawaytest.view.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class DetailsPresenterImpl(
    compositeDisposable: CompositeDisposable,
    private val useCase: GenerateStaticMapUrlUseCase,
    private val schedulerProvider: SchedulerProvider,
    private val favoritesUseCase: FavoriteUseCase

) : DetailsPresenter, BasePresenter<DetailsView>(compositeDisposable) {

    override fun addAndRemoveFromFavorites(venues: Venues) {
        if (!venues.isFavorite) {
            compositeDisposable.add(favoritesUseCase.addToFavorite(venues)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeBy(
                    onComplete = {
                        view?.updateItemView(venues)
                    },
                    onError = {}))
        } else {
            compositeDisposable.add(favoritesUseCase.removeFromFavorite(venues)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeBy(
                    onComplete = {
                        view?.updateItemView(venues)
                    },
                    onError = {}))
        }
    }

    override fun setFavorite(venues: Venues) {
        val imageFavorite = if (venues.isFavorite) R.drawable.ic_favorite_black_24dp else R.drawable.ic_favorite_border_black_24dp
        (view as DetailsView).setIfFavorite(imageFavorite)
    }

    override fun createStaticMapUrl(venues: Venues) {
        compositeDisposable.add(useCase.createStaticMapUrl(venues)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe {
                (view as DetailsView).loadMap(it)
            })
    }
}
