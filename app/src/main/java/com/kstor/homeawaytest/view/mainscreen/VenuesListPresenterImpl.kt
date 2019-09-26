package com.kstor.homeawaytest.view.mainscreen

import android.view.View
import androidx.navigation.Navigation
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.domain.model.VenuesParcelize
import com.kstor.homeawaytest.view.BasePresenter
import com.kstor.homeawaytest.view.BaseView
import com.kstor.homeawaytest.view.VenuesMapper
import com.kstor.homeawaytest.view.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class VenuesListPresenterImpl(compositeDisposable: CompositeDisposable,
    private val useCase: VenuesUseCase,
    private val schedulerProvider: SchedulerProvider
) :
    VenuesListPresenter, BasePresenter<VenuesListView>(compositeDisposable), VenuesMapper {

    override fun hideMupButton() {
        view?.hideMupButn()
    }

    override fun showError(throwable: Throwable) {
        view?.showError(throwable)
    }

    override fun showProgress() {
        view?.showProgress()
    }

    private var query = ""

    override fun navigateToDetailsScreen(view: View, venuesParcelize: VenuesParcelize) {
        val action =
            VenuesListFragmentDirections.actionVenuesListFragmentToDetailFragment(venuesParcelize)
        Navigation.findNavController(view).navigate(action)
    }

    override fun navigateToMapScreen(view: View) {
        Navigation.findNavController(view).navigate(VenuesListFragmentDirections.actionVenuesListFragmentToMapFragment(query))
    }

    override fun getVenues(query: String) {
        this.query = query
        useCase.loadVenuesData(query).toObservable().subscribeOn(schedulerProvider.io())

            .map {
                it.venues
            }
            .observeOn(schedulerProvider.ui())
            .subscribeBy(
                onNext = {
                    view?.hideProgress()
                    view?.displayVenues(it)
                    view?.showMupButn()
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
