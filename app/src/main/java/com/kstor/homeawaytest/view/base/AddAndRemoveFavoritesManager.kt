package com.kstor.homeawaytest.view.base

import com.kstor.homeawaytest.domain.FavoriteUseCase
import com.kstor.homeawaytest.domain.model.Venue
import io.reactivex.rxkotlin.subscribeBy
interface AddAndRemoveFavoritesManager {

    fun <T : ViewWithFavorites> BasePresenter<T>.addAndRemoveFromFavorites(venues: Venue, favoritesUseCase: FavoriteUseCase) {
        val act = if (!venues.isFavorite) {
            { favoritesUseCase.addToFavorite(venues) }
        } else {
            {
                favoritesUseCase.removeFromFavorite(venues) }
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
