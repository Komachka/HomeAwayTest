package com.kstor.homeawaytest.data

const val CLIENT_ID = "FVKVXRRGCLEW0VUADJQVIEHKMWIGURSZV1QM4GUHWVJFT1QT"
const val CLIENT_SECRET = "LHTQZG3SSVF1PBTIMZUCWV3JX5ED3ZPCICM0LWB4HVFZUUEU"

const val BASE_URL = "https://api.foursquare.com/v2/venues/"
const val SEARCH_URL = "search?"
const val DETAIL_URL = "{id}"
const val CLIENT_ID_QUERY_PARAM = "client_id"
const val CLIENT_SECRET_QUERY_PARAM = "client_secret"
const val NEAR_QUERY_PARAM = "near"
const val SEARCH_QUERY_PARAM = "query"
const val VERSION_QUERY_PARAM = "v"
const val LIMIT_QUERY_PARAM = "limit"
const val LOAD_LIMIT = 15
const val LOADING_TIMEOUT = 1000L
const val MIN_INPUT_LENGTH = 2

const val SIZE_32 = "32"
const val SIZE_44 = "44"
const val SIZE_64 = "64"
const val SIZE_88 = "88"
const val SIZE_400 = "400"

const val NEAR = "Seattle,+WA"
const val V = "20180401"
const val CENTER_LAT = 47.6062
const val CENTER_LNG = -122.3321

const val RADIUS = 6378 * 1000 // metres
const val HALF_OF_CIRCLE_DEGREE = 180

const val PERSISTENT_STORAGE_NAME = "city_center"
const val PERSISTENT_STORAGE_KEY_LAT = "lat"
const val PERSISTENT_STORAGE_KEY_LNG = "lng"
const val PERSISTENT_STORAGE_DEF_VAL = 0.0F

typealias LAT = Double
typealias LNG = Double

const val API_KEY = "AIzaSyC6SflSVpRXbS2qbY0P7yJ1THGl-dOJiUQ"
const val STATIC_MAP_BASE_URL = "https://maps.googleapis.com/maps/api/staticmap?"

const val colour1 = "blue"
const val colour2 = "red"

const val CENTER = "center"
const val ZOOM = "zoom"
const val SIZE = "size"
const val MAPTYPE = "maptype"
const val MARKERS = "markers"
const val KEY = "key"

const val IMAGE_SIZE = "600x350"
const val MAP_TYPE_TERRIAN = "terrian"

const val DATA_BASE_NAME = "app-database"
const val FAVORITE_TABLE_NAME = "favorite"
const val VENUES_TABLE_NAME = "venues"
const val EMPTY_SEARCH_ERROR_MESSAGE = "Result is empty"
const val NO_FAVORITE_MESSAGE = "There are no favorites yet"

const val EMPTY_VENUES_LIST_MESSAGE = "The venues list is empty"
const val INVALID_CITY_CENTER = "The city center info is incorrect"
const val LOCAL_DATA_EMPTY = "The local data is empty"


