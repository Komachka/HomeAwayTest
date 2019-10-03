package com.kstor.homeawaytest.data.network.model

data class NetworkDetailsModel(
    var meta: Any? = null,
    var response: DetailResponse? = null
)

data class DetailResponse(
    var venue: DetailVenue? = null
)

data class DetailVenue(
    var id: String? = null,
    var name: String? = null,
    var contact: Contact? = null,
    var location: Location? = null,
    var canonicalUrl: String? = null,
    var categories: List<NetworkCategory>? = null,
    var verified: Boolean? = null,
    var stats: Any? = null,
    var url: String? = null,
    var price: Any? = null,
    var likes: Any? = null,
    var dislike: Boolean? = null,
    var ok: Boolean? = null,
    var rating: Double? = null,
    var ratingColor: String? = null,
    var ratingSignals: Int? = null,
    var menu: Any? = null,
    var allowMenuUrlEdit: Boolean? = null,
    var beenHere: Any? = null,
    var specials: Any? = null,
    var photos: Any? = null,
    var venuePage: Any? = null,
    var reasons: Any? = null,
    var description: String? = null,
    var page: Any? = null,
    var hereNow: Any? = null,
    var createdAt: Int? = null,
    var shortUrl: String? = null,
    var timeZone: String? = null,
    var listed: Any? = null,
    var hours: Hours? = null,
    var popular: Any? = null,
    var pageUpdates: Any? = null,
    var inbox: Any? = null,
    var parent: Any? = null,
    var hierarchy: List<Any>? = null,
    var attributes: Any? = null,
    var bestPhoto: BestPhoto? = null,
    var colors: Any? = null
)

data class Contact(
    var phone: String? = null,
    var formattedPhone: String? = null,
    var facebook: String? = null,
    var facebookUsername: String? = null,
    var facebookName: String? = null
)

data class Hours(
    var status: String? = null,
    var richStatus: Any? = null,
    var isOpen: Boolean? = null,
    var isLocalHoliday: Boolean? = null,
    var dayData: List<Any>? = null,
    var timeframes: List<Timeframe>? = null
)

data class Timeframe(
    var days: String? = null,
    var includesToday: Boolean? = null,
    var open: List<Open>? = null,
    var segments: List<Any>? = null
)

data class Open(
    var renderedTime: String? = null
)

data class BestPhoto(
    var id: String? = null,
    var createdAt: Int? = null,
    var source: Any? = null,
    var prefix: String? = null,
    var suffix: String? = null,
    var width: Int? = null,
    var height: Int? = null,
    var visibility: String? = null
)
