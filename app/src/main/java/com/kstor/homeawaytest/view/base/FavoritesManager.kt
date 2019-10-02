package com.kstor.homeawaytest.view.base

import com.kstor.homeawaytest.domain.FavoriteUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
interface FavoritesManager{

    fun <T:ViewWithFavorites>BasePresenter<T>.addAndRemoveFromFavorites(venues: Venues, favoritesUseCase: FavoriteUseCase)
    {
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
                        view?.updateItemView(venues = venues)
                    },
                    onError = {
                        view?.showError(it)
                    })
        )
    }
}
