package com.kstor.homeawaytest.view.mainscreen

import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.view.BasePresentor
import com.kstor.homeawaytest.view.BaseView
import com.kstor.homeawaytest.view.utils.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.subscribeBy

class VenuesListPresenterImpl(
    private val useCase: VenuesUseCase,
    private val schedulerProvider: SchedulerProvider
) :
    VenuesListPresenter, BasePresentor<VenuesListView>() {

    override fun getVenues(query: String) {
        useCase.loadVenuesData(query).toObservable().subscribeOn(schedulerProvider.io())
            .map {
                it.venues
            }
            .observeOn(schedulerProvider.ui())
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
