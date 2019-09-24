package com.kstor.homeawaytest.view.mapscreen

import android.view.View
import androidx.navigation.Navigation
import com.google.android.gms.maps.model.LatLng
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.BasePresentor
import com.kstor.homeawaytest.view.VenuesMapper
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.subscribeBy

class MapPresenterImpl(
    private val venuesListUseCase: VenuesUseCase,
    private val iOScheduler: Scheduler,
    private val mainScheduler: Scheduler
) : MapPresenter, BasePresentor<MapView>(), VenuesMapper {

    private lateinit var venuesMap: Map<LatLng, Venues>

    override fun navigateToDetailsScreen(view: View, position: LatLng) {
        venuesMap[position]?.let { venues ->
            map(venues)?.let {
                Navigation.findNavController(view).navigate(MapFragmentDirections.actionMapFragmentToDetailFragment(it))
            }
        }
    }

    override fun getVenues(query: String) {
        venuesListUseCase.loadVenuesData(query)
            .subscribeOn(iOScheduler)
            .observeOn(mainScheduler)
            .doOnNext {
                view?.showCenterOnTheMap(it)
            }
            .map { it.venues }
            .subscribeBy {
                venuesMap = it.createMarkersMap()
                view?.showVenuesOnTheMap(venuesMap)
        }
    }

    private fun List<Venues>.createMarkersMap(): Map<LatLng, Venues> {
        return this.filter {
            it.lat != null && it.lng != null
        }.map {
            val lat = it.lat ?: 0.0
            val lng = it.lng ?: 0.0
            LatLng(lat, lng) to it
        }.toMap()
    }
}
