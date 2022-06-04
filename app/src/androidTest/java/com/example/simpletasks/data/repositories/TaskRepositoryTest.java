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
import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.types.TaskStepTypes;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@SuppressWarnings("deprecation")
public class TaskRepositoryTest {
    TaskRepository repository;
    private TaskStep textTaskStep;
    private TaskStepDao taskStepDao;
    private TaskDao taskDao;
    private Task task;
    private TaskWithSteps taskWithSteps;

    @Before
    public void setUp() {
        task = new Task("Task", "", "test task", new Date(2000, 1, 1), 100L, 10L, new Date(2000, 1, 1));
        textTaskStep = new TaskStep(task.getId(), TaskStepTypes.TEXT, 0, "title", "imageUri", "description", "videoUri", "audioUri");
        taskWithSteps = new TaskWithSteps(task, Collections.singletonList(textTaskStep));

        Context context = ApplicationProvider.getApplicationContext();
        final AppDatabase appDb = AppDatabase.getAppDb(context);
        repository = new TaskRepository(context);
        taskStepDao = appDb.taskStepDao();
        taskDao = appDb.taskDao();
        taskDao.deleteAll();
        taskStepDao.deleteAll();
    }

    @Test
    public void testGetAllTasks() {
        insertTaskWithSteps();

        final LiveData<List<Task>> allTasksLD = repository.getAllTasks();
        observeTask(allTasksLD);
        sleep(500);
        final List<Task> allTasks = allTasksLD.getValue();

        assert allTasks != null;
        assertEquals(Collections.singletonList(task), allTasks);
    }

    @Test
    public void testGetAllTasksWithSteps() {
        insertTaskWithSteps();

        final LiveData<List<TaskWithSteps>> allTasksWithStepsLD = repository.getAllTasksWithSteps();
        observeTaskWithSteps(allTasksWithStepsLD);
        sleep(500);
        final List<TaskWithSteps> allTasks = allTasksWithStepsLD.getValue();

        assert allTasks != null;
        assertEquals(1, allTasks.size());
        final TaskWithSteps taskWithSteps = allTasks.get(0);
        assertEquals(Collections.singletonList(textTaskStep), taskWithSteps.getSteps());
        assertEquals(task, taskWithSteps.getTask());
    }

    @Test
    public void testGetTasksByDateWithSteps() {
        insertTaskWithSteps();
        final Date dateWithTask = new Date(2000, 1, 1);
        final Date dateWithoutTask = new Date(2000, 1, 2);

        final LiveData<List<TaskWithSteps>> tasksWithSteps = repository.getTasksByDateWithSteps(dateWithTask);
        final LiveData<List<TaskWithSteps>> emptyTasksLD = repository.getTasksByDateWithSteps(dateWithoutTask);
        observeTaskWithSteps(tasksWithSteps);
        observeTaskWithSteps(emptyTasksLD);
        sleep(500);
        final List<TaskWithSteps> tasks = tasksWithSteps.getValue();
        final List<TaskWithSteps> emptyTasks = emptyTasksLD.getValue();

        assert tasks != null;
        assertEquals(1, tasks.size());
        final TaskWithSteps taskWithSteps = tasks.get(0);
        assertEquals(Collections.singletonList(textTaskStep), taskWithSteps.getSteps());
        assertEquals(task, taskWithSteps.getTask());
        assert emptyTasks != null;
        assertEquals(0, emptyTasks.size());
    }

    @Test
    public void testGetTasksByDate() {
        insertTaskWithSteps();
        final Date dateWithTask = new Date(2000, 1, 1);
        final Date dateWithoutTask = new Date(2000, 1, 2);

        final LiveData<List<Task>> tasksLD = repository.getTasksByDate(dateWithTask);
        final LiveData<List<Task>> emptyTasksLD = repository.getTasksByDate(dateWithoutTask);
        observeTask(tasksLD);
        observeTask(emptyTasksLD);
        sleep(500);
        final List<Task> tasks = tasksLD.getValue();
        final List<Task> emptyTasks = emptyTasksLD.getValue();

        assert tasks != null;
        assertEquals(1, tasks.size());
        assertEquals(this.task, tasks.get(0));
        assert emptyTasks != null;
        assertEquals(0, emptyTasks.size());
    }

    @Test
    public void testInsertTaskWithSteps() {
        repository.insertTaskWithSteps(Collections.singletonList(taskWithSteps));

        final List<TaskWithSteps> allTasks = getTaskWithSteps();
        assert allTasks != null;
        assertEquals(1, allTasks.size());
        final TaskWithSteps taskWithSteps = allTasks.get(0);
        assertEquals(Collections.singletonList(textTaskStep), taskWithSteps.getSteps());
        assertEquals(task, taskWithSteps.getTask());
    }

    @Test
    public void testDeleteTasks() {
        repository.deleteTasks(Collections.singletonList(taskWithSteps));

        final List<TaskWithSteps> allTasks = getTaskWithSteps();
        assert allTasks != null;
        assertEquals(0, allTasks.size());
    }

    @Test
    public void testInsertTask() {
        repository.insertTasks(Collections.singletonList(task));

        final List<Task> allTasks = getTasks();
        assert allTasks != null;
        assertEquals(1, allTasks.size());
        final Task actualTask = allTasks.get(0);
        assertEquals(task, actualTask);
    }

    @Test
    public void testUpdateTasksWithSteps() {
        insertTaskWithSteps();


        final String expectedDescription = "new description";
        task.setDescription(expectedDescription);
        repository.updateTasksWithSteps(Collections.singletonList(taskWithSteps));

        final List<TaskWithSteps> allTasks = getTaskWithSteps();
        assert allTasks != null;
        assertEquals(1, allTasks.size());
        final TaskWithSteps taskWithSteps = allTasks.get(0);
        assertEquals(expectedDescription, taskWithSteps.getTask().getDescription());
    }


    @Nullable
    private List<Task> getTasks() {
        final LiveData<List<Task>> all = taskDao.getAll();
        observeTask(all);
        sleep(100);
        return all.getValue();
    }

    @Nullable
    private List<TaskWithSteps> getTaskWithSteps() {
        final LiveData<List<TaskWithSteps>> allWithSteps = taskDao.getAllWithSteps();
        observeTaskWithSteps(allWithSteps);
        sleep(100);
        return allWithSteps.getValue();
    }

    private void insertTaskWithSteps() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        taskDao.insertTasks(tasks);
        sleep(500);
        List<TaskStep> taskSteps = new ArrayList<>();
        taskSteps.add(textTaskStep);
        taskStepDao.insertTaskSteps(taskSteps);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void observeTask(LiveData<List<Task>> liveData) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> liveData.observeForever(taskSteps1 -> {

        }));
    }

    private void observeTaskWithSteps(LiveData<List<TaskWithSteps>> liveData) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> liveData.observeForever(taskSteps1 -> {

        }));
    }
}
