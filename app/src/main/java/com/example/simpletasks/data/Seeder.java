package com.example.simpletasks.data;

import android.util.Log;

import com.example.simpletasks.data.domains.TaskStepTypes;
import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Seeder {
    String TAG = "Seeder";

    private Seeder(){};

    public static List<Task> createSeed(int nbrOfTasks){
        return new Seeder().getSeed(nbrOfTasks);
    }

    private List<Task> getSeed(int quantity){
        Log.d(TAG, "Creating seed of size " + quantity);

        List<Task> tasks = new ArrayList<>();
        Date today = new Date();

        for(int i = 0; i < quantity; i++){
            String title = "Task Title " + i;
            String description = "Task Description " + i;
            Date nextStart = new Date(today.getYear(), today.getMonth(), today.getDay() + i, today.getHours(), today.getMinutes());
            Long interval = 3*24*60*60L;
            Long notificationDelta = 3*60*60L;
            Date endDate = new Date(today.getYear(), today.getMonth(), today.getDay() + i + 7, today.getHours(), today.getMinutes());

            tasks.add(new Task(title, description, nextStart, interval, notificationDelta, endDate));
        }

        addSteps(tasks);

        Log.d(TAG, "" + tasks.get(0).getSteps().size());

        return tasks;
    }

    private void addSteps(List<Task> tasks){
        Log.d(TAG, "Add steps to " + tasks.size() + " tasks.");

        for(int i = 0; i < tasks.size(); i++){
            Task task = tasks.get(i);
            List<TaskStep> steps = new ArrayList<>();

            String taskId = task.getId();


            int nbrOfSteps = (int) (Math.random() * 5 + 5);

            Log.d(TAG, taskId + ": " + nbrOfSteps + " steps.");
            for(int j = 0; j < nbrOfSteps; j++){
                TaskStepTypes type = TaskStepTypes.values()[(int) (Math.random() * 3)];
                int index = j;
                String title = String.format("Step Title %s.%s", i, j);
                String imageUri = String.format("Step Image URI %s.%s", i, j);
                String description = String.format("Step Description %s.%s", i, j);
                String videoUri = String.format("Step Video URI %s.%s", i, j);
                String audioUri = String.format("Step Audio URI %s.%s", i, j);

                steps.add(index, new TaskStep(taskId, type, index, title, imageUri, description, videoUri, audioUri));
            }

            tasks.get(i).setSteps(steps);
        }
    }
}
