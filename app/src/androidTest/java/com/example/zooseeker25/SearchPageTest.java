package com.example.zooseeker25;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.zooseeker25.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchPageTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void searchPageTest() {
        ViewInteraction button = onView(
allOf(withId(R.id.view_route_btn), withText("VIEW ROUTE"),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        button.check(matches(isDisplayed()));
        
        ViewInteraction textView = onView(
allOf(withId(R.id.listCounterPlaceHolder), withText("0"),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        textView.check(matches(withText("0")));
        
        ViewInteraction searchView = onView(
allOf(withId(R.id.searchView),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        searchView.check(matches(isDisplayed()));
        
        ViewInteraction textView2 = onView(
allOf(withId(R.id.title_text), withText("ZooSeeker"),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        textView2.check(matches(withText("ZooSeeker")));
        
        ViewInteraction textView3 = onView(
allOf(withId(R.id.title_text), withText("ZooSeeker"),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        textView3.check(matches(withText("ZooSeeker")));
        }
    }
