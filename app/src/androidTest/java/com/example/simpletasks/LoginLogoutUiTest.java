package com.example.simpletasks;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static com.example.simpletasks.MainActivity.SHOW_ADD_PIN_TIP;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.simpletasks.data.AppDatabase;
import com.example.simpletasks.data.daos.PinDao;
import com.example.simpletasks.data.repositories.PinRepository;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginLogoutUiTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);


    @Before
    public void setup() throws ExecutionException, InterruptedException {
        Context context = ApplicationProvider.getApplicationContext();
        PinDao pinDao = AppDatabase.getAppDb(context).pinDao();
        pinDao.deleteAll().get();
    }

    @Test
    public void loginLogoutUiTest() {
        final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(SHOW_ADD_PIN_TIP, false).commit();

        ViewInteraction button = onView(
                allOf(withId(R.id.loginButton),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.loginButton),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.logoutButton),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton2.perform(click());


        ViewInteraction viewGroup = onView(
                allOf(withParent(allOf(withId(android.R.id.content),
                                withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        viewGroup.check(matches(isDisplayed()));

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.cancelBTN),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        1),
                                0),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.backButton_EditTasks),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction viewGroup2 = onView(
                allOf(withParent(allOf(withId(android.R.id.content),
                                withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        viewGroup2.check(matches(isDisplayed()));

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.cancelBTN),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        1),
                                0),
                        isDisplayed()));
        materialButton4.perform(click());

        pressBack();

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.actionBTN),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        1),
                                1),
                        isDisplayed()));
        materialButton5.perform(click());

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
