package com.kstor.homeawaytest
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kstor.homeawaytest.di.DaggerTestComponent
import com.kstor.homeawaytest.di.TestStaticMapRepositoryModule
import com.kstor.homeawaytest.di.TestVenuesRepositoryModule
import com.kstor.homeawaytest.domain.StaticMapRepository
import com.kstor.homeawaytest.domain.VenuesRepository
import com.kstor.homeawaytest.fake.FakeStaticMapRepository
import com.kstor.homeawaytest.view.detailscreen.DetailFragment
import com.kstor.homeawaytest.view.di.AppModule
import com.kstor.homeawaytest.view.di.mock.FakeVenuesRepository
import com.kstor.homeawaytest.view.utils.VenuesMapper
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class DetailsTest : VenuesMapper {

    private lateinit var venuesRepository: VenuesRepository
    private lateinit var staticMapRepository: StaticMapRepository

    @Before
    fun setup() {
        val instr = InstrumentationRegistry.getInstrumentation()
        val app = instr.targetContext.applicationContext as App
        venuesRepository = FakeVenuesRepository()
        staticMapRepository = FakeStaticMapRepository()
        MockitoAnnotations.initMocks(this)
        val component = DaggerTestComponent.builder().appModule(AppModule(app))
            .testVenuesRepositoryModule(
                TestVenuesRepositoryModule(
                    venuesRepository
                )
            )
            .testStaticMapRepositoryModule(TestStaticMapRepositoryModule(staticMapRepository))
            .build()
        component.inject(this)
        app.homeAwayComponents = component
    }


    @Test
    fun show_detail_screen_for_venues() {
        val venues = venuesRepository.getFavorites().blockingGet().first()
        val parselize = mapToPasrelize(venues)
        val bundle = Bundle().apply {
            putParcelable("venues", parselize)
        }
        launchFragmentInContainer<DetailFragment>(bundle, R.style.AppTheme)
        onView(ViewMatchers.withId(R.id.venuesNameNameTextView)).check(matches(ViewMatchers.withText(venues.name)))
        onView(ViewMatchers.withId(R.id.venuesNameAdressTextView)).check(matches(ViewMatchers.withText(venues.address)))
        onView(ViewMatchers.withId(R.id.venuesDistanceFromCenterTextView)).check(matches(ViewMatchers.withText("${venues.distance} m")))
        onView(ViewMatchers.withId(R.id.venuesCategory)).check(matches(ViewMatchers.withText(venues.categories?.name)))
        onView(ViewMatchers.withId(R.id.venuesPlaceImgView)).check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.mapIv)).check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.fabFavorite)).check(
            matches(ViewMatchers.withTagValue(CoreMatchers.equalTo(R.drawable.ic_favorite_black_24dp))))
    }

    @Test
    fun change_favorite_icon_onClick() {
        val venues = venuesRepository.getFavorites().blockingGet().first()
        val parselize = mapToPasrelize(venues)
        val bundle = Bundle().apply {
            putParcelable("venues", parselize)
        }
        launchFragmentInContainer<DetailFragment>(bundle, R.style.AppTheme)


        onView(ViewMatchers.withId(R.id.fabFavorite)).perform(click())
        onView(ViewMatchers.withId(R.id.fabFavorite)).check(
            matches(ViewMatchers.withTagValue(CoreMatchers.equalTo(R.drawable.ic_favorite_border_black_24dp))))
    }
}