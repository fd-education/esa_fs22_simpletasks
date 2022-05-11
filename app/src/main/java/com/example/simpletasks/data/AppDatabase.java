package com.example.simpletasks.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.simpletasks.data.converters.*;
import com.example.simpletasks.data.daos.*;
import com.example.simpletasks.data.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Room database for Simple Tasks
 */
@Database(entities = {Task.class, TaskStep.class, Pin.class}, version = 2, exportSchema = false)
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

    public static AppDatabase getAppDb(final Context context) {
        AppDatabase result = APP_DB;

        if (result == null) {
            synchronized (AppDatabase.class) {
                result = APP_DB;
                if (result == null) {
                    APP_DB = result = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return result;
    }

    public static AppDatabase getSeededAppDb(final Context context, boolean doSeedTasks, boolean doSeedPins){
        AppDatabase result = APP_DB;

        if (result == null) {
            synchronized (AppDatabase.class) {
                result = APP_DB;
                if (result == null) {
                    Builder<AppDatabase> bld = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME).fallbackToDestructiveMigration();

                    if(doSeedTasks) bld.addCallback(seedTasks);

                    if(doSeedPins) bld.addCallback(seedPins);

                    APP_DB = result = bld.build();
                }
            }
        }

        return result;
    }

    // TODO Remove before final submission
    private static final RoomDatabase.Callback seedTasks = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            databaseWriteExecutor.execute(() -> {
                TaskDao taskDao = APP_DB.taskDao();
                TaskStepDao taskStepDao = APP_DB.taskStepDao();

                List<TaskWithSteps> tasksWithSteps = Seeder.createSeed(5);

                List<Task> tasks = new ArrayList<>();

                for (TaskWithSteps task : tasksWithSteps) {
                    tasks.add(task.getTask());
                }

                taskDao.insertTasks(tasks);

                for (TaskWithSteps task : tasksWithSteps) {
                    taskStepDao.insertTaskSteps(task.getSteps());
                }
            });
        }
    };

    // TODO Remove before final submission
    private static final RoomDatabase.Callback seedPins = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            databaseWriteExecutor.execute(() -> {
                PinDao pinDao = APP_DB.pinDao();

                Pin pin1 = new Pin("12345".hashCode());
                Pin pin2 = new Pin("00000".hashCode());

                pinDao.deleteAll();

                pinDao.insertPin(pin1);
                pinDao.insertPin(pin2);
            });
        }
    };
}
