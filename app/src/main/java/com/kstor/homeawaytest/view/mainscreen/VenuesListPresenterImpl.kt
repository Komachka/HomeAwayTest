package com.kstor.homeawaytest.view.mainscreen

import android.view.View
import androidx.navigation.Navigation
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.BasePresentor
import com.kstor.homeawaytest.view.BaseView
import com.kstor.homeawaytest.view.VenuesMapper
import com.kstor.homeawaytest.view.utils.SchedulerProvider
import io.reactivex.rxkotlin.subscribeBy

class VenuesListPresenterImpl(
    private val useCase: VenuesUseCase,
    private val schedulerProvider: SchedulerProvider
) :
    VenuesListPresenter, BasePresentor<VenuesListView>(), VenuesMapper {
    override fun showError(throwable: Throwable) {
        view?.showError(throwable)
    }

    override fun showProgress() {
        view?.showProgress()
    }

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

    override fun navigateToDetailScreen(view: View, venue: Venues) {
        map(venue)?.let {
            val action =
                VenuesListFragmentDirections.actionVenuesListFragmentToDetailFragment(it)
            Navigation.findNavController(view).navigate(action)
        }
    }
}
