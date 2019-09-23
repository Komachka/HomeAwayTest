package com.kstor.homeawaytest.view.mapscreen


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
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
import com.kstor.homeawaytest.data.log
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.domain.model.VenusData
import com.kstor.homeawaytest.view.BaseFragment
import com.kstor.homeawaytest.view.BasePresentor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class MapFragment : BaseFragment(), OnMapReadyCallback, MapView {

    var myMap:GoogleMap? = null
    lateinit var mapPresentor:MapPresenter


    @Inject
    lateinit var useCases: VenuesUseCase

    override fun showCenterOnTheMap(venusData: VenusData) {
        val sydney = LatLng(venusData.citCenterlat, venusData.citCenterlng)
        myMap?.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        myMap?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        myMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f))

    }


    override fun showVenuesOnTheMap(venues: List<Venues>) {
        venues.forEach {
            addVenuesMarker(it)
        }

    }

    private fun addVenuesMarker(venues: Venues) {
        venues.lat?.let { lat->
            venues.lng?.let {lng ->
                val position = LatLng(lat, lng)
                venues.name?.let { myMap?.addMarker(MarkerOptions().position(position).title(it)) }
            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as App).homeAwayComponents.inject(this)
    }

    override fun setUp() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapPresentor = MapPresentorImpl(useCases, Schedulers.io(), AndroidSchedulers.mainThread())
        (mapPresentor as MapPresentorImpl).attachView(this)
        mapPresentor.getVenues("coffe") // late we will take it from database
    }

    override fun destroy() {
        (mapPresentor as MapPresentorImpl).detachView()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let {
            myMap = it
        }

        val sydney = LatLng(-34.0, 151.0)
        if (googleMap == null)
            log("google map is null")
        else
        {
            log("google map is not null")
        }

        //googleMap?.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //googleMap?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }


}
