package com.example.simpletasks.data;

import android.util.Log;

import com.example.simpletasks.data.domains.TaskStepTypes;
import com.example.simpletasks.data.entities.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Seeder {
    String TAG = "Seeder";

    private Seeder() {
    }

    ;

    public static List<TaskWithSteps> createSeed(int nbrOfTasks) {
        return new Seeder().getSeed(nbrOfTasks);
    }

    private List<TaskWithSteps> getSeed(int quantity) {
        Log.d(TAG, "Creating seed of size " + quantity);

        List<TaskWithSteps> tasks = new ArrayList<>();
        Date today = new Date();

        for (int i = 0; i < quantity; i++) {
            String title = "Task Title " + i;
            String description = "Task Description " + i;
            Date nextStart = new Date(today.getYear(), today.getMonth(), today.getDate() + i, today.getHours() + 1, today.getMinutes());
            Long interval = 3 * 24 * 60 * 60L;
            Long notificationDelta = 3 * 60 * 60L;
            Date endDate = new Date(today.getYear(), today.getMonth(), today.getDate() + i + 7, today.getHours(), today.getMinutes());

            Task task = new Task(title, description, nextStart, interval, notificationDelta, endDate);
            addSteps(task);
            tasks.add(new TaskWithSteps(task, task.getSteps()));
        }


        Log.d(TAG, "" + tasks.get(0).getSteps().size());

        return tasks;
    }

    private void addSteps(Task task) {
        Log.d(TAG, "Add steps to " + task.getTitle() + ".");

        List<TaskStep> steps = new ArrayList<>();

        String taskId = task.getId();

        int nbrOfSteps = (int) (Math.random() * 5 + 5);
        Log.d(TAG, taskId + ": " + nbrOfSteps + " steps.");

        String i = taskId;
        for (int j = 0; j < nbrOfSteps; j++) {
            TaskStepTypes type = TaskStepTypes.values()[(int) (Math.random() * 3)];
            String title = String.format("Step Title %s.%s", i, j);
            String imageUri = String.format("Step Image URI %s.%s", i, j);
            String description = String.format("Step Description %s.%s", i, j);
            String videoUri = String.format("Step Video URI %s.%s", i, j);
            String audioUri = String.format("Step Audio URI %s.%s", i, j);

            steps.add(j, new TaskStep(taskId, type, j, title, imageUri, description, videoUri, audioUri));
        }

        task.setSteps(steps);
    }
}
