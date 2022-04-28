package com.example.simpletasks.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.simpletasks.data.converter.DateConverter;
import com.example.simpletasks.data.dao.PinDao;
import com.example.simpletasks.data.dao.TaskDao;
import com.example.simpletasks.data.dao.TaskStepDao;
import com.example.simpletasks.data.entity.Pin;
import com.example.simpletasks.data.entity.Task;
import com.example.simpletasks.data.entity.TaskStep;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Room database for Simple Tasks
 */
@Database(entities = {Task.class, TaskStep.class, Pin.class}, version = 1, exportSchema = false)
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
                    APP_DB = result = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)/*.addCallback(sRoomDatabaseCallback)*/.build();
                }
            }
        }

        return result;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            databaseWriteExecutor.execute(() -> {
                TaskDao dao = APP_DB.taskDao();

                List<TaskStep> taskSteps = new ArrayList<>();
                taskSteps.add(new TaskStep("0", "Type", 0, "Titel Step1", "https://onlyhdwallpapers.com/wallpaper/testimage-hdtv-3sf1.jpg?preview", "beschreibung step", "", ""));
                taskSteps.add(new TaskStep("0", "Type", 1, "Titel Step2", "", "beschreibung step", "", ""));
                Task task = new Task("Titel1", "Beschreibung1", new Date(2022, 5, 1), null, Long.MAX_VALUE, new Date(2022, 6, 30), taskSteps);
                dao.insertTasks(task);
                taskSteps = new ArrayList<>();
                taskSteps.add(new TaskStep("0", "Type", 0, "Titel Step1", "https://www.researchgate.net/profile/Shagufta-Yasmin/publication/313121867/figure/fig4/AS:629900906078221@1527191487716/Original-Colour-Test-Image.png", "beschreibung step", "", ""));
                taskSteps.add(new TaskStep("0", "Type", 1, "Titel Step2", "", "beschreibung step", "", ""));
                task = new Task("Titel2", "Beschreibung2", new Date(2022, 5, 1), null, Long.MAX_VALUE, new Date(2022, 6, 30), taskSteps);
                dao.insertTasks(task);
            });
        }
    };
}
