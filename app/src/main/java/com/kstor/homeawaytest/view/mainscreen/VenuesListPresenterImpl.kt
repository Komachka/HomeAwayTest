package com.kstor.homeawaytest.view.mainscreen

import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.view.BasePresentor
import com.kstor.homeawaytest.view.BaseView
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.subscribeBy

class VenuesListPresenterImpl(
    private val useCase: VenuesUseCase,
    private val iOScheduler: Scheduler,
    private val mainScheduler: Scheduler
) :
    VenuesListPresenter, BasePresentor<VenuesListView>() {

    override fun getVenues(query: String) {
        useCase.loadVenuesData(query).toObservable().subscribeOn(iOScheduler)
            .map {
                it.venues
            }
            .observeOn(mainScheduler)
            .subscribeBy(
                onNext = {
                    view?.hideProgress()
                    view?.displayVenues(it)
                }, onError = {
                    (view as BaseView).showError(it)
                }, onComplete = {
                    println("Complete")
                }
            )
    }
}
