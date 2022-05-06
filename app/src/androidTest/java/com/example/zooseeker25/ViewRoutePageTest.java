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
public class ViewRoutePageTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void viewRoutePageTest() {
        ViewInteraction materialButton = onView(
allOf(withId(R.id.view_route_btn), withText("View Route"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
4),
isDisplayed()));
        materialButton.perform(click());
        
        ViewInteraction viewGroup = onView(
allOf(withParent(allOf(withId(android.R.id.content),
withParent(withId(androidx.appcompat.R.id.decor_content_parent)))),
isDisplayed()));
        viewGroup.check(matches(isDisplayed()));
        
        ViewInteraction button = onView(
allOf(withId(R.id.back_btn), withText("BACK"),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        button.check(matches(isDisplayed()));
        
        ViewInteraction button2 = onView(
allOf(withId(R.id.plan_route_btn), withText("PLAN ROUTE"),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        button2.check(matches(isDisplayed()));
        
        ViewInteraction button3 = onView(
allOf(withId(R.id.plan_route_btn), withText("PLAN ROUTE"),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        button3.check(matches(isDisplayed()));
        
        ViewInteraction materialButton2 = onView(
allOf(withId(R.id.back_btn), withText("Back"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
1),
isDisplayed()));
        materialButton2.perform(click());
        
        ViewInteraction button4 = onView(
allOf(withId(R.id.view_route_btn), withText("VIEW ROUTE"),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        button4.check(matches(isDisplayed()));
        
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
        
        ViewInteraction searchView2 = onView(
allOf(withId(R.id.searchView),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        searchView2.check(matches(isDisplayed()));
        }
    
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
