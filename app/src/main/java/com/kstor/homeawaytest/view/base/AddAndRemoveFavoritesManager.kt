package com.kstor.homeawaytest.view.base

import com.kstor.homeawaytest.data.log
import com.kstor.homeawaytest.domain.FavoriteUseCase
import com.kstor.homeawaytest.domain.RepoResult
import com.kstor.homeawaytest.domain.model.Venue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface AddAndRemoveFavoritesManager {

    suspend fun <T : ViewWithFavorites> BasePresenter<T>.addAndRemoveFromFavorites(venues: Venue, favoritesUseCase: FavoriteUseCase) {
        var result =
        if (!venues.isFavorite) {
            log("Add to favorite")
            favoritesUseCase.addToFavorite(venues)
        } else {
            log("remove from favorite")
            favoritesUseCase.removeFromFavorite(venues)
        }
        withContext(Dispatchers.Main) {
            when (result) {
                is RepoResult.Success -> {
                    view?.updateItemView(venues = venues)
                }
                is RepoResult.Error<*> -> {
                    view?.showError(result.throwable)
                }
            }
        }
    }
}
