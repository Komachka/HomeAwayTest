package com.kstor.homeawaytest.view.mainscreen

import com.kstor.homeawaytest.data.log
import com.kstor.homeawaytest.domain.VenuesUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class VenuesListPresenterImpl @Inject constructor(private val useCase: VenuesUseCase) :
    VenuesListPresenter {
    lateinit var venuesListView: VenuesListView

    override fun setView(view: VenuesListView) {
        venuesListView = view
    }

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
                venuesListView.hideProgress()
                venuesListView.displayVenues(it)
            }
    }
}
