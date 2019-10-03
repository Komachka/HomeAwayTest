package com.kstor.homeawaytest.view.mapscreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kstor.homeawaytest.App
import com.kstor.homeawaytest.R
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.base.BaseFragment
import javax.inject.Inject

class MapFragment : BaseFragment(), OnMapReadyCallback, MapView {

    private var myMap: GoogleMap? = null
    @Inject
    lateinit var mapPresenter: MapPresenter

    override fun showCenterOnTheMap(sydney: LatLng) {
        myMap?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        myMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f))
    }

    override fun showVenuesOnTheMap(venuesMap: Map<LatLng, Venues>) {
        venuesMap.forEach {
            addVenuesMarker(it)
        }
    }

    private fun addVenuesMarker(venues: Map.Entry<LatLng, Venues>) {
        myMap?.addMarker(MarkerOptions().position(venues.key).title(venues.value.name))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as App).homeAwayComponents.inject(this)
    }

    override fun setUp() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        (mapPresenter as MapPresenterImpl).attachView(this)
        arguments?.let {
            mapPresenter.getVenues(MapFragmentArgs.fromBundle(it).query)
        }
    }

    override fun destroy() {
        (mapPresenter as MapPresenterImpl).apply {
            detachView()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let {
            myMap = it
        }
        googleMap?.setOnInfoWindowClickListener {
            marker ->
            view?.let {
                mapPresenter.navigateToDetailsScreen(it, marker.position)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }
}
