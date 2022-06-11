package com.example.simpletasks;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.content.Context;
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
import com.example.simpletasks.data.daos.TaskDao;
import com.example.simpletasks.data.daos.TaskStepDao;
import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.repositories.TaskRepository;
import com.example.simpletasks.data.types.TaskStepTypes;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.Date;
import java.util.concurrent.ExecutionException;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AbortTaskUiTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() throws ExecutionException, InterruptedException {
        Date today = new Date();
        // today = new Date(today.getYear(), today.getMonth(), today.getDay(), 0, 0);
        Task task = new Task("Task", "", today, 1L, 10L, new Date(today.getYear(), today.getMonth(), today.getDate() + 3));
        TaskStep textTaskStep = new TaskStep(task.getId(), TaskStepTypes.TEXT, 0, "title", "imageUri", "description", "videoUri", "audioUri");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Context context = ApplicationProvider.getApplicationContext();
        final AppDatabase appDb = AppDatabase.getAppDb(context);
        final TaskDao taskDao = appDb.taskDao();
        final TaskStepDao taskStepDao = appDb.taskStepDao();

        taskDao.deleteAll().get();
        taskStepDao.deleteAll().get();
        taskDao.insertTasks(Collections.singletonList(task)).get();
        taskStepDao.insertTaskStep(textTaskStep).get();
    }

    @Test
    public void abortTaskUiTest() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView = onView(
                allOf(withId(R.id.titleTask_taskList),
                        withParent(allOf(withId(R.id.taskListItem),
                                withParent(withId(R.id.tasks_list)))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.tasks_list),
                        childAtPosition(
                                withClassName(is("android.widget.FrameLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction button = onView(
                allOf(withId(R.id.ib_taskstep_backbutton),
                        withParent(allOf(withId(R.id.ll_taskstep_buttongroup),
                                withParent(IsInstanceOf.instanceOf(ViewGroup.class)))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.ib_taskstep_backbutton),
                        childAtPosition(
                                allOf(withId(R.id.ll_taskstep_buttongroup),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                4)),
                                0),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction button2 = onView(
                allOf(withId(R.id.cancelBTN),
                        withParent(withParent(IsInstanceOf.instanceOf(ViewGroup.class))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.actionBTN),
                        withParent(withParent(IsInstanceOf.instanceOf(ViewGroup.class))),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.cancelBTN),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        1),
                                0),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction button4 = onView(
                allOf(withId(R.id.ib_taskstep_backbutton),
                        withParent(allOf(withId(R.id.ll_taskstep_buttongroup),
                                withParent(IsInstanceOf.instanceOf(ViewGroup.class)))),
                        isDisplayed()));
        button4.check(matches(isDisplayed()));

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.ib_taskstep_backbutton),
                        childAtPosition(
                                allOf(withId(R.id.ll_taskstep_buttongroup),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                4)),
                                0),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.actionBTN),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        1),
                                1),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.titleTask_taskList),
                        withParent(allOf(withId(R.id.taskListItem),
                                withParent(withId(R.id.tasks_list)))),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));
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
