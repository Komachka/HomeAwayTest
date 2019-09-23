package com.kstor.homeawaytest

import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.domain.model.VenuesData
import com.kstor.homeawaytest.view.mainscreen.VenuesListPresenterImpl
import com.kstor.homeawaytest.view.mainscreen.VenuesListView
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class VenuesListPresentorTest {

    companion object {
        const val TEST_QUERY = ""
    }

    @Mock
    lateinit var useCaseResultWithData: VenuesUseCase
    @Mock
    lateinit var useCaseResultWithError: VenuesUseCase
    @Mock
    private lateinit var view: VenuesListView

    private lateinit var ioScheduler: Scheduler
    private lateinit var mainScheduler: Scheduler

    private lateinit var venuesList: List<Venues>
    private lateinit var error: Throwable

    private lateinit var presenter: VenuesListPresenterImpl
    private lateinit var presenterNoView: VenuesListPresenterImpl
    private lateinit var presenterWithError: VenuesListPresenterImpl

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        venuesList = listOf(
            Venues("1", "Name", null, "Adress", 0, 0.0, 0.0)
        )
        error = Throwable("Something wrong")
        mainScheduler = Schedulers.trampoline()
        ioScheduler = Schedulers.trampoline()
        presenter = createBaseTestPresenter()
        presenterNoView = createPresenterWithoutView()
        presenterWithError = createPresenterWithError()
    }

    private fun createPresenterWithoutView(): VenuesListPresenterImpl {
        val data = VenuesData(venuesList, 0.0, 0.0)
        val goodResult = Observable.just(data)
        `when`(useCaseResultWithData.loadVenuesData(TEST_QUERY)).thenReturn(goodResult)
        presenterNoView = VenuesListPresenterImpl(useCaseResultWithData, ioScheduler, mainScheduler)
        return presenter
    }

    private fun createBaseTestPresenter(): VenuesListPresenterImpl {
        val data = VenuesData(venuesList, 0.0, 0.0)
        val goodResult = Observable.just(data)
        `when`(useCaseResultWithData.loadVenuesData(TEST_QUERY)).thenReturn(goodResult)
        presenter = VenuesListPresenterImpl(useCaseResultWithData, ioScheduler, mainScheduler)
        presenter.attachView(view)
        return presenter
    }

    private fun createPresenterWithError(): VenuesListPresenterImpl {
        val badResult = Observable.error<VenuesData>(error)
        `when`(useCaseResultWithError.loadVenuesData(TEST_QUERY)).thenReturn(badResult)
        presenterWithError =
            VenuesListPresenterImpl(useCaseResultWithError, ioScheduler, mainScheduler)
        presenterWithError.attachView(view)
        return presenterWithError
    }

    @Test
    fun show_venues_list_after_presenter_call_get_venues() {
        presenter.getVenues(TEST_QUERY)
        verify(view).hideProgress()
        verify(view).displayVenues(venuesList)
    }

    @Test
    fun does_not_show_venues_list_if_view_is_not_attached_to_presentor() {
        presenterNoView.detachView()
        presenterNoView.getVenues(TEST_QUERY)
        verify(view, never()).displayVenues(venuesList)
    }

    @Test
    fun show_error_if_use_case_return_error() {
        presenterWithError.getVenues(TEST_QUERY)
        verify(view).showError(error)
    }
}
