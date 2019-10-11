package com.kstor.homeawaytest.view.base

import com.kstor.homeawaytest.domain.FavoriteUseCase
import com.kstor.homeawaytest.domain.RepoResult
import com.kstor.homeawaytest.domain.model.Venue
import com.kstor.homeawaytest.view.utils.DispatcherProvider
import kotlinx.coroutines.withContext

interface AddAndRemoveFavoritesManager {

    suspend fun <T : ViewWithFavorites> BasePresenter<T>.addAndRemoveFromFavorites(
        venues: Venue,
        favoritesUseCase: FavoriteUseCase,
        dispatchesProvider: DispatcherProvider
    ) {
        var result =
        if (venues.isFavorite) {
            favoritesUseCase.addToFavorite(venues)
        } else {
            favoritesUseCase.removeFromFavorite(venues)
        }
        withContext(dispatchesProvider.ui()) {
            handleRepoResult(result,
                success = {
                    view?.updateItemView(venues = venues)
                }, fail = {
                    view?.showError((result as RepoResult.Error<*>).throwable)
                })
        }
    }
}
