package com.example.simpletasks;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.simpletasks.MainActivity.SHOW_ADD_PIN_TIP;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.simpletasks.data.AppDatabase;
import com.example.simpletasks.data.daos.PinDao;
import com.example.simpletasks.data.daos.TaskDao;
import com.example.simpletasks.data.daos.TaskStepDao;
import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;
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
public class EditTaskPlanTaskUiTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @SuppressWarnings("deprecation")
    @Before
    public void setup() throws ExecutionException, InterruptedException {
        final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(SHOW_ADD_PIN_TIP, false).commit();


        Date today = new Date();
        Task task = new Task("Task", "", today, 1L, 10L, new Date(today.getYear(), today.getMonth(), today.getDate() + 3));
        Task task2 = new Task("Task2", "", today, 1L, 10L, new Date(today.getYear(), today.getMonth(), today.getDate() + 3));
        TaskStep textTaskStep = new TaskStep(task.getId(), TaskStepTypes.TEXT, 0, "title", "imageUri", "description", "videoUri", "audioUri");
        TaskStep textTaskStep2 = new TaskStep(task2.getId(), TaskStepTypes.TEXT, 0, "title", "imageUri", "description", "videoUri", "audioUri");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final AppDatabase appDb = AppDatabase.getAppDb(context);
        final TaskDao taskDao = appDb.taskDao();
        final TaskStepDao taskStepDao = appDb.taskStepDao();
        final PinDao pinDao = appDb.pinDao();

        pinDao.deleteAll().get();
        taskDao.deleteAll().get();
        taskStepDao.deleteAll().get();
        taskDao.insertTasks(Collections.singletonList(task)).get();
        taskDao.insertTasks(Collections.singletonList(task2)).get();
        taskStepDao.insertTaskStep(textTaskStep).get();
        taskStepDao.insertTaskStep(textTaskStep2).get();
    }

    @Test
    public void editTaskPlanTaskUiTest() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.loginButton),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        materialButton.perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.deleteTaskButton_editTasks),
                        childAtPosition(
                                allOf(withId(R.id.imageTask),
                                        childAtPosition(
                                                withId(R.id.tasks_list),
                                                1)),
                                4),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.deleteTaskButton_editTasks),
                        childAtPosition(
                                allOf(withId(R.id.imageTask),
                                        childAtPosition(
                                                withId(R.id.tasks_list),
                                                1)),
                                4),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction viewGroup = onView(
                allOf(withParent(allOf(withId(android.R.id.content),
                                withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        viewGroup.check(matches(isDisplayed()));

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.actionBTN),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        1),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.editTaskButton_editTasks),
                        childAtPosition(
                                allOf(withId(R.id.imageTask),
                                        childAtPosition(
                                                withId(R.id.tasks_list),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction editText = onView(
                allOf(withId(R.id.taskTitle_editTask),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.planTaskButton_editTask),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.saveTaskButton_editTask),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.planTaskButton_editTask),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                7),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.daysDecimalInput_ScheduleTask), withText("0"),
                        withParent(withParent(IsInstanceOf.instanceOf(android.view.ViewGroup.class))),
                        isDisplayed()));
        editText2.check(matches(withText("0")));

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.hoursDecimalInput_ScheduleTask), withText("0"),
                        withParent(withParent(IsInstanceOf.instanceOf(android.view.ViewGroup.class))),
                        isDisplayed()));
        editText3.check(matches(withText("0")));

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.minutesDecimalInput_ScheduleTask), withText("0"),
                        withParent(withParent(IsInstanceOf.instanceOf(android.view.ViewGroup.class))),
                        isDisplayed()));
        editText4.check(matches(withText("0")));

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.daysDecimalInput_ScheduleTask), withText("0"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        8),
                                3),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("2"));

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.daysDecimalInput_ScheduleTask), withText("2"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        8),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.hoursDecimalInput_ScheduleTask), withText("0"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        8),
                                4),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("1"));

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.hoursDecimalInput_ScheduleTask), withText("1"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        8),
                                4),
                        isDisplayed()));
        appCompatEditText4.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.minutesDecimalInput_ScheduleTask), withText("0"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        8),
                                5),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("15"));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.minutesDecimalInput_ScheduleTask), withText("15"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        8),
                                5),
                        isDisplayed()));
        appCompatEditText6.perform(closeSoftKeyboard());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.saveTaskButton_scheduleTask),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                7),
                        isDisplayed()));
        materialButton4.perform(click());
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
