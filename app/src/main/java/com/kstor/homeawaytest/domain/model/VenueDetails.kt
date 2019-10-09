package com.kstor.homeawaytest.domain.model

data class VenueDetails(
    var id: String? = null,
    var name: String? = null,
    var formattedPhone: String? = null,
    var canonicalUrl: String? = null,
    var url: String? = null,
    var rating: Double? = null,
    var ratingColor: String? = null,
    var description: String? = null,
    var shortUrl: String? = null,
    var isOpen: Boolean? = null,
    var hoursPerDay: List<HoursPerDay>? = null,
    var bestPhoto: String? = null
)

data class HoursPerDay(
    var days: String? = null,
    var renderedTime: String? = null
)
