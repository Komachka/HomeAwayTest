package com.kstor.homeawaytest

import com.kstor.homeawaytest.domain.FavoriteUseCase
import com.kstor.homeawaytest.domain.GenerateStaticMapUrlUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.detailscreen.DetailsPresenter
import com.kstor.homeawaytest.view.detailscreen.DetailsPresenterImpl
import com.kstor.homeawaytest.view.detailscreen.DetailsView
import com.kstor.homeawaytest.view.utils.FavoriteImageRes
import com.kstor.homeawaytest.view.utils.SchedulerProvider
import com.kstor.homeawaytest.view.utils.TestSchedulerProvider
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.initMocks
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailsPresenterTest {
    @Mock
    lateinit var compositeDisposable: CompositeDisposable
    lateinit var schedulerProvider: SchedulerProvider
    @Mock
    lateinit var staticMapUseCase: GenerateStaticMapUrlUseCase

    @Mock
    lateinit var errorStaticMapUseCase: GenerateStaticMapUrlUseCase

    @Mock
    lateinit var favoriteUseCase: FavoriteUseCase
    @Mock
    lateinit var errorFavoriteUseCase: FavoriteUseCase

    @Mock
    lateinit var view: DetailsView

    lateinit var testVenues: Venues
    lateinit var testError: Throwable
    lateinit var testPresenter: DetailsPresenter
    lateinit var errorPresenter: DetailsPresenter

    @Before
    fun setup() {
        initMocks(this)
        testVenues = createTestVenues()
        testError = Throwable("This is test error")
        createStaticMapUseCase()
        createErrorStaticMapUseCase()
        createFavoriteUseCase()
        createErrorFavoriteUseCase()

        schedulerProvider = TestSchedulerProvider(Schedulers.trampoline())

        testPresenter = DetailsPresenterImpl(compositeDisposable, staticMapUseCase, schedulerProvider, favoriteUseCase)
        (testPresenter as DetailsPresenterImpl).attachView(view)

        errorPresenter = DetailsPresenterImpl(compositeDisposable, errorStaticMapUseCase, schedulerProvider, errorFavoriteUseCase)
        (errorPresenter as DetailsPresenterImpl).attachView(view)
    }

    private fun createErrorFavoriteUseCase() {
        `when`(errorFavoriteUseCase.removeFromFavorite(testVenues)).thenReturn(Completable.error(testError))
        `when`(errorFavoriteUseCase.addToFavorite(testVenues)).thenReturn(Completable.error(testError))
    }

    private fun createFavoriteUseCase() {
        `when`(favoriteUseCase.removeFromFavorite(testVenues)).thenReturn(Completable.fromRunnable { })
        `when`(favoriteUseCase.addToFavorite(testVenues)).thenReturn(Completable.fromRunnable { })
    }

    private fun createTestVenues(): Venues {
        return Venues("1", "Name1", null, "Adress1", 10, 1.0, 2.0, true)
    }

    private fun createStaticMapUseCase() {
        val result = Observable.just(TEST_URL)
        `when`(staticMapUseCase.createStaticMapUrl(testVenues)).thenReturn(result)
    }
    private fun createErrorStaticMapUseCase() {
        val result = Observable.error<String>(testError)
        `when`(errorStaticMapUseCase.createStaticMapUrl(testVenues)).thenReturn(result)
    }

    @Test
    fun show_map_image() {
        testPresenter.createStaticMapUrl(testVenues)
        verify(view).loadMap(TEST_URL)
        verifyZeroInteractions(view)
    }

    @Test
    fun show_error_on_map_loaded() {
        errorPresenter.createStaticMapUrl(testVenues)
        verify(view).showError(testError)
        verifyZeroInteractions(view)
    }

    @Test
    fun change_favorite_venues_image() {
        testVenues.isFavorite = true
        testPresenter.setFavorite(testVenues)
        verify(view).setIfFavorite(FavoriteImageRes.IS_FAVORITE.resId)

        testVenues.isFavorite = false
        testPresenter.setFavorite(testVenues)
        verify(view).setIfFavorite(FavoriteImageRes.IS_NOT_FAVORITE.resId)
        verifyZeroInteractions(view)
    }

    @Test
    fun remove_from_favorites_success() {
        testVenues.isFavorite = true
        (testPresenter as DetailsPresenterImpl).addAndRemoveFromFavorites(testVenues)

        verify(favoriteUseCase).removeFromFavorite(testVenues)
        verify(view).updateItemView(testVenues)
        verifyZeroInteractions(view)
    }

    @Test
    fun add_to_favorites_success() {
        testVenues.isFavorite = false
        (testPresenter as DetailsPresenterImpl).addAndRemoveFromFavorites(testVenues)

        verify(favoriteUseCase).addToFavorite(testVenues)
        verify(view).updateItemView(testVenues)
        verifyZeroInteractions(view)
    }

    @Test
    fun remove_from_favorites_error() {
        testVenues.isFavorite = true
        (errorPresenter as DetailsPresenterImpl).addAndRemoveFromFavorites(testVenues)

        verify(errorFavoriteUseCase).removeFromFavorite(testVenues)
        verify(view).showError(testError)
        verifyZeroInteractions(view)
    }

    @Test
    fun add_to_favorites_error() {
        testVenues.isFavorite = false
        (errorPresenter as DetailsPresenterImpl).addAndRemoveFromFavorites(testVenues)

        verify(errorFavoriteUseCase).addToFavorite(testVenues)
        verify(view).showError(testError)
        verifyZeroInteractions(view)
    }

    companion object {
        const val TEST_URL = "URL"
    }
}
