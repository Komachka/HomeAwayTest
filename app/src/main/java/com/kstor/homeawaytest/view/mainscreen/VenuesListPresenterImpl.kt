package com.kstor.homeawaytest.view.mainscreen

import com.kstor.homeawaytest.data.log
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.view.BasePresentor
import com.kstor.homeawaytest.view.BaseView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class VenuesListPresenterImpl (
    private val useCase: VenuesUseCase) :
    VenuesListPresenter, BasePresentor<VenuesListView>() {

    override fun getVenues(query: String) {
        useCase.loadVenuesData(query).subscribeOn(Schedulers.io())
            .map {
                it.venues
            }
            .doOnError {
                log(it.toString())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                (view as VenuesListView).hideProgress()
                (view as VenuesListView).displayVenues(it)
            }
    }
}
