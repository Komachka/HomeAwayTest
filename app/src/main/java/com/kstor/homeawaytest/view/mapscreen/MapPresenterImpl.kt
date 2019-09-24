package com.kstor.homeawaytest.view.mapscreen

import android.view.View
import androidx.navigation.Navigation
import com.google.android.gms.maps.model.LatLng
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.BasePresentor
import com.kstor.homeawaytest.view.BaseView
import com.kstor.homeawaytest.view.RxPresentor
import com.kstor.homeawaytest.view.VenuesMapper
import com.kstor.homeawaytest.view.utils.SchedulerProvider

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy


class MapPresenterImpl(
    private val venuesListUseCase: VenuesUseCase,
    private val schedulerProvider: SchedulerProvider
) : MapPresenter, BasePresentor<MapView>(), VenuesMapper, RxPresentor {

    private val venuesMap = mutableMapOf<LatLng, Venues>()
    private val compositeDisposable = CompositeDisposable()

    override fun navigateToDetailsScreen(view: View, position: LatLng) {
        venuesMap[position]?.let { venues ->
            map(venues)?.let {
                Navigation.findNavController(view).navigate(MapFragmentDirections.actionMapFragmentToDetailFragment(it))
            }
        }
    }

    override fun getVenues(query: String) {
        compositeDisposable.add(venuesListUseCase.loadVenuesData(query)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSuccess {
                view?.showCenterOnTheMap(it)
            }
            .map { it.venues }
            .subscribeBy (
                onError = {
                    (view as BaseView).showError(it)
                },
                onSuccess = {
                    it.createVenuesMap()
                    view?.showVenuesOnTheMap(venuesMap)
                }
            ))
    }

    private fun List<Venues>.createVenuesMap(): Map<LatLng, Venues> {
        this.forEach {
            it.lat?.let { lat ->
                it.lng?.let { lng ->
                    venuesMap.put(LatLng(lat, lng), it)
                }
            }
        }
        return venuesMap
    }

    override fun onDispoce() {
        compositeDisposable.clear()
    }

}
