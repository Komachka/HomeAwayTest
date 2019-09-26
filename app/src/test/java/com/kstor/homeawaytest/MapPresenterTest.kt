package com.kstor.homeawaytest

import com.google.android.gms.maps.model.LatLng
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.domain.model.VenuesData
import com.kstor.homeawaytest.view.mapscreen.MapPresenterImpl
import com.kstor.homeawaytest.view.mapscreen.MapView
import com.kstor.homeawaytest.view.utils.SchedulerProvider
import com.kstor.homeawaytest.view.utils.TestSchedulerProvider
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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

    private lateinit var compositeDisposable:CompositeDisposable

    private lateinit var schedulerProvider: SchedulerProvider


    private lateinit var venuesList: List<Venues>
    private lateinit var venuesMap: Map<LatLng, Venues>
    private lateinit var error: Throwable

    private lateinit var presenter: MapPresenterImpl
    private lateinit var presenterNoView: MapPresenterImpl
    private lateinit var presenterWithError: MapPresenterImpl

    private lateinit var centerLatLng:LatLng

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        compositeDisposable = CompositeDisposable()
        venuesList = listOf(
            Venues("1", "Name1", null, "Adress1", 10, 1.0, 2.0),
            Venues("2", "Name2", null, "Adress2", 20, 2.0, 3.0)
        )

        centerLatLng = LatLng(0.0, 0.0)
        venuesMap = mapOf(
            LatLng(venuesList.first().lat!!, venuesList.first().lng!!) to venuesList.first(),
            LatLng(venuesList.last().lat!!, venuesList.last().lng!!) to venuesList.last()
        )
        error = Throwable("Something wrong")
        schedulerProvider = TestSchedulerProvider(Schedulers.trampoline())
        presenter = createBaseTestPresenter()
        presenterNoView = createPresenterWithoutView()
        presenterWithError = createPresenterWithError()

    }

    private fun createPresenterWithoutView(): MapPresenterImpl {
        val goodResult = Observable.just(venuesList)
        `when`(useCaseResultWithData.loadVenuesData(TEST_QUERY)).thenReturn(goodResult)
        presenterNoView = MapPresenterImpl(compositeDisposable, useCaseResultWithData, schedulerProvider)
        return presenterNoView
    }

    private fun createBaseTestPresenter(): MapPresenterImpl {
        val goodResult = Observable.just(venuesList)
        `when`(useCaseResultWithData.loadVenuesData(TEST_QUERY)).thenReturn(goodResult)
        presenter = MapPresenterImpl(compositeDisposable, useCaseResultWithData, schedulerProvider)
        presenter.attachView(view)
        return presenter
    }

    private fun createPresenterWithError(): MapPresenterImpl {
        val bedResult = Observable.error<List<Venues>>(error)
        `when`(useCaseResultWithError.loadVenuesData(TEST_QUERY)).thenReturn(bedResult)
        presenterWithError =
            MapPresenterImpl(compositeDisposable, useCaseResultWithError, schedulerProvider)
        presenterWithError.attachView(view)
        return presenterWithError
    }

    @Test
    fun show_venues_on_the_map_and_city_center_after_presenter_call_get_venues() {
        presenter.getVenues(TEST_QUERY)
        verify(view).showCenterOnTheMap(centerLatLng)
        verify(view).showVenuesOnTheMap(venuesMap)
        Mockito.verifyZeroInteractions(view)
    }

    @Test
    fun does_not_show_venues_on_map_if_view_is_not_attached_to_presenter() {
        presenterNoView.detachView()
        presenterNoView.getVenues(TEST_QUERY)
        verify(view, never()).showVenuesOnTheMap(venuesMap)
        Mockito.verifyZeroInteractions(view)
    }

    @Test
    fun show_error_if_use_case_return_error() {
        presenterWithError.getVenues(TEST_QUERY)
        verify(view).showError(error)
        Mockito.verifyZeroInteractions(view)
    }
}
