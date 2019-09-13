package com.kstor.homeawaytest.data.network.model
import com.google.gson.annotations.Expose

data class NetworkVenuesModel(
    @Expose
    var meta: Any? = null,
    @Expose
    var response: Response? = null
)

data class Response(
    @Expose
    var venues: List<NetworkVenue>? = null,
    @Expose
    var geocode: Geocode? = null
)

data class NetworkVenue(
    @Expose
    var id: String? = null,
    @Expose
    var name: String? = null,
    @Expose
    var contact: Any? = null,
    @Expose
    var location: Location? = null,
    @Expose
    var categories: List<NetworkCategory>? = null,
    @Expose
    var verified: Boolean? = null,
    @Expose
    var stats: Any? = null,
    @Expose
    var beenHere: Any? = null,
    @Expose
    var hereNow: Any? = null,
    @Expose
    var referralId: String? = null,
    @Expose
    var venueChains: List<Any>? = null,
    @Expose
    var hasPerk: Boolean? = null,
    @Expose
    var venuePage: String? = null
)

data class Location(
    @Expose
    var address: String? = null,
    @Expose
    var crossStreet: String? = null,
    @Expose
    var lat: Double? = null,
    @Expose
    var lng: Double? = null,
    @Expose
    var labeledLatLngs: List<Any>? = null,
    @Expose
    var postalCode: String? = null,
    @Expose
    var cc: String? = null,
    @Expose
    var city: String? = null,
    @Expose
    var state: String? = null,
    @Expose
    var country: String? = null,
    @Expose
    var formattedAddress: List<Any>? = null,
    @Expose
    var neighborhood: String? = null
)

data class NetworkCategory(
    @Expose
    var id: String? = null,
    @Expose
    var name: String? = null,
    @Expose
    var pluralName: String? = null,
    @Expose
    var shortName: String? = null,
    @Expose
    var icon: Icon? = null,
    @Expose
    var primary: Boolean? = null
)

data class Icon(
    @Expose
    var prefix: String? = null,
    @Expose
    var suffix: String? = null
)

class Geocode {
    @Expose
    var what: String? = null
    @Expose
    var where: String? = null
    @Expose
    var feature: Feature? = null
    @Expose
    var parents: List<Any>? = null
}

data class Feature(
    @Expose
    var cc: String? = null,
    @Expose
    var name: String? = null,
    @Expose
    var displayName: String? = null,
    @Expose
    var matchedName: String? = null,
    @Expose
    var highlightedName: String? = null,
    @Expose
    var woeType: Int? = null,
    @Expose
    var slug: String? = null,
    @Expose
    var id: String? = null,
    @Expose
    var longId: String? = null,
    @Expose
    var geometry: Geometry? = null
)

data class Geometry(
    @Expose
    var center: Coordinate? = null,
    @Expose
    var bounds: Bounds? = null
)

data class Coordinate(
    @Expose
    var lat: Double? = null,
    @Expose
    var lng: Double? = null

)

class Bounds {
    @Expose
    var ne: Coordinate? = null
    @Expose
    var sw: Coordinate? = null
}