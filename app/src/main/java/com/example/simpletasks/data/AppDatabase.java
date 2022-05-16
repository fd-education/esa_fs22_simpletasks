package com.example.simpletasks.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.simpletasks.data.converters.DateConverter;
import com.example.simpletasks.data.daos.PinDao;
import com.example.simpletasks.data.daos.TaskDao;
import com.example.simpletasks.data.daos.TaskStepDao;
import com.example.simpletasks.data.entities.Pin;
import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.entities.TaskWithSteps;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Room database for Simple Tasks
 */
@Database(entities = {Task.class, TaskStep.class, Pin.class}, version = 3, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = "AppDatabase";
    // Single instance of the app database
    private static volatile AppDatabase APP_DB;

    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    @VisibleForTesting
    public static final String DB_NAME = "simple-tasks-db";

    // The daos of each table
    public abstract PinDao pinDao();
    public abstract TaskDao taskDao();
    public abstract TaskStepDao taskStepDao();

    /**
     * Get a singleton instance of the app database.
     *
     * @param context the app context
     * @return singleton instance of the app database.
     */
    public static AppDatabase getAppDb(final Context context) {
        AppDatabase result = APP_DB;

        if (result == null) {
            synchronized (AppDatabase.class) {
                result = APP_DB;
                if (result == null) {
                    Log.d(TAG, "database does not exist yet - creating");
                    APP_DB = result = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                    Log.d(TAG, "database was created");
                }
            }
        }

        return result;
    }

    /**
     * Get a singleton instance of the app database with mock data.
     *
     * @param context the app context
     * @param doSeedTasks true if the tasks and steps must be seeded, false otherwise
     * @param doSeedPins true if the pins must be seeded, false otherwise
     * @return singleton instance of the app database.
     */
    public static AppDatabase getSeededAppDb(final Context context, boolean doSeedTasks, boolean doSeedPins){
        AppDatabase result = APP_DB;

        if (result == null) {
            synchronized (AppDatabase.class) {
                result = APP_DB;
                if (result == null) {
                    Log.d(TAG, "seeded database does not exist yet - creating");
                    Builder<AppDatabase> bld = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME).fallbackToDestructiveMigration();

                    if(doSeedTasks) bld.addCallback(seedTasks);

                    if(doSeedPins) bld.addCallback(seedPins);

                    APP_DB = result = bld.build();
                    Log.d(TAG, "seeded database was created");
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
                Log.d(TAG, "start seeding tasks");
                TaskDao taskDao = APP_DB.taskDao();
                TaskStepDao taskStepDao = APP_DB.taskStepDao();

                List<TaskWithSteps> tasksWithSteps = Seeder.createSeed(5);

                List<Task> tasks = new ArrayList<>();

                taskDao.deleteAll();
                taskStepDao.deleteAll();

                for (TaskWithSteps task : tasksWithSteps) {
                    tasks.add(task.getTask());
                }

                taskDao.insertTasks(tasks);

                for (TaskWithSteps task : tasksWithSteps) {
                    taskStepDao.insertTaskSteps(task.getSteps());
                }
                Log.d(TAG, "finished seeding tasks");
            });
        }
    };

    // TODO Remove before final submission
    private static final RoomDatabase.Callback seedPins = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            databaseWriteExecutor.execute(() -> {
                Log.d(TAG, "start seeding pins");
                PinDao pinDao = APP_DB.pinDao();

                Pin pin1 = new Pin("12345".hashCode());
                Pin pin2 = new Pin("00000".hashCode());

                pinDao.deleteAll();

                pinDao.insertPin(pin1);
                pinDao.insertPin(pin2);
                Log.d(TAG, "finished seeding pins");
            });
        }
    };
}
