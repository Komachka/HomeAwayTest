package com.kstor.homeawaytest.view.detailscreen

import com.kstor.homeawaytest.R
import com.kstor.homeawaytest.domain.GenerateStaticMapUrlUseCase
import com.kstor.homeawaytest.domain.model.VenuesParcelize
import com.kstor.homeawaytest.view.BasePresentor
import io.reactivex.Scheduler

class DetailsPresenterImpl(
    private val useCase: GenerateStaticMapUrlUseCase,
    private val iOScheduler: Scheduler,
    private val mainScheduler:Scheduler

) : DetailsPresenter, BasePresentor<DetailsView>() {

    override fun setFavorite(venues: VenuesParcelize) {
        val imageFavorite = if (venues.isFavorite) R.drawable.ic_favorite_black_24dp else R.drawable.ic_favorite_border_black_24dp
        (view as DetailsView).setIfFavorite(imageFavorite)
    }

    override fun createStaticMapUrl(venues: VenuesParcelize) {
        useCase.createStaticMapUrl(venues)
            .subscribeOn(iOScheduler)
            .observeOn(mainScheduler)
            .subscribe {
                (view as DetailsView).loadMap(it)
            }
    }
}
