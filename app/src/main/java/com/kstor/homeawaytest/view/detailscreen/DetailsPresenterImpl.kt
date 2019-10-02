package com.kstor.homeawaytest.view.detailscreen

import com.kstor.homeawaytest.domain.FavoriteUseCase
import com.kstor.homeawaytest.domain.GenerateStaticMapUrlUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.base.FavoritesPresenter
import com.kstor.homeawaytest.view.utils.FavoriteImageRes
import com.kstor.homeawaytest.view.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class DetailsPresenterImpl @Inject constructor(
    compositeDisposable: CompositeDisposable,
    private val useCase: GenerateStaticMapUrlUseCase,
    schedulerProvider: SchedulerProvider,
    favoritesUseCase: FavoriteUseCase

) : DetailsPresenter, FavoritesPresenter<DetailsView>(favoritesUseCase, schedulerProvider, compositeDisposable) {

    override fun updateViewAfterAddOrRemoveFromFavorites(venues: Venues?, throwable: Throwable?) {
        venues?.let {
            view?.updateItemView(venues)
        }
        throwable?.let {
            view?.showError(it)
        }
    }

    /*override fun addAndRemoveFromFavorites(venues: Venues) {
        val act = if (!venues.isFavorite) {
            { favoritesUseCase.addToFavorite(venues) }
        } else {
            { favoritesUseCase.removeFromFavorite(venues) }
        }
        compositeDisposable.add(
            act.invoke()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeBy(
                    onComplete = {
                        view?.updateItemView(venues)
                    },
                    onError = {
                        view?.showError(it)
                    })
        )
    }*/

    override fun setFavorite(venues: Venues) {
        val imageFavorite =
            if (venues.isFavorite) FavoriteImageRes.IS_FAVORITE.resId else FavoriteImageRes.IS_NOT_FAVORITE.resId
        (view as DetailsView).setIfFavorite(imageFavorite)
    }

    override fun createStaticMapUrl(venues: Venues) {
        compositeDisposable.add(useCase.createStaticMapUrl(venues)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribeBy(
                onError = { view?.showError(it) },
                onComplete = {},
                onNext = {
                    (view as DetailsView).loadMap(it)
                }
            ))
    }
}
