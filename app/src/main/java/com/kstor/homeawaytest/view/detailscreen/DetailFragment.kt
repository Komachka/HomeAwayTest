package com.kstor.homeawaytest.view.detailscreen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.kstor.homeawaytest.R
import com.kstor.homeawaytest.data.PERSISTENT_STORAGE_NAME
import com.kstor.homeawaytest.data.log
import com.kstor.homeawaytest.view.ImageLoader
import kotlinx.android.synthetic.main.detail_fragment.*

class DetailFragment : Fragment(), ImageLoader {

    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            val venues = DetailFragmentArgs.fromBundle(it).venues
            val API_KEY = "AIzaSyC6SflSVpRXbS2qbY0P7yJ1THGl-dOJiUQ"

            val settings: SharedPreferences? = context?.getSharedPreferences(PERSISTENT_STORAGE_NAME, Context.MODE_PRIVATE)
            val latCenter = settings?.getFloat("lat", 0.0F)
            val lngCenter = settings?.getFloat("lng", 0.0F)
            val latPoint = venues.lat
            val lngPoint = venues.lng
            val colour1 = "blue"
            val colour2 = "green"
            val zoom = when {
                venues.distance in 0..100 -> 17
                venues.distance in 100..500 -> 15
                venues.distance in 500..2000 ->13
                venues.distance in 2000..4000 ->12
                else -> 10
            }
            log("latPoint $latPoint lngPoint $lngPoint")
            val url = "https://maps.googleapis.com/maps/api/staticmap?" +
                    "center=$latCenter,$lngCenter"+
                    "&zoom=$zoom" +
                    "&size=600x350" +
                    "&maptype=terrain" +
                    "&markers=color:$colour1%7Clabel:C%7C$latCenter,$lngCenter" +
                    "&markers=color:$colour2%7Clabel:P%7C$latPoint,$lngPoint" +
                    "&key=$API_KEY"
            //log("url $url")
            //mapIv.loadImage(url)
            toolbar.apply {
                title = venues.name
            }
            venuesNameNameTextView.text = venues.name
            venuesCategory.text = venues.categories?.let { category -> category.joinToString { it.name ?: "" } }
            venuesNameAdressTextView.text = venues.address
            venues.categories?.get(0)?.iconPath?.let {
                venuesPlaceImgView.loadImage(it)
            }
            venuesDistanceFromCenterTextView.text = "${venues.distance} m"
            if(venues.isFavorite)
                fabFavorite.setImageResource(R.drawable.ic_favorite_black_24dp)
            else
                fabFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp)

        }
    }
}
