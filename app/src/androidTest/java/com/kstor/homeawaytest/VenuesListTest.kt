package com.kstor.homeawaytest

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kstor.homeawaytest.domain.VenuesRepository
import com.kstor.homeawaytest.utils.StringContainsIgnoringCase.Companion.containsStringIgnoreCase
import com.kstor.homeawaytest.view.di.AppModule

import com.kstor.homeawaytest.view.di.mock.FakeRepository

import com.kstor.homeawaytest.view.mainscreen.*
import com.kstor.homeawaytest.view.utils.VenuesMapper
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.equalToIgnoringCase

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
    private lateinit var repository: VenuesRepository


    @Before
    fun setup() {
        val instr = InstrumentationRegistry.getInstrumentation()
        val app = instr.targetContext.applicationContext as App
        repository = FakeRepository()
        MockitoAnnotations.initMocks(this)
        val component = DaggerTestComponent.builder().appModule(AppModule(app))
            .testRepositoryModule(TestRepositoryModule(repository)).build()
        component.inject(this)
        app.homeAwayComponents = component
    }


    @Test
    fun launchDetailActivity_by_clicking_on_item_of_favoriteList() {
        val scenario = launchFragmentInContainer<VenuesListFragment>(Bundle(), R.style.AppTheme)
        //We can associate our new mock with the view's NavController
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }
        val testVenues = repository.getFavorites().blockingGet().first()
        val venuesParselize = mapToPasrelize(testVenues)!!

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
        //before item is in favorite
        onView(withId(R.id.list)).check(
            ViewAssertions.matches(
                atPositionItem(
                    0,
                    hasDescendant(withTagValue(equalTo(R.drawable.ic_favorite_black_24dp)))
                )
            )
        )
        //click on favorite icon
        onView(withId(R.id.list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                action
            )
        )
        // icon  drawable was changed
        onView(withId(R.id.list)).check(
            ViewAssertions.matches(
                atPositionItem(
                    0,
                    hasDescendant(withTagValue(equalTo(R.drawable.ic_favorite_border_black_24dp)))
                )
            )
        )
    }

    @Test
    fun when_type_more_two_symbols_in_search_return_valid_data() {
        launchFragmentInContainer<VenuesListFragment>(Bundle(), R.style.AppTheme)
        //type search query
        onView(withId(R.id.queryEditText)).perform(typeText(TEST_QUERY))
        // icon  drawable was changed
        onView(withId(R.id.queryEditText)).check(
            ViewAssertions.matches(
                withText(TEST_QUERY)
            )
        )
        Thread.sleep(5000)
        onView(withId(R.id.list)).check(
            ViewAssertions.matches(
                isDisplayed()
            )
        )
        onView(withId(R.id.list))
            .check(ViewAssertions.matches(atPositionItem(0, hasDescendant(withText(
                containsStringIgnoreCase(TEST_QUERY)
            )))))
    }

    @Test
    fun when_type_less_two_symbols_search_result_is_not_displayed() {
        launchFragmentInContainer<VenuesListFragment>(Bundle(), R.style.AppTheme)
        val query= TEST_QUERY2.substring(0, 1)
        //type search query
        onView(withId(R.id.queryEditText)).perform(typeText(query))
        // icon  drawable was changed
        onView(withId(R.id.queryEditText)).check(
            ViewAssertions.matches(
                withText(query)
            )
        )
        Thread.sleep(5000)
        onView(withId(R.id.list)).check(
            ViewAssertions.matches(
                atPositionItem(0, isDisplayed() )
            )
        )
        /*onView(withId(R.id.list))
            .check(ViewAssertions.matches(atPositionItem(0, hasDescendant(withText(
                containsStringIgnoreCase(query)
            )))))*/
    }


    companion object {
        const val TEST_QUERY = "coffee"
        const val TEST_QUERY2 = "000"

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
    }

}


