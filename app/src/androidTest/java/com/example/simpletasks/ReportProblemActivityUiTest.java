package com.example.simpletasks;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ReportProblemActivityUiTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    @SuppressWarnings("deprecation")
    // Recommended on https://developer.android.com/training/testing/espresso/intents#test-rules
    public IntentsTestRule<MainActivity> intentsTestRule =
            new IntentsTestRule<>(MainActivity.class);
    private Context context;
    private Task task;
    private TaskStep textTaskStep;


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

    @Before
    @SuppressWarnings("deprecation") // For the date class
    public void setup() throws ExecutionException, InterruptedException {
        context = ApplicationProvider.getApplicationContext();
        final TaskDao taskDao = AppDatabase.getAppDb(context).taskDao();
        final TaskStepDao taskStepDao = AppDatabase.getAppDb(context).taskStepDao();
        taskStepDao.deleteAll().get();
        taskDao.deleteAll().get();

        task = new Task("Task", "test task", new Date(), 1L, 10L, new Date(3000, 1, 1));
        textTaskStep = new TaskStep(task.getId(), TaskStepTypes.TEXT, 0, "title", "imageUri", "description", "videoUri", "audioUri");
        final TaskWithSteps taskWithSteps = new TaskWithSteps(task, Collections.singletonList(textTaskStep));

        final TaskRepository taskRepository = new TaskRepository(context);
        taskRepository.insertTaskWithSteps(Collections.singletonList(taskWithSteps));
    }

    @Test
    public void reportProblemActivityUiTest() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.tasks_list),
                        childAtPosition(
                                withClassName(is("android.widget.FrameLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.b_taskstep_problem), withText(R.string.have_problem),
                        isDisplayed()));
        materialButton.perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.sendProblemButton), withText(R.string.report_problem_send_button),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.problemStatementError), withText(R.string.no_problem_stated),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(withText(R.string.no_problem_stated)));

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.problemStatementEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Test"), closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.sendProblemButton), withText(R.string.report_problem_send_button),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton3.perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intents.intended(new TypeSafeMatcher<Intent>() {
            @Override
            protected boolean matchesSafely(Intent intent) {
                final Intent intentExtra = intent.getParcelableExtra(Intent.EXTRA_INTENT);
                final String additionalInformation = context.getString(
                        R.string.problem_statement_additional_information,
                        task.getTitle(), textTaskStep.getTitle(), 1);
                String problemText = String.format(Locale.getDefault(),
                        "Test%n%n%s", additionalInformation
                );
                return Objects.equals(intent.getAction(), Intent.ACTION_CHOOSER)
                        && Objects.equals(intentExtra.getStringExtra(Intent.EXTRA_TEXT), problemText)
                        && Objects.equals(intentExtra.getType(), "text/plain");
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Intent has action ACTION_SEND and the correct EXTRA_TEXT");
            }
        });
    }
}
