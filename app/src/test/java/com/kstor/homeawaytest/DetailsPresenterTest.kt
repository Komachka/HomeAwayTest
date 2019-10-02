package com.kstor.homeawaytest

import com.kstor.homeawaytest.domain.FavoriteUseCase
import com.kstor.homeawaytest.domain.GenerateStaticMapUrlUseCase
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.view.detailscreen.DetailsPresenter
import com.kstor.homeawaytest.view.detailscreen.DetailsPresenterImpl
import com.kstor.homeawaytest.view.detailscreen.DetailsView
import com.kstor.homeawaytest.view.utils.SchedulerProvider
import com.kstor.homeawaytest.view.utils.TestSchedulerProvider
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailsPresenterTest
{
    @Mock
    lateinit var compositeDisposable: CompositeDisposable
    lateinit var schedulerProvider: SchedulerProvider
    @Mock
    lateinit var staticMapUseCase:GenerateStaticMapUrlUseCase

    @Mock
    lateinit var errorStaticMapUseCase:GenerateStaticMapUrlUseCase
    @Mock
    lateinit var favoriteUseCase:FavoriteUseCase

    @Mock
    lateinit var view: DetailsView

    lateinit var testVenues:  Venues
    lateinit var testError:Throwable
    lateinit var testPresenter: DetailsPresenter
    lateinit var errorPresenter:DetailsPresenter

    @Before
    fun setup()
    {
        MockitoAnnotations.initMocks(this)
        testVenues = createTestVenues()
        testError  = Throwable("This is test error")
        createStaticMapUseCase()
        createErrorStaticMapUseCase()
        schedulerProvider = TestSchedulerProvider(Schedulers.trampoline())
        testPresenter = DetailsPresenterImpl(compositeDisposable, staticMapUseCase, schedulerProvider,  favoriteUseCase)
        (testPresenter as DetailsPresenterImpl).attachView(view)

        errorPresenter = DetailsPresenterImpl(compositeDisposable, errorStaticMapUseCase, schedulerProvider, favoriteUseCase)
        (errorPresenter as DetailsPresenterImpl).attachView(view)

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

    /*
    fun setFavorite(venues: Venues)
    fun addAndRemoveFromFavorites(venues: Venues)
    */

    @Test
    fun show_map_image(){
        testPresenter.createStaticMapUrl(testVenues)
        verify(view).loadMap(TEST_URL)
    }

    @Test
    fun show_error_on_map_loaded(){
        errorPresenter.createStaticMapUrl(testVenues)
        verify(view).showError(testError)
    }


    @Test
    fun set_venues_to_favorite()
    {
        testPresenter.setFavorite(testVenues)
    }

    companion object {
        const val TEST_URL = "URL"
    }
}