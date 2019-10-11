package com.kstor.homeawaytest

import com.kstor.homeawaytest.domain.FavoriteUseCase
import com.kstor.homeawaytest.domain.RepoResult
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.domain.model.Venue
import com.kstor.homeawaytest.view.mainscreen.VenuesListPresenterImpl
import com.kstor.homeawaytest.view.mainscreen.VenuesListView
import com.kstor.homeawaytest.view.utils.DispatcherProvider
import com.kstor.homeawaytest.view.utils.TestSchedulerProvider
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
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
    lateinit var errorFavoritesUseCase: FavoriteUseCase

    @Mock
    lateinit var useCaseResultWithError: VenuesUseCase
    @Mock
    private lateinit var view: VenuesListView

    private lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var venuesList: List<Venue>
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
            Venue("1", "Name", null, "Adress", 0, 0.0, 0.0)
        )
        error = Throwable("Something wrong")
        dispatcherProvider = TestSchedulerProvider()
        createBaseTestPresenter()
        createPresenterWithoutView()
        createPresenterWithError()
    }

    private fun createPresenterWithoutView() = runBlocking<Unit> {
        val goodResult = RepoResult.Success(venuesList)
        `when`(useCaseResultWithData.loadVenuesDataFromApi(TEST_QUERY)).thenReturn(goodResult)
        presenterNoView = VenuesListPresenterImpl(
            useCaseResultWithData,
            dispatcherProvider,
            favoritesUseCase
        )
    }

    private fun createBaseTestPresenter() = runBlocking<Unit> {
        val goodResult = RepoResult.Success(venuesList)
        `when`(useCaseResultWithData.loadVenuesDataFromApi(TEST_QUERY)).thenReturn(goodResult)
        `when`(favoritesUseCase.getFavorites()).thenReturn(goodResult)
        presenter = VenuesListPresenterImpl(
            useCaseResultWithData,
            dispatcherProvider,
            favoritesUseCase
        )
        presenter.attachView(view)
    }

    private fun createPresenterWithError() = runBlocking<Unit> {
        val badResult = RepoResult.Error<List<Venue>>(error)
        `when`(useCaseResultWithError.loadVenuesDataFromApi(TEST_QUERY)).thenReturn(badResult)
        `when`(errorFavoritesUseCase.getFavorites()).thenReturn(badResult)
        presenterWithError =
            VenuesListPresenterImpl(
                useCaseResultWithError,
                dispatcherProvider,
                errorFavoritesUseCase
            )
        presenterWithError.attachView(view)

    }

    @Test
    fun show_venues_list_after_presenter_call_get_venues() = runBlocking<Unit> {
        presenter.getVenues(TEST_QUERY)
        verify(view).hideProgress()
        verify(view).hideNoResult()
        verify(view).displayVenues(venuesList)
        verify(view).showMupButn()
        verifyZeroInteractions(view)
    }

    @Test
    fun does_not_show_venues_list_if_view_is_not_attached_to_presenter() = runBlocking<Unit> {
        presenterNoView.detachView()
        presenterNoView.getVenues(TEST_QUERY)
        verify(view, never()).displayVenues(venuesList)
        verifyZeroInteractions(view)
    }

    @Test
    fun show_error_and_hide_progress_if_use_case_return_error() = runBlocking {
        presenterWithError.getVenues(TEST_QUERY)
        verify(view).hideProgress()
        verify(view).showError(error)
        verify(view).displayVenues(emptyList())
        verify(view).showNoResult()
        verify(view).hideMupButn()
        verifyZeroInteractions(view)
    }

    @Test
    fun get_favorites_successes() = runBlocking<Unit> {
        presenter.getFavorites()
        verify(favoritesUseCase).getFavorites()
        verifyZeroInteractions(favoritesUseCase)
        verify(view).hideProgress()
        verify(view).hideNoResult()
        verify(view).displayVenues(venuesList)
        verifyZeroInteractions(view)
    }

    @Test
    fun get_favorites_error() = runBlocking<Unit> {
        presenterWithError.getFavorites()
        verify(errorFavoritesUseCase).getFavorites()
        verifyZeroInteractions(errorFavoritesUseCase)
        verify(view).displayVenues(emptyList())
        verify(view).hideProgress()
        verify(view).showError(error)
        verify(view).showNoResult()
        verifyZeroInteractions(view)
    }
}
