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

    private val venuesMap = mutableMapOf<LatLng, Venues>()

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
               it.createVenuesMap()
                view?.showVenuesOnTheMap(venuesMap)
        }
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
