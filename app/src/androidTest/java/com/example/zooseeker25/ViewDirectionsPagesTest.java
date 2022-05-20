package com.example.zooseeker25;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
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
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ViewDirectionsPagesTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void viewDirectionsPagesTest() {
        ViewInteraction appCompatImageView = onView(
                allOf(withClassName(is("androidx.appcompat.widget.AppCompatImageView")), withContentDescription("Search"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        childAtPosition(
                                                withId(R.id.searchView),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction searchAutoComplete = onView(
                allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete.perform(replaceText("e"), closeSoftKeyboard());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.search_item_text), withText("Elephant Odyssey"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_results),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction materialTextView2 = onView(
                allOf(withId(R.id.search_item_text), withText("Arctic Foxes"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_results),
                                        1),
                                0),
                        isDisplayed()));
        materialTextView2.perform(click());

        ViewInteraction materialTextView3 = onView(
                allOf(withId(R.id.search_item_text), withText("Gorillas"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_results),
                                        2),
                                0),
                        isDisplayed()));
        materialTextView3.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.view_route_btn), withText("View Selected"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.plan_route_btn), withText("Plan Route"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.direction_exhibit_title), withText("Gorillas"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.directions_list_view),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.direction_exhibit_counter), withText("2"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.next_button), withText("ELEPHANT ODYSSEY\n400 M"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.next_button), withText("Elephant Odyssey\n400 m"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.direction_exhibit_title), withText("Elephant Odyssey"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView3.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.direction_exhibit_counter), withText("1"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView4.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.prev_button), withText("PREVIOUS"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.next_button), withText("ARCTIC FOXES\n800 M"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.next_button), withText("Arctic Foxes\n800 m"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.direction_exhibit_counter), withText("0"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView5.check(matches(isDisplayed()));

        ViewInteraction button4 = onView(
                allOf(withId(R.id.prev_button), withText("PREVIOUS"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button4.check(matches(isDisplayed()));

        ViewInteraction button5 = onView(
                allOf(withId(R.id.next_button), withText("FINISH"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button5.check(matches(isDisplayed()));
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
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
