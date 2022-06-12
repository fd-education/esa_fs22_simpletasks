package com.example.simpletasks.data.repositories;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.simpletasks.data.AppDatabase;
import com.example.simpletasks.data.daos.TaskDao;
import com.example.simpletasks.data.daos.TaskStepDao;
import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.types.TaskStepTypes;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class TaskStepRepositoryTest {
    TaskStepRepository repository;
    private TaskStep textTaskStep;
    private TaskStepDao taskStepDao;
    private TaskDao taskDao;
    private Task task;

    @Before
    public void setUp() {
        task = new Task("Task", "", new Date(2000, 1, 1), 100L, 10L, new Date(2000, 1, 1));
        textTaskStep = new TaskStep(task.getId(), TaskStepTypes.TEXT, 0, "title", "imageUri", "description", "videoUri", "audioUri");
        Context context = ApplicationProvider.getApplicationContext();
        repository = new TaskStepRepository(context);
        final AppDatabase appDb = AppDatabase.getAppDb(context);
        taskStepDao = appDb.taskStepDao();
        taskDao = appDb.taskDao();
        taskStepDao.deleteAll();
        taskDao.deleteAll();
        sleep(500);
    }

    @Test
    public void testGetTaskById() throws ExecutionException, InterruptedException {
        insertTask();
        insertTaskStep();

        final LiveData<List<TaskStep>> taskStepsLD = repository.getByTaskId(task.getId());
        observe(taskStepsLD);
        sleep(500);
        final List<TaskStep> steps = taskStepsLD.getValue();

        assert steps != null;
        assertEquals(1, steps.size());
        assertEquals(textTaskStep, steps.get(0));

    }

    @Test
    public void testInsertTaskSteps() throws ExecutionException, InterruptedException {
        insertTask();

        List<TaskStep> taskSteps = new ArrayList<>();
        taskSteps.add(textTaskStep);
        repository.insertTaskSteps(taskSteps);

        final List<TaskStep> actualTaskSteps = getTaskSteps();
        assertEquals(taskSteps, actualTaskSteps);
    }

    @Test
    public void testUpdateTaskSteps() throws ExecutionException, InterruptedException {
        insertTask();
        insertTaskStep();

        String expectedDescription = "new description";
        textTaskStep.setDescription(expectedDescription);
        List<TaskStep> taskSteps = new ArrayList<>();
        taskSteps.add(textTaskStep);
        repository.updateTaskSteps(taskSteps);

        final List<TaskStep> actualTaskSteps = getTaskSteps();
        assert actualTaskSteps != null;
        assertEquals(taskSteps, actualTaskSteps);
        assertEquals(expectedDescription, actualTaskSteps.get(0).getDescription());
    }

    @Test
    public void testDeleteTaskSteps() throws ExecutionException, InterruptedException {
        insertTask();
        insertTaskStep();

        List<TaskStep> taskSteps = new ArrayList<>();
        taskSteps.add(textTaskStep);
        repository.deleteTaskSteps(taskSteps);

        getTaskSteps();
        sleep(400);
        final List<TaskStep> actualTaskSteps = getTaskSteps();
        assert actualTaskSteps != null;
        assertEquals(0, actualTaskSteps.size());

    }

    @Nullable
    private List<TaskStep> getTaskSteps() {
        final LiveData<List<TaskStep>> taskStepsLD = taskStepDao.getByTaskId(task.getId());
        observe(taskStepsLD);
        sleep(1000);
        return taskStepsLD.getValue();
    }


    private void insertTaskStep() throws ExecutionException, InterruptedException {
        List<TaskStep> taskSteps = new ArrayList<>();
        taskSteps.add(textTaskStep);
        taskStepDao.insertTaskSteps(taskSteps).get();
    }

    private void insertTask() throws ExecutionException, InterruptedException {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        taskDao.insertTasks(tasks).get();
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void observe(LiveData<List<TaskStep>> taskStepsLD) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> taskStepsLD.observeForever(taskSteps1 -> {

        }));
    }
}
