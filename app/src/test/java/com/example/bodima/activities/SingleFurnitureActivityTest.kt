package com.example.bodima.activities

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class SingleFurnitureActivityTest {

    private lateinit var scenario: ActivityScenario<SingleFurnitureActivity>

    @Before
    fun setUp() {
        val intent = Intent()
        intent.putExtra("itemId", "123") // replace with an actual item ID from Firebase Realtime Database

        scenario = ActivityScenario.launch(SingleFurnitureActivity::class.java, intent)
    }

    @Test
    fun testUIElements() {
        Espresso.onView(ViewMatchers.withId(R.id.f_singletitle)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.f_singledescription)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.f_singlequantity)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.f_singleaddress)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.f_mobile)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.f_singlefurnitureimage)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.f_butCall)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.f_plus)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.f_minus)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.new_quantity)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.f_total)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testQuantityButtons() {
        Espresso.onView(ViewMatchers.withId(R.id.f_plus)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.new_quantity)).check(ViewAssertions.matches(ViewMatchers.withText("1")))
        Espresso.onView(ViewMatchers.withId(R.id.f_total)).check(ViewAssertions.matches(ViewMatchers.withText("Total :50"))) // replace 50 with an actual price value from Firebase Realtime Database

        Espresso.onView(ViewMatchers.withId(R.id.f_minus)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.new_quantity)).check(ViewAssertions.matches(ViewMatchers.withText("0")))
        Espresso.onView(ViewMatchers.withId(R.id.f_total)).check(ViewAssertions.matches(ViewMatchers.withText("Total :0")))
    }

    // add more tests as needed
}
