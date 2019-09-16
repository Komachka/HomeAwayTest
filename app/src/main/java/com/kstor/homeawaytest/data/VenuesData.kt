package com.kstor.homeawaytest.data

data class VenusData(
    val venues: List<Venue>,
    val citCenterlat: Double,
    val citCenterlng: Double
)

data class Venue(
    val id: String,
    val name: String,
    val categories: List<VenueCategory>,
    val address: String,
    val distance: Int,
    val lat: Double,
    val lng: Double
)

data class VenueCategory(
    val id: String,
    val name: String,
    val iconPath: String
)
