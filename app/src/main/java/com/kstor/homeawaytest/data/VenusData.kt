package com.kstor.homeawaytest.data

class VenusData(
    val venues: List<Venue>,
    val citCenterlat:Double,
    val citCenterlng:Double
)

class Venue(
    val id: String,
    val name: String,
    val categories: List<VenueCategory>,
    val address: String,
    val lat: Double,
    var lng: Double? = null
)

class VenueCategory(
    val id: String,
    val name: String,
    val iconPath: String
)