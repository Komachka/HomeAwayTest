package com.kstor.homeawaytest

import com.kstor.homeawaytest.data.ApiResult
import com.kstor.homeawaytest.data.db.LocalData
import com.kstor.homeawaytest.data.db.model.DBVenuesModel
import com.kstor.homeawaytest.data.network.RemoteData
import com.kstor.homeawaytest.data.network.model.*
import com.kstor.homeawaytest.data.repos.VenuesRepositoryImp
import com.kstor.homeawaytest.data.sp.SharedPreferenceData
import com.kstor.homeawaytest.domain.RepoResult
import com.kstor.homeawaytest.domain.model.Venue
import com.kstor.homeawaytest.domain.model.VenuesCategory
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class VenuesRepositoryTest {
    @Mock
    private lateinit var remoteData: RemoteData

    @Mock
    private lateinit var localData: LocalData

    @Mock
    private lateinit var preferenceData: SharedPreferenceData

    @InjectMocks
    lateinit var repo: VenuesRepositoryImp

    private val QUERY = "coffee"
    private val LIMIT = 2
    private val lat = 47.60621
    private val lng = -122.33207

    @Before
    fun setup() {
        createRepoWithData()
    }

    private fun createRepoWithData() = runBlocking<Unit> {
        val correctData = ApiResult.Succsses(createCorrectData())
        `when`(remoteData.closedVenues(LIMIT, QUERY)).thenReturn(correctData)
        lenient().`when`(preferenceData.setCityCenterInfo(lat, lng)).thenReturn(true)
        `when`(localData.getAllVenues()).thenReturn(getDBVenuesModelList())
        repo = VenuesRepositoryImp(remoteData, preferenceData, localData)
    }

    private fun getDBVenuesModelList() =
        listOf(
            DBVenuesModel(
                "1", "Storyville Coffee Company", "1", "category", "iconPath",
                "address", 100, 5.0, 3.0, true
            ),
            DBVenuesModel(
                "2", "Anchorhead Coffee Co", "2", "category", "iconPath",
                "address", 100, 5.0, 3.0, true
            )
        )

    private fun createCorrectData() =
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
                            Coordinate(lat, lng),
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
    fun repository_return_valid_data() = runBlocking<Unit> {
        val result = repo.getClosestVenuses(LIMIT, QUERY)
        assert((result as RepoResult.Success).data == dataFromDb())
    }

    private fun dataFromDb(): List<Venue> {
        return listOf(
            Venue(
                "1", "Storyville Coffee Company", VenuesCategory("1", "category", "iconPath"),
                "address", 100, 5.0, 3.0, true
            ),
            Venue(
                "2", "Anchorhead Coffee Co", VenuesCategory("2", "category", "iconPath"),
                "address", 100, 5.0, 3.0, true
            )
        )
    }
}
