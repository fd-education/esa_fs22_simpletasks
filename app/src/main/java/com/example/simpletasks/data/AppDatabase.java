package com.example.simpletasks.data;

import android.content.Context;


import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.simpletasks.data.converter.DateConverter;
import com.example.simpletasks.data.dao.PinDao;
import com.example.simpletasks.data.dao.TaskDao;
import com.example.simpletasks.data.dao.TaskStepDao;
import com.example.simpletasks.data.entity.Pin;
import com.example.simpletasks.data.entity.Task;
import com.example.simpletasks.data.entity.TaskStep;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Room database for Simple Tasks
 */
@Database(entities={Task.class, TaskStep.class, Pin.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase APP_DB;

    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    @VisibleForTesting
    public static final String DB_NAME = "simple-tasks-db";

    public abstract PinDao pinDao();
    public abstract TaskDao taskDao();
    public abstract TaskStepDao taskStepDao();

    public static AppDatabase getAppDb(final Context context){
        AppDatabase result = APP_DB;

        if(result == null){
            synchronized (AppDatabase.class){
                result = APP_DB;
                if(result == null){
                    APP_DB = result = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME).build();
                }
            }
        }

        return result;
    }
}
