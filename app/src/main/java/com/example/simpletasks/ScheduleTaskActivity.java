package com.example.simpletasks;

import static com.example.simpletasks.EditTaskActivity.SHARED_PREF_TASK_ID;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.viewmodels.TaskViewModel;
import com.example.simpletasks.listener.DateTimePickerListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ScheduleTaskActivity extends AppCompatActivity {
    private static final String TAG = "ScheduleTaskActivity";

    private Task task;
    private Calendar calendar;
    private DateTimePickerListener dateTimePickerNextExecution;
    private DateTimePickerListener dateTimePickerLastExecution;

    /**
     * set the view and get the task element from the intent
     *
     * @param savedInstanceState reconstruction of a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_task);

        getTask();
    }

    /**
     * handles a click on the plan next execution button
     *
     * @param view the view that triggered the event
     */
    public void onPlanNextExecutionClicked(View view) {
        calendar = Calendar.getInstance();
        dateTimePickerNextExecution = new DateTimePickerListener(calendar);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateTimePickerNextExecution, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /**
     * handles a click on the plan next execution button
     *
     * @param view the view that triggered the event
     */
    public void onPlanLastExecutionClicked(View view) {
        calendar = Calendar.getInstance();
        dateTimePickerLastExecution = new DateTimePickerListener(calendar);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateTimePickerLastExecution, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /**
     * handle click events on the save button
     *
     * @param view the view that triggered the event
     */
    public void onSaveTaskClicked(View view) {
        //save the new end date
        if (dateTimePickerLastExecution != null) {
            Date newEndDate = dateTimePickerLastExecution.getUpdatedCalendar().getTime();
            task.setEndDate(newEndDate);
        }

        //save the new start date if not after end date
        if (dateTimePickerNextExecution != null) {
            Date newNextStartDate = dateTimePickerNextExecution.getUpdatedCalendar().getTime();
            if (newNextStartDate.before(task.getEndDate())) {
                task.setNextStartDate(newNextStartDate);
            }
        }

        //save the interval
        setNewInterval();

        //save the new start date in the database
        TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.updateTask(task);
        Log.d(TAG, "updated the task times");

        //go back (call super method because local method is asking to discard changes)
        super.onBackPressed();
    }

    /**
     * Handle click events on the back button
     *
     * @param view the view that triggered the event
     */
    public void onBackClicked(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        new DialogBuilder()
                .setDescriptionText(R.string.discard_changes_text)
                .setContext(this)
                .setTwoButtonLayout(R.string.cancel_popup, R.string.discard_changes_button)
                .setAction(super::onBackPressed).build().show();
    }


    //gets the task with steps object from the intent
    private void getTask() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        String taskId = sharedPreferences.getString(SHARED_PREF_TASK_ID, "default");

        TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getTaskById(taskId).observe(this, fetchedTask -> {
            task = fetchedTask;
            Log.d(TAG, "successfully fetched the task");
            fillUiElements();
        });
    }

    //fills the ui elements with the data
    private void fillUiElements() {
        //use this suppress because we really want this specific date time format to appear
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.date_time_format), Locale.GERMANY);
        String currentNextExecutionDate = simpleDateFormat.format(task.getNextStartDate());
        String currentLastExecutionDate = simpleDateFormat.format(task.getEndDate());
        long currentIntervalLong = task.getInterval();
        int currentIntervalDays = (int) (currentIntervalLong / TaskWithSteps.ONE_DAY_INTERVAL);
        currentIntervalLong = currentIntervalLong - currentIntervalDays * TaskWithSteps.ONE_DAY_INTERVAL;
        int currentIntervalHours = (int) (currentIntervalLong / TaskWithSteps.ONE_HOUR_INTERVAL);
        currentIntervalLong = currentIntervalLong - currentIntervalHours * TaskWithSteps.ONE_HOUR_INTERVAL;
        int currentIntervalMinutes = (int) (currentIntervalLong / TaskWithSteps.ONE_MINUTE_INTERVAL);

        //text views
        TextView textView = findViewById(R.id.nextExecutionTextView_scheduleTask);
        textView.setText(getString(R.string.current_next_execution, currentNextExecutionDate));
        textView = findViewById(R.id.lastExecutionTextView_scheduleTask);
        textView.setText(getString(R.string.current_last_execution, currentLastExecutionDate));
        textView = findViewById(R.id.intervalTextView_scheduleTask);
        textView.setText(getString(R.string.current_interval, currentIntervalDays, currentIntervalHours, currentIntervalMinutes));

        //edit text values for the interval
        EditText editText = findViewById(R.id.daysDecimalInput_ScheduleTask);
        editText.setText(String.valueOf(currentIntervalDays));
        editText = findViewById(R.id.hoursDecimalInput_ScheduleTask);
        editText.setText(String.valueOf(currentIntervalHours));
        editText = findViewById(R.id.minutesDecimalInput_ScheduleTask);
        editText.setText(String.valueOf(currentIntervalMinutes));
    }

    //sets the new interval in the task
    private void setNewInterval() {
        //initialize the holder variables
        int newDaysInterval;
        int newHoursInterval;
        int newMinutesInterval;

        //get the values of the edit task elements
        EditText editText = findViewById(R.id.daysDecimalInput_ScheduleTask);
        newDaysInterval = Integer.parseInt(editText.getText().toString());
        editText = findViewById(R.id.hoursDecimalInput_ScheduleTask);
        newHoursInterval = Integer.parseInt(editText.getText().toString());
        editText = findViewById(R.id.minutesDecimalInput_ScheduleTask);
        newMinutesInterval = Integer.parseInt(editText.getText().toString());

        //calculate the new long
        long newInterval = newDaysInterval * TaskWithSteps.ONE_DAY_INTERVAL + newHoursInterval * TaskWithSteps.ONE_HOUR_INTERVAL + newMinutesInterval * TaskWithSteps.ONE_MINUTE_INTERVAL;
        //set the calculated value as interval
        task.setInterval(newInterval);
    }

}