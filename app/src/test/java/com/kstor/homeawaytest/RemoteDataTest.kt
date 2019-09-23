package com.kstor.homeawaytest

import com.kstor.homeawaytest.data.CLIENT_ID
import com.kstor.homeawaytest.data.CLIENT_SECRET
import com.kstor.homeawaytest.data.NEAR
import com.kstor.homeawaytest.data.V
import com.kstor.homeawaytest.data.network.RemoteData
import com.kstor.homeawaytest.data.network.VenuesService
import com.kstor.homeawaytest.data.network.model.*
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RemoteDataTest {

    @Mock
    lateinit var venuesService: VenuesService
    private val search = {
        venuesService.getVenusesNetworkData(
            CLIENT_ID,
            CLIENT_SECRET,
            NEAR,
            QUERY,
            V,
            LIMIT
        )
    }

    private val failedSearch = {
        venuesService.getVenusesNetworkData(CLIENT_ID, CLIENT_SECRET, NEAR,
            ERROR_QUERY, V, LIMIT
        )
    }
    private lateinit var error: Throwable

    @InjectMocks
    lateinit var remoteData: RemoteData

    @Before
    fun setup() {
        error = Throwable("Some Error occurred")
        mockGetVenuesNetworkData()
        mockGetVenuesErrorData()
    }

    private fun mockGetVenuesErrorData() {
        `when`(failedSearch.invoke()).thenReturn(
            Observable.error<NetworkVenuesModel>(error).firstOrError()
        )
    }

    private fun mockGetVenuesNetworkData() {
        `when`(search.invoke()).thenReturn(
            Observable.just(
                NetworkVenuesModel(
                    null,
                    Response(
                        listOf<NetworkVenue>(
                            NetworkVenue(
                                "52d456c811d24128cdd7bc8b",
                                "Storyville Coffee Company",
                                null,
                                Location(
                                    "1001 1st Ave",
                                    "at Madison St",
                                    47.60475923205166,
                                    -122.33636210125788,
                                    null,
                                    "98104",
                                    "US",
                                    "Seattle",
                                    "WA",
                                    "United States",
                                    null,
                                    null
                                ),
                                listOf(
                                    NetworkCategory(
                                        "4bf58dd8d48988d1e0931735",
                                        "Coffee Shop",
                                        "Coffee Shops",
                                        "Coffee Shop",
                                        Icon(
                                            "https://ss3.4sqi.net/img/categories_v2/food/coffeeshop_",
                                            ".png"
                                        ),
                                        true
                                    )
                                ),
                                verified = true,
                                stats = null,
                                beenHere = null,
                                hereNow = null,
                                referralId = "v-1523913911",
                                venueChains = null,
                                hasPerk = null,
                                venuePage = "https://www.facebook.com/StoryvilleCoffee"
                            ),
                            NetworkVenue(
                                "57e95a82498e0a3995a43e90",
                                "Anchorhead Coffee Co",
                                null,
                                Location(
                                    "1600 7th Ave Ste 105",
                                    "Olive Way",
                                    47.61340942776967,
                                    -122.33469499761385,
                                    null,
                                    "98104",
                                    "US",
                                    "Seattle",
                                    "WA",
                                    "United States",
                                    null,
                                    null
                                ),
                                listOf(
                                    NetworkCategory(
                                        "4bf58dd8d48988d1e0931735",
                                        "Coffee Shop",
                                        "Coffee Shops",
                                        "Coffee Shop",
                                        Icon(
                                            "https://ss3.4sqi.net/img/categories_v2/food/coffeeshop_",
                                            ".png"
                                        ),
                                        true
                                    )
                                ),
                                verified = false,
                                stats = null,
                                beenHere = null,
                                hereNow = null,
                                referralId = "v-1523913911",
                                venueChains = null,
                                hasPerk = null,
                                venuePage = "https://www.facebook.com/StoryvilleCoffee"
                            )
                        ),
                        Geocode(
                            feature = Feature(
                                "US",
                                "Seattle",
                                "Seattle, WA, United States",
                                "Seattle, WA, United States",
                                "<b>Seattle</b>, <b>WA</b>, United States",
                                7,
                                "seattle-washington",
                                "geonameid:5809844",
                                "72057594043737780",
                                Geometry(
                                    Coordinate(47.60621, -122.33207),
                                    Bounds(
                                        Coordinate(47.734145, -122.224433),
                                        Coordinate(47.481719999999996, -122.459696)
                                    )
                                )
                            )
                        )
                    )
                )
            )
                .firstOrError()
        )
    }

    @Test
    fun remote_data_return_error() {
        val testObserver = remoteData.closedVenues(LIMIT, ERROR_QUERY).test()
        testObserver.awaitTerminalEvent()
        testObserver.assertError {
            it == error
        }
    }

    @Test
    fun remote_data_is_not_null_and_without_errors() {
        val testObserver = remoteData.closedVenues(LIMIT, QUERY).test()
        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()
            .assertValue {
                model ->
                model.response != null
            }
    }

    @Test
    fun remote_data_responce_has_venues_list() {
        val testObserver = remoteData.closedVenues(Companion.LIMIT, Companion.QUERY).test()
        testObserver.awaitTerminalEvent()
        testObserver
            .assertValue {
                    model ->
                val list = model.response?.venues
                list != null && list.isNotEmpty()
            }
    }

    @Test
    fun remote_data_responce_has_venues_list_with_correct_data() {
        remoteData.closedVenues(Companion.LIMIT, Companion.QUERY)
            .test()
            .assertNoErrors()
            .assertValue { l ->
                Observable.fromIterable(l.response?.venues)
                    .map {
                        it.name
                    }
                    .toList()
                    .blockingGet() == listOf("Storyville Coffee Company", "Anchorhead Coffee Co")
            }
    }

    companion object {
        private const val QUERY = "coffee"
        private const val ERROR_QUERY = "error"
        private const val LIMIT = 2
    }
}
