package com.kstor.homeawaytest.view.base

import com.kstor.homeawaytest.domain.FavoriteUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

abstract class FavoritesPresenter<T>(
    private val favoritesUseCase: FavoriteUseCase,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
) : BasePresenter<T>(compositeDisposable, schedulerProvider) {
    fun addAndRemoveFromFavorites(venues: Venues) {
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
                        updateViewAfterAddOrRemoveFromFavorites(venues = venues)
                    },
                    onError = {
                        updateViewAfterAddOrRemoveFromFavorites(throwable = it)
                    })
        )
    }

    abstract fun updateViewAfterAddOrRemoveFromFavorites(venues: Venues? = null, throwable: Throwable? = null)
}
