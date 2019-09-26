package com.kstor.homeawaytest

import com.kstor.homeawaytest.domain.FavoriteUseCase
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.domain.model.VenuesData
import com.kstor.homeawaytest.view.mainscreen.VenuesListPresenterImpl
import com.kstor.homeawaytest.view.mainscreen.VenuesListView
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
class VenuesListPresenterTest {

    companion object {
        const val TEST_QUERY = ""
    }

    @Mock
    lateinit var useCaseResultWithData: VenuesUseCase


    @Mock
    lateinit var favoritesUseCase: FavoriteUseCase


    @Mock
    lateinit var useCaseResultWithError: VenuesUseCase
    @Mock
    private lateinit var view: VenuesListView

    private lateinit var schedulerProvider: SchedulerProvider

    private lateinit var venuesList: List<Venues>
    private lateinit var error: Throwable

    private lateinit var presenter: VenuesListPresenterImpl
    private lateinit var presenterNoView: VenuesListPresenterImpl
    private lateinit var presenterWithError: VenuesListPresenterImpl
    private lateinit var compositeDisposable: CompositeDisposable

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        compositeDisposable = CompositeDisposable()
        venuesList = listOf(
            Venues("1", "Name", null, "Adress", 0, 0.0, 0.0)
        )
        error = Throwable("Something wrong")
        schedulerProvider = TestSchedulerProvider(Schedulers.trampoline())
        presenter = createBaseTestPresenter()
        presenterNoView = createPresenterWithoutView()
        presenterWithError = createPresenterWithError()

    }

    private fun createPresenterWithoutView(): VenuesListPresenterImpl {
        val goodResult = Observable.just(venuesList)
        `when`(useCaseResultWithData.loadVenuesData(TEST_QUERY)).thenReturn(goodResult)
        presenterNoView = VenuesListPresenterImpl(compositeDisposable, useCaseResultWithData, schedulerProvider, favoritesUseCase)
        return presenterNoView
    }

    private fun createBaseTestPresenter(): VenuesListPresenterImpl {

        val goodResult = Observable.just(venuesList)
        `when`(useCaseResultWithData.loadVenuesData(TEST_QUERY)).thenReturn(goodResult)
        presenter = VenuesListPresenterImpl(compositeDisposable, useCaseResultWithData, schedulerProvider, favoritesUseCase)
        presenter.attachView(view)
        return presenter
    }

    private fun createPresenterWithError(): VenuesListPresenterImpl {
        val bedResult = Observable.error<List<Venues>>(error)
        `when`(useCaseResultWithError.loadVenuesData(TEST_QUERY)).thenReturn(bedResult)
        presenterWithError =
            VenuesListPresenterImpl(compositeDisposable, useCaseResultWithError, schedulerProvider, favoritesUseCase)
        presenterWithError.attachView(view)
        return presenterWithError
    }

    @Test
    fun show_venues_list_after_presenter_call_get_venues() {
        presenter.getVenues(TEST_QUERY)
        verify(view).hideProgress()
        verify(view).displayVenues(venuesList)
        verify(view).showMupButn()
        Mockito.verifyZeroInteractions(view)
    }

    @Test
    fun does_not_show_venues_list_if_view_is_not_attached_to_presenter() {
        presenterNoView.detachView()
        presenterNoView.getVenues(TEST_QUERY)
        verify(view, never()).displayVenues(venuesList)
        Mockito.verifyZeroInteractions(view)
    }

    @Test
    fun show_error_if_use_case_return_error() {
        presenterWithError.getVenues(TEST_QUERY)
        verify(view).showError(error)
        Mockito.verifyZeroInteractions(view)
    }
}
