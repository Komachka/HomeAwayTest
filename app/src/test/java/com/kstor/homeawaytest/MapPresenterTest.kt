package com.kstor.homeawaytest

import com.google.android.gms.maps.model.LatLng
import com.kstor.homeawaytest.domain.RepoResult
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.domain.model.Venue
import com.kstor.homeawaytest.view.mapscreen.MapPresenterImpl
import com.kstor.homeawaytest.view.mapscreen.MapView
import com.kstor.homeawaytest.view.utils.DispatcherProvider
import com.kstor.homeawaytest.view.utils.TestSchedulerProvider
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapPresenterTest {

    companion object {
        const val TEST_QUERY = ""
    }

    @Mock
    lateinit var useCaseResultWithData: VenuesUseCase
    @Mock
    lateinit var useCaseResultWithError: VenuesUseCase
    @Mock
    private lateinit var view: MapView

    private lateinit var compositeDisposable: CompositeDisposable

    private lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var venuesList: List<Venue>
    private lateinit var venuesMap: Map<LatLng, Venue>
    private lateinit var error: Throwable

    private lateinit var presenter: MapPresenterImpl
    private lateinit var presenterNoView: MapPresenterImpl
    private lateinit var presenterWithError: MapPresenterImpl

    private lateinit var centerLatLng: LatLng

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        compositeDisposable = CompositeDisposable()
        venuesList = listOf(
            Venue("1", "Name1", null, "Adress1", 10, 1.0, 2.0),
            Venue("2", "Name2", null, "Adress2", 20, 2.0, 3.0)
        )

        centerLatLng = LatLng(0.0, 0.0)
        venuesMap = mapOf(
            LatLng(venuesList.first().lat!!, venuesList.first().lng!!) to venuesList.first(),
            LatLng(venuesList.last().lat!!, venuesList.last().lng!!) to venuesList.last()
        )
        error = Throwable("Something wrong")
        dispatcherProvider = TestSchedulerProvider()
        createBaseTestPresenter()
        createPresenterWithoutView()
        createPresenterWithError()
    }

    private fun createPresenterWithoutView() = runBlocking<Unit> {
        val goodResult = venuesList
        `when`(useCaseResultWithData.loadVenuesCache()).thenReturn(RepoResult.Success(goodResult))
        presenterNoView = MapPresenterImpl(useCaseResultWithData, dispatcherProvider)
    }

    private fun createBaseTestPresenter() = runBlocking<Unit> {
        val goodResult = venuesList
        `when`(useCaseResultWithData.loadVenuesCache()).thenReturn(RepoResult.Success(goodResult))

        `when`(useCaseResultWithData.getCityCenter()).thenReturn(RepoResult.Success(0.0F to 0.0F))
        presenter = MapPresenterImpl(useCaseResultWithData, dispatcherProvider)
        presenter.attachView(view)
    }

    private fun createPresenterWithError() = runBlocking<Unit> {
        val bedResult = RepoResult.Error<List<Venue>>(error)
        `when`(useCaseResultWithError.loadVenuesCache()).thenReturn(bedResult)
        presenterWithError =
            MapPresenterImpl(useCaseResultWithError, dispatcherProvider)
        presenterWithError.attachView(view)
    }

    @Test
    fun show_venues_on_the_map_after_presenter_call_get_venues() = runBlocking<Unit> {
        presenter.getVenues(TEST_QUERY)
        verify(view).showVenuesOnTheMap(venuesMap)
        Mockito.verifyZeroInteractions(view)
    }

    @Test
    fun does_not_show_venues_on_map_if_view_is_not_attached_to_presenter() = runBlocking<Unit> {
        presenterNoView.detachView()
        presenterNoView.getVenues(TEST_QUERY)
        verify(view, never()).showVenuesOnTheMap(venuesMap)
        Mockito.verifyZeroInteractions(view)
    }

    @Test
    fun show_error_if_use_case_return_error() = runBlocking<Unit> {
        presenterWithError.getVenues(TEST_QUERY)
        verify(view).showError(error)
        Mockito.verifyZeroInteractions(view)
    }

    @Test
    fun show__city_center() = runBlocking<Unit> {
        presenter.setUpMapToCityCenter()
        verify(view).showCenterOnTheMap(centerLatLng)
        Mockito.verifyZeroInteractions(view)
    }
}
