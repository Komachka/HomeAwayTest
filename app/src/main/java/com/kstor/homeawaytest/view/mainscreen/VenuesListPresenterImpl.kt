package com.kstor.homeawaytest.view.mainscreen

import android.view.View
import androidx.navigation.Navigation
import com.kstor.homeawaytest.data.log
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.domain.model.VenuesParcelize
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

    override fun getFavorites() {
        useCase.getFavorites().subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribeBy(
                onSuccess = {
                    view?.hideProgress()
                    view?.displayVenues(it)
                    view?.showMupButn()
                    log("count ${it.size}")
                }, onError = {
                    (view as BaseView).showError(it)
                }
            )
    }

    override fun addToFavorite(venues: Venues) {
        if (!venues.isFavorite) {
            useCase.addToFavorite(venues)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeBy(
                    onComplete = {
                        view?.updateItemView(venues)
                    },
                    onError = {})
        } else {
            log("is already favorite")
            useCase.removeFromFavorite(venues)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeBy(
                    onComplete = {
                        view?.updateItemView(venues)
                    },
                    onError = {})
        }
    }

    override fun hideMupButton() {
        view?.hideMupButn()
    }

    override fun showError(throwable: Throwable) {
        view?.showError(throwable)
    }

    override fun showProgress() {
        view?.showProgress()
    }


    override fun navigateToDetailsScreen(view: View, venuesParcelize: VenuesParcelize) {
        val action =
            VenuesListFragmentDirections.actionVenuesListFragmentToDetailFragment(venuesParcelize)
        Navigation.findNavController(view).navigate(action)
    }

    override fun navigateToMapScreen(view: View, query: String) {
        Navigation.findNavController(view)
            .navigate(VenuesListFragmentDirections.actionVenuesListFragmentToMapFragment(query))
    }

    override fun getVenues(query: String) {
        useCase.loadVenuesData(query).subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribeBy(
                onNext = {
                    view?.hideProgress()
                    view?.displayVenues(it)
                    view?.showMupButn()
                    log("count ${it.size}")
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
