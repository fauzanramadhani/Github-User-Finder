package com.fgr.githubuserfinder

import androidx.annotation.DrawableRes
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {


    @Before
    fun setup() {
        ActivityScenario.launch((MainActivity::class.java))
    }

    @Test
    fun testMenuIconIsDisplayed() {
        onView(withId(R.id.light)).check(matches(isDisplayed()))
        onView(withId(R.id.search)).check(matches(isDisplayed()))
    }
}