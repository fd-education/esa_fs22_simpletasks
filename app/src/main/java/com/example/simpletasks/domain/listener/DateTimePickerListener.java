package com.example.simpletasks.domain.listener;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

public class DateTimePickerListener implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private int newYear, newMonth, newDay, newHour, newMinute;
    private final Calendar calendar;

    public DateTimePickerListener(Calendar calendar) {
        this.calendar = calendar;
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
        Context context = datePicker.getContext();
        newYear = year;
        newMonth = month;
        newDay = dayOfMonth;
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(context));
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

    /**
     * updates the calendar object with the picked values and returns the object
     * @return the calendar with the picked values
     */
    public Calendar getUpdatedCalendar() {
        calendar.set(newYear, newMonth, newDay, newHour, newMinute);
        return calendar;
    }
}
