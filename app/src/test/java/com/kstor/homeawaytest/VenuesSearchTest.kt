package com.kstor.homeawaytest

import com.kstor.homeawaytest.domain.VenuesRepository
import com.kstor.homeawaytest.domain.VenuesUseCase
import com.kstor.homeawaytest.view.mainscreen.VenuesListPresenterImpl
import com.kstor.homeawaytest.view.mainscreen.VenuesListView
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class VenuesSearchTest {

    private lateinit var useCase: VenuesUseCase
    @Mock private lateinit var venuesRepository: VenuesRepository
    private lateinit var ioScheduler: Scheduler
    @Mock private lateinit var mainScheduler: Scheduler
    private lateinit var presenter: VenuesListPresenterImpl
    private lateinit var view: VenuesListView

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        useCase = VenuesUseCase(venuesRepository)
        ioScheduler = Schedulers.io()

        presenter = VenuesListPresenterImpl(useCase, ioScheduler, mainScheduler)
        view = mock()
    }

    @Test
    fun search_progress_not_started_query_less_two_symbols() {
        presenter.getVenues("")
        verify(view, never()).showProgress()
    }
}
