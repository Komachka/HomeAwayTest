package com.kstor.homeawaytest.view.mapscreen

import androidx.lifecycle.ViewModelProviders
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

import com.kstor.homeawaytest.R
import com.kstor.homeawaytest.data.log


class MapFragment : Fragment(), OnMapReadyCallback {

    override fun onMapReady(googleMap: GoogleMap?) {
        val sydney = LatLng(-34.0, 151.0)
        if (googleMap == null)
            log("google map is null")
        else
        {
            log("google map is not null")
        }
        googleMap?.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    companion object {
        fun newInstance() = MapFragment()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
}
