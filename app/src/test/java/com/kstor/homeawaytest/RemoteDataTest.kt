package com.kstor.homeawaytest

import com.kstor.homeawaytest.data.network.RemoteData
import com.kstor.homeawaytest.data.network.VenuesService
import com.kstor.homeawaytest.data.network.model.NetworkVenuesModel
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RemoteDataTest {

    @Mock
    lateinit var venusService: VenuesService

    @InjectMocks
    lateinit var remoteData: RemoteData

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun emptyTest() {
        remoteData.closedVenues(1, "")
    }

    @Test
    fun anotherTest() {
        val res = Observable.just(NetworkVenuesModel())
        `when`(remoteData.closedVenues(1, ""))
            .thenReturn(res)
        val data = remoteData.closedVenues(1, "").test()
        data.awaitTerminalEvent()
        data.assertNoErrors()
        data.assertValue {
            it.response != null
        }

        /*
        `when`(stackOverflowService.getBadges(anyInt())).thenReturn(
            just(BadgeResponse.create(Badge.create("badge")))
        )*/
    }
}
