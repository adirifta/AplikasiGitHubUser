package com.example.aplikasigithubuser.ui.activity

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.aplikasigithubuser.R
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityUITest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        activityScenario.close()
    }

    @Test
    fun testSearchViewDisplayed() {
        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
    }

    @Test
    fun testRecyclerViewDisplayed() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun testSettingsMenuItemDisplayed() {
        onView(withId(R.id.action_settings)).check(matches(isDisplayed()))
    }

    @Test
    fun testFavoriteMenuItemDisplayed() {
        onView(withId(R.id.action_favorite)).check(matches(isDisplayed()))
    }
}