package com.example.simpletasks.data;

import android.content.Context;


import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.simpletasks.data.converters.DateConverter;
import com.example.simpletasks.data.daos.PinDao;
import com.example.simpletasks.data.daos.TaskDao;
import com.example.simpletasks.data.daos.TaskStepDao;
import com.example.simpletasks.data.entities.Pin;
import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Room database for Simple Tasks
 */
@Database(entities={Task.class, TaskStep.class, Pin.class}, version = 2, exportSchema = false)
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

    public void seedDatabase(int nbrOfTasks){
        databaseWriteExecutor.execute(() -> {
            List<Task> tasks = Seeder.createSeed(nbrOfTasks);

            this.taskDao().insertTasks(tasks);

            for(Task task: tasks){
                this.taskStepDao().insertTaskSteps(task.getSteps());
            }
        });
    }
}
