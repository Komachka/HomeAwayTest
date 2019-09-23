package com.kstor.homeawaytest.view.mainscreen

import android.view.View
import androidx.navigation.Navigation
import com.google.android.material.internal.NavigationMenu
import com.kstor.homeawaytest.data.log
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.domain.model.VenuesParcelize
import com.kstor.homeawaytest.view.BasePresentor
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class VenuesListPresenterImpl(
    private val useCase: VenuesUseCase,
    private val iOScheduler: Scheduler,
    private val mainScheduler: Scheduler
) :
    VenuesListPresenter, BasePresentor<VenuesListView>() {

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
