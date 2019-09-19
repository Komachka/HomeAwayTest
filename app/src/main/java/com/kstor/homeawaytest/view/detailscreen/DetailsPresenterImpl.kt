package com.kstor.homeawaytest.view.detailscreen

import com.kstor.homeawaytest.domain.GenerateStaticMapUrlUseCase
import com.kstor.homeawaytest.domain.model.VenuesParcelize
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailsPresenterImpl @Inject constructor(
    private val useCase: GenerateStaticMapUrlUseCase
) : DetailsPresenter {

    private lateinit var detailsView: DetailsView
    override fun setView(view: DetailsView) {
        detailsView = view
    }

    override fun createStaticMapUrl(venues: VenuesParcelize) {
        useCase.createStaticMapUrl(venues)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                detailsView.loadMap(it)
            }
    }
}
