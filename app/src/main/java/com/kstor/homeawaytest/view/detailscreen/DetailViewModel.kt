package com.kstor.homeawaytest.view.detailscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kstor.homeawaytest.data.API_KEY
import com.kstor.homeawaytest.data.STATIC_MAP_BASE_URL
import com.kstor.homeawaytest.data.sp.SharedPreferenceData
import com.kstor.homeawaytest.view.VenuesParcelize

class DetailViewModel(private val preferenceData: SharedPreferenceData) : ViewModel() {

    fun createStaticMapUrl(venues: VenuesParcelize): String {
        val (latCenter, lngCenter) = preferenceData.getCityCenterInfo()
        val latPoint = venues.lat
        val lngPoint = venues.lng
        val colour1 = "blue"
        val colour2 = "green"
        val zoom = when {
            venues.distance in 0..100 -> 17
            venues.distance in 100..500 -> 15
            venues.distance in 500..2000 -> 13
            venues.distance in 2000..4000 -> 12
            else -> 10
        }

        return STATIC_MAP_BASE_URL +
                "center=$latCenter,$lngCenter" +
                "&zoom=$zoom" +
                "&size=600x350" +
                "&maptype=terrain" +
                "&markers=color:$colour1%7Clabel:C%7C$latCenter,$lngCenter" +
                "&markers=color:$colour2%7Clabel:P%7C$latPoint,$lngPoint" +
                "&key=$API_KEY"
    }
}

class DetailViewModelFactory(private val preferenceData: SharedPreferenceData) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            DetailViewModel(this.preferenceData) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
