package com.kstor.homeawaytest.domain.model

data class VenuesData(
    val venues: List<Venues>,
    val citCenterlat: Double,
    val citCenterlng: Double
)

data class Venues(
    var id: String? = null,
    var name: String? = null,
    var categories: VenuesCategory? = null,
    var address: String? = null,
    var distance: Int? = null,
    var lat: Double? = null,
    var lng: Double? = null,
    var isFavorite: Boolean = false
)

data class VenuesCategory(
    var id: String?,
    var name: String?,
    val iconPath: String
)
