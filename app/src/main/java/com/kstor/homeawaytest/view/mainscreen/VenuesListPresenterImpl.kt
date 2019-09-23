package com.kstor.homeawaytest.view.mainscreen

import com.kstor.homeawaytest.data.log
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.view.BasePresentor
import io.reactivex.Scheduler

class VenuesListPresenterImpl(
    private val useCase: VenuesUseCase,
    private val iOScheduler: Scheduler,
    private val mainScheduler: Scheduler
) :
    VenuesListPresenter, BasePresentor<VenuesListView>() {

    override fun getVenues(query: String) {
        useCase.loadVenuesData(query).subscribeOn(iOScheduler)
            .map {
                it.venues
            }
            .doOnError {
                log(it.toString())
            }
            .observeOn(mainScheduler)
            .subscribe {
                (view as VenuesListView).hideProgress()
                (view as VenuesListView).displayVenues(it)
            }
    }
}
