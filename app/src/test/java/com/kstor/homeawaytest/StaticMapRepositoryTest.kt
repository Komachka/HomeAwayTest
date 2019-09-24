package com.kstor.homeawaytest

import com.kstor.homeawaytest.data.repos.StaticMapRepositoryImpl
import com.kstor.homeawaytest.data.sp.SharedPreferenceData
import com.kstor.homeawaytest.domain.model.VenuesCategoryParcelize
import com.kstor.homeawaytest.domain.model.VenuesParcelize
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StaticMapRepositoryTest {
    @Mock
    private lateinit var preferenceData: SharedPreferenceData

    @InjectMocks
    lateinit var repo: StaticMapRepositoryImpl

    private val lat = 50.0F
    private val lng = 40.0F
    private lateinit var venues: VenuesParcelize

    @Before
    fun setup() {
         venues = VenuesParcelize(
            id = "1",
            name = "Some Caffe",
            categories = listOf(VenuesCategoryParcelize("1", "Coffe", "imagePath")),
            address = "some adress",
            distance = 10,
            lat = lat.toDouble(),
            lng = lng.toDouble(),
            isFavorite = true
        )
        repo = createRepo()
    }

    private fun createRepo(): StaticMapRepositoryImpl {
        Mockito.lenient().`when`(preferenceData.getCityCenterInfo()).thenReturn(lat to lng)
        return StaticMapRepositoryImpl(preferenceData)
    }

    @Test
    fun repository_return_not_empty_url() {
        repo.createStaticMapUrl(venues).test()
            .assertNoErrors()
            .assertValue { url ->
                print(url)
                url.isNotEmpty()
            }
    }
}
