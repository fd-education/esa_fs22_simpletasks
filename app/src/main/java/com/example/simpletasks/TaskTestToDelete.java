package com.example.simpletasks;

import java.util.ArrayList;

public class TaskTestToDelete {
    private String title;
    private ArrayList<TaskStepTestToDelete> steps;
    private int countersteps;

    public TaskTestToDelete(String title, ArrayList<TaskStepTestToDelete> steps) {
        this.title = title;
        this.steps = steps;
        this.countersteps = steps.size();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<TaskStepTestToDelete> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<TaskStepTestToDelete> steps) {
        this.steps = steps;
    }

    public int getCountersteps() {
        return countersteps;
    }

    public void setCountersteps(int countersteps) {
        this.countersteps = countersteps;
    }
}
