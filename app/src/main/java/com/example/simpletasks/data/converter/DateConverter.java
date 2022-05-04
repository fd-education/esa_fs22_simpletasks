package com.example.simpletasks.data.converter;


import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public Long dateToLong(Date date){
        return date == null? null : date.getTime();
    }

    @TypeConverter
    public Date longToDate(Long stamp){
        return stamp == null? null : new Date(stamp);
    }
}
