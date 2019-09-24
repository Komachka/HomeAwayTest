package com.kstor.homeawaytest.data.network.model

data class NetworkVenuesModel(
    var meta: Any? = null,
    var response: Response? = null
)

data class Response(
    var venues: List<NetworkVenue>? = null,
    var geocode: Geocode? = null
)

data class NetworkVenue(
    var id: String? = null,
    var name: String? = null,
    var contact: Any? = null,
    var location: Location? = null,
    var categories: List<NetworkCategory>? = null,
    var verified: Boolean? = null,
    var stats: Any? = null,
    var beenHere: Any? = null,
    var hereNow: Any? = null,
    var referralId: String? = null,
    var venueChains: List<Any>? = null,
    var hasPerk: Boolean? = null,
    var venuePage: Any? = null
)

data class Location(
    var address: String? = null,
    var crossStreet: String? = null,
    var lat: Double? = null,
    var lng: Double? = null,
    var labeledLatLngs: List<Any>? = null,
    var postalCode: String? = null,
    var cc: String? = null,
    var city: String? = null,
    var state: String? = null,
    var country: String? = null,
    var formattedAddress: List<Any>? = null,
    var neighborhood: String? = null
)

data class NetworkCategory(
    var id: String? = null,
    var name: String? = null,
    var pluralName: String? = null,
    var shortName: String? = null,
    var icon: Icon? = null,
    var primary: Boolean? = null
)

data class Icon(
    var prefix: String? = null,
    var suffix: String? = null
)

data class Geocode(
    var what: String? = null,
    var where: String? = null,
    var feature: Feature? = null,
    var parents: List<Any>? = null
)

data class Feature(
    var cc: String? = null,
    var name: String? = null,
    var displayName: String? = null,
    var matchedName: String? = null,
    var highlightedName: String? = null,
    var woeType: Int? = null,
    var slug: String? = null,
    var id: String? = null,
    var longId: String? = null,
    var geometry: Geometry? = null
)

data class Geometry(
    var center: Coordinate? = null,
    var bounds: Bounds? = null
)

data class Coordinate(
    var lat: Double? = null,
    var lng: Double? = null
)

data class Bounds(
    var ne: Coordinate? = null,
    var sw: Coordinate? = null
)
