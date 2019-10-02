package com.kstor.homeawaytest.view.base

import com.kstor.homeawaytest.domain.FavoriteUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.utils.TestSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

abstract class FavoritesPresenter<T>(
    private val favoritesUseCase: FavoriteUseCase,
    schedulerProvider: TestSchedulerProvider,
    compositeDisposable: CompositeDisposable) : BasePresenter<T>(compositeDisposable, schedulerProvider)
{
    fun addAndRemoveFromFavorites(venues: Venues) { // TODO code duplicate
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
                        //view?.updateItemView(venues)
                        updateView(venues = venues)
                    },
                    onError = {
                        updateView(throwable = it)
                        //view?.showError(it)
                    })
        )
    }

    abstract fun updateView(venues: Venues? = null, throwable: Throwable? = null)
}