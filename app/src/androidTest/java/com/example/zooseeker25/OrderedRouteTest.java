package com.example.zooseeker25;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
/**
 * Selected several exhibits
 * We want to see the order of exhibits in the overView Page
 */
public class OrderedRouteTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>( MainActivity.class );

    @Test
    public void orderedRouteTest() {
        ViewInteraction materialButton = onView(
                allOf( withId( R.id.clear_all_button ), withText( "Clear Selected" ),
                        childAtPosition(
                                childAtPosition(
                                        withId( android.R.id.content ),
                                        0 ),
                                5 ),
                        isDisplayed() ) );
        materialButton.perform( click() );

        ViewInteraction materialButton2 = onView(
                allOf( withId( R.id.clear_all_button ), withText( "Clear Selected" ),
                        childAtPosition(
                                childAtPosition(
                                        withId( android.R.id.content ),
                                        0 ),
                                5 ),
                        isDisplayed() ) );
        materialButton2.perform( click() );

        ViewInteraction appCompatImageView = onView(
                allOf( withClassName( is( "androidx.appcompat.widget.AppCompatImageView" ) ), withContentDescription( "Search" ),
                        childAtPosition(
                                allOf( withClassName( is( "android.widget.LinearLayout" ) ),
                                        childAtPosition(
                                                withId( R.id.searchView ),
                                                0 ) ),
                                1 ),
                        isDisplayed() ) );
        appCompatImageView.perform( click() );

        ViewInteraction searchAutoComplete = onView(
                allOf( withClassName( is( "android.widget.SearchView$SearchAutoComplete" ) ),
                        childAtPosition(
                                allOf( withClassName( is( "android.widget.LinearLayout" ) ),
                                        childAtPosition(
                                                withClassName( is( "android.widget.LinearLayout" ) ),
                                                1 ) ),
                                0 ),
                        isDisplayed() ) );
        searchAutoComplete.perform( replaceText( "a" ), closeSoftKeyboard() );

        ViewInteraction materialTextView = onView(
                allOf( withId( R.id.search_item_text ), withText( "Gorillas" ),
                        childAtPosition(
                                childAtPosition(
                                        withId( R.id.search_results ),
                                        3 ),
                                0 ),
                        isDisplayed() ) );
        materialTextView.perform( click() );

        ViewInteraction materialTextView2 = onView(
                allOf( withId( R.id.search_item_text ), withText( "Flamingos" ),
                        childAtPosition(
                                childAtPosition(
                                        withId( R.id.search_results ),
                                        1 ),
                                0 ),
                        isDisplayed() ) );
        materialTextView2.perform( click() );

        ViewInteraction materialButton3 = onView(
                allOf( withId( R.id.view_route_btn ), withText( "View Selected" ),
                        childAtPosition(
                                childAtPosition(
                                        withId( android.R.id.content ),
                                        0 ),
                                4 ),
                        isDisplayed() ) );
        materialButton3.perform( click() );

        ViewInteraction materialButton4 = onView(
                allOf( withId( R.id.plan_route_btn ), withText( "Plan Route" ),
                        childAtPosition(
                                childAtPosition(
                                        withId( android.R.id.content ),
                                        0 ),
                                2 ),
                        isDisplayed() ) );
        materialButton4.perform( click() );

        ViewInteraction materialButton5 = onView(
                allOf( withId( android.R.id.button2 ), withText( "No" ),
                        childAtPosition(
                                childAtPosition(
                                        withId( androidx.appcompat.R.id.buttonPanel ),
                                        0 ),
                                2 ) ) );
        materialButton5.perform( scrollTo(), click() );

        ViewInteraction textView = onView(
                allOf( withId( R.id.selected_exhibit_name ), withText( "1) Flamingos : 90 meters" ),
                        withParent( withParent( withId( R.id.Overview_recyclerView ) ) ),
                        isDisplayed() ) );
        textView.check( matches( withText( "1) Flamingos : 90 meters" ) ) );

        ViewInteraction textView2 = onView(
                allOf( withId( R.id.selected_exhibit_name ), withText( "2) Gorillas : 420 meters" ),
                        withParent( withParent( withId( R.id.Overview_recyclerView ) ) ),
                        isDisplayed() ) );
        textView2.check( matches( withText( "2) Gorillas : 420 meters" ) ) );
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText( "Child at position " + position + " in parent " );
                parentMatcher.describeTo( description );
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches( parent )
                        && view.equals( ((ViewGroup) parent).getChildAt( position ) );
            }
        };
    }
}
