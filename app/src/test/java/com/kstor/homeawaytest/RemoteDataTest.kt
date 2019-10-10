package com.kstor.homeawaytest

import com.kstor.homeawaytest.data.*
import com.kstor.homeawaytest.data.network.RemoteData
import com.kstor.homeawaytest.data.network.VenuesService
import com.kstor.homeawaytest.data.network.model.*
import io.reactivex.Observable
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class RemoteDataTest {

    @Mock
    lateinit var venuesService: VenuesService


    private suspend fun failedSearch() =
        venuesService.getVenusesNetworkData(CLIENT_ID, CLIENT_SECRET, NEAR, ERROR_QUERY, V, LIMIT)


    private suspend fun makeSearch() =
        venuesService.getVenusesNetworkData(
            CLIENT_ID,
            CLIENT_SECRET,
            NEAR,
            QUERY,
            V,
            LIMIT
        )

    private lateinit var error: Throwable

    @InjectMocks
    lateinit var remoteData: RemoteData

    @Before
    fun setup() {
        error = Throwable("Some Error occurred")
        mockGetVenuesNetworkData()
        mockGetVenuesErrorData()
    }

    private fun mockGetVenuesErrorData() = runBlocking<Unit> {
        `when`(failedSearch()).thenReturn(
            retrofit2.Response.error(403, ResponseBody.create(
                "application/json".toMediaTypeOrNull(),
                "{\"error\":[\"error message\"]}"
            ))
        )
    }

    private fun mockGetVenuesNetworkData() = runBlocking {
        `when`(makeSearch()).thenReturn(
            retrofit2.Response.success(createMockNetworkVenuesModel())
        )
    }

    private fun createMockNetworkVenuesModel() =
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

    @Test
    fun remote_data_return_error() = runBlocking<Unit> {
        val apiResult = remoteData.closedVenues(LIMIT, ERROR_QUERY)
        assert(apiResult is ApiResult.Error<*>)
        assert(

            (apiResult as ApiResult.Error<*>).throwable is IOException
        )
    }

    @Test
    fun remote_data_is_not_null_and_without_errors() = runBlocking {
        val apiResult = remoteData.closedVenues(LIMIT, QUERY)
        assert(apiResult is ApiResult.Succsses)
        assert(
            (apiResult as ApiResult.Succsses).data.let {model ->
                model.response != null
            }
        )
    }

    @Test
    fun remote_data_response_has_venues_list() = runBlocking<Unit> {
        remoteData.closedVenues(Companion.LIMIT, Companion.QUERY).let {apiResult ->
        assert(apiResult is ApiResult.Succsses)
            val response =(apiResult as ApiResult.Succsses).data.response
            assert(
                response != null
            )
            assert(response?.venues != null)
            assert(response?.venues?.isNotEmpty() ?: false)
        }
    }

    @Test
    fun remote_data_response_has_venues_list_with_correct_data() = runBlocking {
        remoteData.closedVenues(LIMIT, QUERY).let {apiResult ->
            assert(apiResult is ApiResult.Succsses)
            val response =(apiResult as ApiResult.Succsses).data.response
            assert(
                response != null
            )
            assert(Observable.fromIterable(response?.venues)
                .map {
                    it.name
                }
                .toList()
                .blockingGet() == listOf("Storyville Coffee Company", "Anchorhead Coffee Co"))
        }
    }

    companion object {
        private const val QUERY = "coffee"
        private const val ERROR_QUERY = "error"
        private const val LIMIT = 2
    }
}


