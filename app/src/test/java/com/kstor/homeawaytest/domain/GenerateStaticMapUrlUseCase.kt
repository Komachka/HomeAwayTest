package com.kstor.homeawaytest.domain

import com.kstor.homeawaytest.data.repos.StaticMapRepositoryImpl
import com.kstor.homeawaytest.data.sp.SharedPreferenceData
import com.kstor.homeawaytest.domain.model.VenuesParcelize
import com.kstor.homeawaytest.view.detailscreen.DetailsView
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito

class GenerateStaticMapUrlUseCaseTest {

    lateinit var generateStaticMapUrlUseCase: GenerateStaticMapUrlUseCase

    @Before
    fun setUp() {

        val prefData: SharedPreferenceData = mock()
        val repository = StaticMapRepositoryImpl(prefData)
        Mockito.`when`(prefData.getCityCenterInfo()).thenReturn(0.0F to 0.0F)
        generateStaticMapUrlUseCase = GenerateStaticMapUrlUseCase(repository)
    }

    @Test
    fun putNullToCreateStaticMapUrl() {
        val venues = VenuesParcelize(
            null,
            null,
            null,
            null,
            0, 0.0, 0.0)
        val res = generateStaticMapUrlUseCase.createStaticMapUrl(venues)
        val view: DetailsView = mock()
        verify(view).loadMap(anyString())
    }
}
