package com.example.simpletasks;

public class TaskStepTestToDelete {
    private int index;
    private String title;
    private String description;

    public TaskStepTestToDelete(int index, String title, String description) {
        this.index = index;
        this.title = title;
        this.description = description;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
