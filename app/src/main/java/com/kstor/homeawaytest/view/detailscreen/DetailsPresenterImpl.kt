package com.kstor.homeawaytest.view.detailscreen

import com.kstor.homeawaytest.domain.GenerateStaticMapUrlUseCase
import com.kstor.homeawaytest.domain.model.VenuesParcelize
import com.kstor.homeawaytest.view.BasePresentor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailsPresenterImpl (
    private val useCase: GenerateStaticMapUrlUseCase
) : DetailsPresenter, BasePresentor<DetailsView>() {

    override fun createStaticMapUrl(venues: VenuesParcelize) {
        useCase.createStaticMapUrl(venues)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                (view as DetailsView).loadMap(it)
            }
    }
}
