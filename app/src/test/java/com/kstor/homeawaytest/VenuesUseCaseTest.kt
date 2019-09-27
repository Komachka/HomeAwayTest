package com.kstor.homeawaytest

import com.kstor.homeawaytest.data.LOAD_LIMIT
import com.kstor.homeawaytest.domain.VenuesRepository
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.domain.model.VenuesCategory
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class VenuesUseCaseTest {

    lateinit var useCase: VenuesUseCase

    companion object {
        private const val QUERY = "coffee"
        private const val ERROR = "error"
        private const val lat = 47.60621
        private const val lng = -122.33207
    }

    @Mock
    lateinit var repository: VenuesRepository
    private val error = Throwable("Some Error occurred")
    @Before
    fun setup() {

        `when`(repository.getClosestVenuses(LOAD_LIMIT, QUERY)).thenReturn(
            createSingleWithCorrectData()
        )

        `when`(repository.getClosestVenuses(LOAD_LIMIT, ERROR)).thenReturn(
            createSingleWithError()
        )
        useCase = VenuesUseCase(repository)
    }

    private fun createSingleWithError(): Observable<List<Venues>> {
        return Observable.error<List<Venues>>(error)
    }

    private fun createSingleWithCorrectData(): Observable<List<Venues>> {
        return Observable.just(
            listOf<Venues>(
                Venues(
                    "52d456c811d24128cdd7bc8b",
                    "Storyville Coffee Company",

                    VenuesCategory(
                        "4bf58dd8d48988d1e0931735",
                        "Coffee Shop",
                        "https://ss3.4sqi.net/img/categories_v2/food/coffeeshop_.png"

                    ),
                    "1001 1st Ave",
                    100,
                    47.60475923205166,
                    -122.33636210125788
                ),
                Venues(
                    "57e95a82498e0a3995a43e90",
                    "Anchorhead Coffee Co",

                    VenuesCategory(
                        "4bf58dd8d48988d1e0931735",
                        "Coffee Shop",
                        "https://ss3.4sqi.net/img/categories_v2/food/coffeeshop_.png"

                    ),
                    "1600 7th Ave Ste 105",
                    200,
                    47.61340942776967,
                    -122.33469499761385

                )
            ))
    }

    @Test
    fun on_load_venues_data_get_valid_venues_data() {
        useCase.loadVenuesDataFromApi(QUERY).test()
            .assertNoErrors()
            .assertValue {
                it.isNotEmpty()
            }
    }

    @Test
    fun on_load_venues_data_get_error() {
        useCase.loadVenuesDataFromApi(ERROR).test()
            .assertError {
                it == error
            }
    }
}
