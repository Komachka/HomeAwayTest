package com.kstor.homeawaytest.data

data class VenusData(
    val venues: List<Venue>,
    val citCenterlat: Double,
    val citCenterlng: Double
)

data class Venue(
    var id: String?=null,
    var name: String?=null,
    var categories: List<VenueCategory>?=null,
    var address: String?=null,
    var distance: Int?=null,
    var lat: Double?=null,
    var lng: Double?=null
)

data class VenueCategory(
    var id: String?,
    var name: String?,
    val iconPath: String
)
