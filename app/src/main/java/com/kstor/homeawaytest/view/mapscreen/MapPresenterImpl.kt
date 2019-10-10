package com.kstor.homeawaytest.view.mapscreen

import android.view.View
import androidx.navigation.Navigation
import com.google.android.gms.maps.model.LatLng
import com.kstor.homeawaytest.data.log
import com.kstor.homeawaytest.domain.RepoResult
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.domain.model.Venue
import com.kstor.homeawaytest.view.base.BasePresenter
import com.kstor.homeawaytest.view.utils.DispatcherProvider
import com.kstor.homeawaytest.view.utils.VenuesMapper
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapPresenterImpl @Inject constructor(
    private val venuesListUseCase: VenuesUseCase,
    dispatcherProvider: DispatcherProvider
) : MapPresenter, BasePresenter<MapView>(dispatcherProvider),
    VenuesMapper {

    override fun setUpMapToCityCenter() {
        launch {
            val resultCityCenter = venuesListUseCase.getCityCenter()
            withContext(Dispatchers.Main) {
                handleRepoResult(resultCityCenter,
                    success = {
                        val (lat, lng) = (resultCityCenter as RepoResult.Success).data
                        view?.showCenterOnTheMap(LatLng(lat.toDouble(), lng.toDouble()))
                    }, fail = {
                        view?.showError((resultCityCenter as RepoResult.Error<*>).throwable)
                    })
            }
        }
    }

    private val venuesMap = mutableMapOf<LatLng, Venue>()

    override fun navigateToDetailsScreen(view: View, position: LatLng) {
        venuesMap[position]?.let { venues ->
            mapToParcelize(venues)?.let {
                Navigation.findNavController(view)
                    .navigate(MapFragmentDirections.actionMapFragmentToDetailFragment(it))
            }
        }
    }

    override fun getVenues(query: String) {
        launch {
            val resultVenues = venuesListUseCase.loadVenuesCache()
            withContext(Dispatchers.Main) {
                handleRepoResult(resultVenues,
                    success = {
                        (resultVenues as RepoResult.Success).data.createVenuesMap()
                        view?.showVenuesOnTheMap(venuesMap)
                    }, fail = {
                        view?.showError((resultVenues as RepoResult.Error<*>).throwable)
                    })
            }

        }
    }


    private fun List<Venue>.createVenuesMap(): Map<LatLng, Venue> {
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
