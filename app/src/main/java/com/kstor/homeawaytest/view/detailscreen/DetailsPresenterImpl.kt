package com.kstor.homeawaytest.view.detailscreen

import com.kstor.homeawaytest.R
import com.kstor.homeawaytest.data.log
import com.kstor.homeawaytest.domain.FavoriteUseCase
import com.kstor.homeawaytest.domain.GenerateStaticMapUrlUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.domain.model.VenuesParcelize
import com.kstor.homeawaytest.view.BasePresentor
import com.kstor.homeawaytest.view.VenuesMapper
import com.kstor.homeawaytest.view.utils.SchedulerProvider
import io.reactivex.rxkotlin.subscribeBy

class DetailsPresenterImpl(
    private val useCase: GenerateStaticMapUrlUseCase,
    private val schedulerProvider: SchedulerProvider,
    private val favoritesUseCase: FavoriteUseCase

) : DetailsPresenter, BasePresentor<DetailsView>() {

    override fun addAndRemoveFromFavorites(venues: Venues) {
        if (!venues.isFavorite) {
            favoritesUseCase.addToFavorite(venues)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeBy(
                    onComplete = {
                        view?.updateItemView(venues)
                    },
                    onError = {})
        } else {
            log("is already favorite")
            favoritesUseCase.removeFromFavorite(venues)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeBy(
                    onComplete = {
                        view?.updateItemView(venues)
                    },
                    onError = {})
        }
    }

    override fun setFavorite(venues: Venues) {
        val imageFavorite = if (venues.isFavorite) R.drawable.ic_favorite_black_24dp else R.drawable.ic_favorite_border_black_24dp
        (view as DetailsView).setIfFavorite(imageFavorite)
    }

    override fun createStaticMapUrl(venues: Venues) {
        useCase.createStaticMapUrl(venues)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe {
                (view as DetailsView).loadMap(it)
            }
    }


}
