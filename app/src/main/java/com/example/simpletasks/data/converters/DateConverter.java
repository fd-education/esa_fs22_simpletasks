package com.example.simpletasks.data.converters;


import android.util.Log;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Converts date to long and long to date when exchanging data between the database and the application.
 */
public class DateConverter {
    private static final String DEBUG_TAG = "DateConverter";

    /**
     * Convert from date to long when inserting or updating dates in the database.
     *
     * @param date the date to convert
     * @return the long value for a date
     */
    @TypeConverter
    public Long dateToLong(final Date date) {
        Log.d(DEBUG_TAG, String.format("Date %s converted to Long", date));
        return date == null ? null : date.getTime();
    }

    /**
     * Convert from long to date when fetching data from the database.
     *
     * @param stamp the long value to convert
     * @return the date for the long value
     */
    @TypeConverter
    public Date longToDate(final Long stamp) {
        Log.d(DEBUG_TAG, String.format("Long %s converted to Date", stamp));
        return stamp == null ? null : new Date(stamp);
    }
}
