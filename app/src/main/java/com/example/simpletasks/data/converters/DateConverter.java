package com.example.simpletasks.data.converters;


import android.util.Log;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    private static final String DEBUG_TAG = "DateConverter";

    @TypeConverter
    public Long dateToLong(Date date){
        Log.d(DEBUG_TAG, String.format("Date %s converted to Long", date));
        return date == null? null : date.getTime();
    }

    @TypeConverter
    public Date longToDate(Long stamp){
        Log.d(DEBUG_TAG, String.format("Long %s converted to Date", stamp));
        return stamp == null? null : new Date(stamp);
    }
}
