package com.kstor.homeawaytest.data

data class VenusData(
    val venues: List<Venues>,
    val citCenterlat: Double,
    val citCenterlng: Double
)

data class Venues(
    var id: String? = null,
    var name: String? = null,
    var categories: List<VenuesCategory>? = null,
    var address: String? = null,
    var distance: Int? = null,
    var lat: Double? = null,
    var lng: Double? = null
)

data class VenuesCategory(
    var id: String?,
    var name: String?,
    val iconPath: String
)
