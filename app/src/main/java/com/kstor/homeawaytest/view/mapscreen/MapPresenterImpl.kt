package com.kstor.homeawaytest.view.mapscreen

import android.view.View
import com.kstor.homeawaytest.domain.GenerateStaticMapUrlUseCase
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.domain.model.VenuesParcelize
import com.kstor.homeawaytest.view.BasePresentor
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.subscribeBy

class MapPresenterImpl(private val venuesListUseCase: VenuesUseCase,
                       private val iOScheduler: Scheduler,
                       private val mainScheduler: Scheduler
) : MapPresenter, BasePresentor<MapView>() {

    override fun getVenues(query: String) {
        venuesListUseCase.loadVenuesData(query)
            .subscribeOn(iOScheduler)
            .observeOn(mainScheduler)
            .doOnNext {
                view?.showCenterOnTheMap(it)
            }
            .map { it.venues }
            .subscribeBy {
                view?.showVenuesOnTheMap(it)
        }
    }

    override fun navigateToDetailsScreen(view: View, venuesParcelize: VenuesParcelize) {

    }
}