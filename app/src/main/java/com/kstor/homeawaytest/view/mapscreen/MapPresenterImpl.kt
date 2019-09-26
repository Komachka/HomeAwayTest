package com.kstor.homeawaytest.view.mapscreen

import android.view.View
import androidx.navigation.Navigation
import com.google.android.gms.maps.model.LatLng
import com.kstor.homeawaytest.data.CENTER_LAT
import com.kstor.homeawaytest.data.CENTER_LNG
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.BasePresenter
import com.kstor.homeawaytest.view.BaseView
import com.kstor.homeawaytest.view.VenuesMapper
import com.kstor.homeawaytest.view.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class MapPresenterImpl(
    compositeDisposable: CompositeDisposable,
    private val venuesListUseCase: VenuesUseCase,
    private val schedulerProvider: SchedulerProvider
) : MapPresenter, BasePresenter<MapView>(compositeDisposable), VenuesMapper {

    override fun setUpMapToCityCenter() {
        view?.showCenterOnTheMap(LatLng(CENTER_LAT, CENTER_LNG))
    }

    private val venuesMap = mutableMapOf<LatLng, Venues>()

    override fun navigateToDetailsScreen(view: View, position: LatLng) {
        venuesMap[position]?.let { venues ->
            mapToPaprelize(venues)?.let {
                Navigation.findNavController(view)
                    .navigate(MapFragmentDirections.actionMapFragmentToDetailFragment(it))
            }
        }
    }

    override fun getVenues(query: String) {
        compositeDisposable.add(venuesListUseCase.loadVenuesData(query)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnNext {
                val (lat, lng) = venuesListUseCase.getCityCenter()
                view?.showCenterOnTheMap(LatLng(lat.toDouble(), lng.toDouble()))
            }
            .subscribeBy(
                onError = {
                    (view as BaseView).showError(it)
                },
                onNext = {
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
}
