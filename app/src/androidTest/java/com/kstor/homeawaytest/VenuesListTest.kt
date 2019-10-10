package com.kstor.homeawaytest

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kstor.homeawaytest.di.DaggerTestComponent
import com.kstor.homeawaytest.di.TestDetailsRepositoryModule
import com.kstor.homeawaytest.di.TestStaticMapRepositoryModule
import com.kstor.homeawaytest.di.TestVenuesRepositoryModule
import com.kstor.homeawaytest.domain.StaticMapRepository
import com.kstor.homeawaytest.domain.VenueDetailsRepository
import com.kstor.homeawaytest.domain.VenuesRepository
import com.kstor.homeawaytest.fake.FakeDetailsRepository
import com.kstor.homeawaytest.fake.FakeStaticMapRepository
import com.kstor.homeawaytest.utils.StringContainsIgnoringCase.Companion.containsStringIgnoreCase
import com.kstor.homeawaytest.view.di.AppModule
import com.kstor.homeawaytest.view.di.mock.FakeVenuesRepository
import com.kstor.homeawaytest.view.mainscreen.*
import com.kstor.homeawaytest.view.utils.FavoriteImageRes
import com.kstor.homeawaytest.view.utils.VenuesMapper
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class VenuesListTest : VenuesMapper {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.kstor.homeawaytest", appContext.packageName)
    }

    @Mock
    private lateinit var navController: NavController
    private lateinit var venuesRepository: VenuesRepository
    private lateinit var staticMapRepository: StaticMapRepository
    private lateinit var detailRepository: VenueDetailsRepository

    @Before
    fun setup() {
        val instr = InstrumentationRegistry.getInstrumentation()
        val app = instr.targetContext.applicationContext as App
        venuesRepository = FakeVenuesRepository()
        staticMapRepository = FakeStaticMapRepository()
        detailRepository = FakeDetailsRepository()
        MockitoAnnotations.initMocks(this)
        val component = DaggerTestComponent.builder().appModule(AppModule(app))
            .testVenuesRepositoryModule(
                TestVenuesRepositoryModule(
                    venuesRepository
                )
            )
            .testDetailsRepositoryModule(TestDetailsRepositoryModule(detailRepository))
            .testStaticMapRepositoryModule(TestStaticMapRepositoryModule(staticMapRepository))
            .build()
        component.inject(this)
        app.homeAwayComponents = component
    }

    @Test
    fun navigate_to_details_fragment_by_clicking_on_item_from_favorite_list() {
        val scenario = launchFragmentInContainer<VenuesListFragment>(Bundle(), R.style.AppTheme)
        // We can associate our new mock with the view's NavController
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }
        val testVenues = venuesRepository.getFavorites().blockingGet().first()
        val venuesParselize = mapToParcelize(testVenues)!!

        onView(withId(R.id.list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        verify(navController).navigate(
            VenuesListFragmentDirections.actionVenuesListFragmentToDetailFragment(venuesParselize)
        )

        onView(withText(testVenues.name)).check(ViewAssertions.matches(isDisplayed()))
        onView(withText(testVenues.address)).check(ViewAssertions.matches(isDisplayed()))
        onView(withText("${testVenues.distance} m")).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun click_on_favorite_icon_to_remove_item_from_favorites() {
        launchFragmentInContainer<VenuesListFragment>(Bundle(), R.style.AppTheme)
        val action = object : ViewAction {
            internal var click = ViewActions.click()
            override fun getDescription(): String {
                return "Click on item name recycler"
            }

            override fun getConstraints(): Matcher<View> {
                return click.constraints
            }

            override fun perform(uiController: UiController?, view: View?) {
                val layout = view?.findViewById<ConstraintLayout>(R.id.list_item)
                layout?.findViewById<ImageButton>(R.id.imageFavorite)?.performClick()
            }
        }
        // before item is in favorite
        onView(withId(R.id.list)).check(
            ViewAssertions.matches(
                atPositionItem(
                    0,
                    hasDescendant(withTagValue(equalTo(FavoriteImageRes.IS_FAVORITE.resId)))
                )
            )
        )
        // click on favorite icon
        onView(withId(R.id.list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                action
            )
        )
        // remove from recycler
        Thread.sleep(3000)
        onView(withId(R.id.list)).check(
            ViewAssertions.matches(
                atPositionItem(
                    0,
                    not(isDisplayed())
                )
            )
        )
    }

    @Test
    fun type_query_in_search_return_valid_data() {
        launchFragmentInContainer<VenuesListFragment>(Bundle(), R.style.AppTheme)
        // type search query
        onView(withId(R.id.queryEditText)).perform(typeText(TEST_QUERY))
        onView(withId(R.id.queryEditText)).check(
            ViewAssertions.matches(
                withText(TEST_QUERY)
            )
        )
        Thread.sleep(3000)
        // recycler is visible
        onView(withId(R.id.list)).check(
            ViewAssertions.matches(
                isDisplayed()
            )
        )
        // recycler has item that contains query
        onView(withId(R.id.list))
            .check(
                ViewAssertions.matches(
                    atPositionItem(
                        0, hasDescendant(
                            withText(
                                containsStringIgnoreCase(TEST_QUERY)
                            )
                        )
                    )
                )
            )
        // map button is displayed
        onView(withId(R.id.fab)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun typed_query_without_return_data_search_result_is_not_displayed() {
        launchFragmentInContainer<VenuesListFragment>(Bundle(), R.style.AppTheme)
        val query = TEST_QUERY2.substring(0, 3)
        // type search query
        onView(withId(R.id.queryEditText)).perform(typeText(query))

        onView(withId(R.id.queryEditText)).check(
            ViewAssertions.matches(
                withText(query)
            )
        )
        Thread.sleep(3000)
        // recycler has not item that contains query
        onView(withId(R.id.list)).check(
            ViewAssertions.matches(
                atPositionItem(0, not(isDisplayed()))
            )
        )
        // map button is not displayed
        onView(withId(R.id.fab)).check(ViewAssertions.matches(not(isDisplayed())))
    }

    @Test
    fun navigate_to_map_fragment_by_clicking_on_fab_button() {
        val scenario = launchFragmentInContainer<VenuesListFragment>(Bundle(), R.style.AppTheme)
        // We can associate our new mock with the view's NavController
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // search with test query
        onView(withId(R.id.queryEditText)).perform(typeText(TEST_QUERY))

        onView(withId(R.id.queryEditText)).check(
            ViewAssertions.matches(
                withText(TEST_QUERY)
            )
        )
        Thread.sleep(3000)
        // map button is  displayed
        onView(withId(R.id.fab)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.fab)).perform(click())
        verify(navController).navigate(
            VenuesListFragmentDirections.actionVenuesListFragmentToMapFragment(TEST_QUERY)
        )
    }

    @Test
    fun add_to_favorite_displayed_in_favorite_list() {
        launchFragmentInContainer<VenuesListFragment>(Bundle(), R.style.AppTheme)
        // type search query
        onView(withId(R.id.queryEditText)).perform(typeText(TEST_QUERY_FOR_NOT_FAVORITE_ITEM))
        onView(withId(R.id.queryEditText)).check(
            ViewAssertions.matches(
                withText(TEST_QUERY_FOR_NOT_FAVORITE_ITEM)
            )
        )
        Thread.sleep(3000)
        // recycler is visible
        onView(withId(R.id.list)).check(
            ViewAssertions.matches(
                isDisplayed()
            )
        )
        // recycler has item that contains query
        onView(withId(R.id.list))
            .check(
                ViewAssertions.matches(
                    atPositionItem(
                        0, hasDescendant(
                            withText(
                                containsStringIgnoreCase(TEST_QUERY_FOR_NOT_FAVORITE_ITEM)
                            )
                        )
                    )
                )
            )
        // map button is displayed
        onView(withId(R.id.fab)).check(ViewAssertions.matches(isDisplayed()))

        val action = object : ViewAction {
            internal var click = ViewActions.click()
            override fun getDescription(): String {
                return "Click on item name recycler"
            }

            override fun getConstraints(): Matcher<View> {
                return click.constraints
            }

            override fun perform(uiController: UiController?, view: View?) {
                val layout = view?.findViewById<ConstraintLayout>(R.id.list_item)
                layout?.findViewById<ImageButton>(R.id.imageFavorite)?.performClick()
            }
        }
        // before item is in favorite
        onView(withId(R.id.list)).check(
            ViewAssertions.matches(
                atPositionItem(
                    TEST_ITEM_FAVORITE,
                    hasDescendant(withTagValue(equalTo(FavoriteImageRes.IS_NOT_FAVORITE.resId)))
                )
            )
        )
        // click on favorite icon
        onView(withId(R.id.list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                TEST_ITEM_FAVORITE,
                action
            )
        )
        // icon  drawable was changed
        onView(withId(R.id.list)).check(
            ViewAssertions.matches(
                atPositionItem(
                    0,
                    hasDescendant(withTagValue(equalTo(FavoriteImageRes.IS_FAVORITE.resId)))
                )
            )
        )
        onView(withId(R.id.queryEditText)).perform(replaceText(""))
        Thread.sleep(3000)
        onView(withId(R.id.list)).check(
            ViewAssertions.matches(
                atPatternItem(
                    TEST_QUERY_FOR_NOT_FAVORITE_ITEM,
                    hasDescendant(withTagValue(equalTo(FavoriteImageRes.IS_FAVORITE.resId)))
                )
            )
        )
    }

    companion object {
        const val TEST_QUERY = "coffee"
        const val TEST_QUERY_FOR_NOT_FAVORITE_ITEM = "zoo"

        const val TEST_QUERY2 = "000"
        const val TEST_ITEM_FAVORITE = 0

        fun atPositionItem(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
            return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
                override fun describeTo(description: Description?) {
                    description?.appendText("has item at position $position: ")
                    itemMatcher.describeTo(description)
                }

                override fun matchesSafely(item: RecyclerView?): Boolean {
                    val viewHolder: RecyclerView.ViewHolder? =
                        item?.findViewHolderForAdapterPosition(position)
                    val recyclerItem = viewHolder?.itemView
                    return itemMatcher.matches(recyclerItem)
                }
            }
        }

        fun atPatternItem(pattern: String, itemMatcher: Matcher<View>): Matcher<View> {
            return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
                override fun describeTo(description: Description?) {
                    description?.appendText("has item with text $pattern: ")
                    itemMatcher.describeTo(description)
                }

                override fun matchesSafely(item: RecyclerView?): Boolean {
                    val data = (item?.adapter as VenuesListAdapter).venues
                    if (data.isEmpty()) return false
                    var position = 0
                    for (i in 0 until data.size) {
                        if (data[i].name?.toLowerCase()?.contains(pattern.toLowerCase()) == true) {
                            position = i
                        }
                    }
                    val viewHolder: RecyclerView.ViewHolder? =
                        item?.findViewHolderForAdapterPosition(position)
                    val recyclerItem = viewHolder?.itemView
                    return itemMatcher.matches(recyclerItem)
                }
            }
        }
    }
}
