package com.kstor.homeawaytest

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kstor.homeawaytest.domain.model.Venues
import com.kstor.homeawaytest.domain.model.VenuesCategory
import com.kstor.homeawaytest.view.mainscreen.*
import com.kstor.homeawaytest.view.utils.VenuesMapper
import org.hamcrest.Matcher

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.w3c.dom.Text

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest: VenuesMapper {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.kstor.homeawaytest", appContext.packageName)
    }


    @Mock
    private lateinit var navController: NavController

    @Mock
    private lateinit var presentor: VenuesListPresenter

    @Before
    fun setup()
    {
        MockitoAnnotations.initMocks(this)
    }



    @Test
    fun launchDetailActivity_by_cliking_on_item() {
        val scenario = launchFragmentInContainer<VenuesListFragment>(Bundle(), R.style.AppTheme)
        //We can associate our new mock with the view's NavController
        val fragmentView:View = mock(View::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }
        val testVenues = Venues(
            "56d249bc498ef524220083f3",
            "Hoffman's House of Horrors",
            VenuesCategory("4bf58dd8d48988d117941735",
                "Beer Garden",
                "https://ss3.4sqi.net/img/categories_v2/nightlife/beergarden_32.png"),
            "7986 Emery Blvd NE",
            28882,
            47.633885804708434,
            -122.71479606628417,
            false
        )
        val venuesParselize = mapToPasrelize(testVenues)!!

        val view = mock(VenuesListView::class.java)
        `when`(presentor.getFavorites()).thenAnswer {
            view.displayVenues(listOf(testVenues))
        }
        val action = object:ViewAction{
            internal var click = ViewActions.click()
            override fun getDescription(): String {
                return "Click on item name recycler"
            }

            override fun getConstraints(): Matcher<View> {
                return click.constraints
            }

            override fun perform(uiController: UiController?, view: View?) {
                val layout = view?.findViewById<ConstraintLayout>(R.id.list_item)
                layout?.performClick()
            }
        }


        onView(withId(R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, action))

        verify(navController).navigate(
            VenuesListFragmentDirections.actionVenuesListFragmentToDetailFragment(venuesParselize)
        )
        onView(ViewMatchers.withText("Hoffman's House of Horrors")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText("7986 Emery Blvd NE")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText("28882 m")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
