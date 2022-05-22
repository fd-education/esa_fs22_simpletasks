package com.example.simpletasks;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.viewmodels.TaskViewModel;

import java.util.Calendar;
import java.util.Date;

public class ScheduleTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "ScheduleTaskActivity";

    private TaskWithSteps task;
    private Calendar calendar;
    private int newYear, newMonth, newDay, newHour, newMinute;

    /**
     * set the view and get the task element from the intent
     *
     * @param savedInstanceState reconstruction of a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_task);

        // Remove the action bar at the top of the screen
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        task = getTask();
    }

    public void onPlanNextExecutionButtonClicked(View view) {
         calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /**
     * handle click events on the save button
     *
     * @param view the view that triggered the event
     */
    public void onSaveTaskClicked(View view) {
        //todo implement

        Date newNextStartDate = new Date(newYear, newMonth, newDay, newHour, newMinute);
        task.getTask().setNextStartDate(newNextStartDate);

        //save the new start date in the database
        TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.updateTask(task);
        Log.d(TAG, "updated the task times");
    }

    /**
     * Handle click events on the back button
     *
     * @param view the view that triggered the event
     */
    public void onBackClicked(View view) {
        onBackPressed();
    }

    /**
     * handles the event when the user clicks "ok" in the date picker
     *
     * @param datePicker the date picker
     * @param year       the selected year
     * @param month      the selected month
     * @param dayOfMonth the selected day of the month
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        newYear = year;
        newMonth = month;
        newDay = dayOfMonth;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    /**
     * handles the event when the user clicks "ok" in the time picker
     *
     * @param timePicker the time picker
     * @param hour       the selected hour
     * @param minute     the selected minute
     */
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        newHour = hour;
        newMinute = minute;
    }

    //gets the task with steps object from the intent
    private TaskWithSteps getTask() {
        return getIntent().getParcelableExtra(MainActivity.TASK_INTENT_EXTRA);
    }


}