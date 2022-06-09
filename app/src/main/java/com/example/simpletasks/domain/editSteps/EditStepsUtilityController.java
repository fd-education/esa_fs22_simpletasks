package com.example.simpletasks.domain.editSteps;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.simpletasks.EditAudioStepActivity;
import com.example.simpletasks.EditTextStepActivity;
import com.example.simpletasks.EditVideoStepActivity;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.types.TaskStepTypes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class EditStepsUtilityController implements EditStepsUtility {
    private static final String TAG = "EditStepsUtility";

    private final Context context;

    public EditStepsUtilityController(Context context) {
        this.context = context;
    }

    @Override
    public Intent getHandlerIntent(TaskStepTypes type) throws IllegalArgumentException {
        switch (type) {
            case TEXT:
                return new Intent(context, EditTextStepActivity.class);
            case AUDIO:
                return new Intent(context, EditAudioStepActivity.class);
            case VIDEO:
                return new Intent(context, EditVideoStepActivity.class);

            default:
                throw new IllegalArgumentException("Not a known task step format.");
        }
    }

    public static List<TaskStep> getSortedTaskSteps(List<String> order, List<TaskStep> taskSteps) {
        if (order == null || order.isEmpty()) {
            Log.d(TAG, "No order to restore. Order by index from database.");
            return taskSteps;
        }

        Log.d(TAG, "Restoring order from list");
        List<TaskStep> steps = new ArrayList<>();

        for (String id : order) {
            for (TaskStep step : taskSteps) {
                if (step.getId().equals(id)) {
                    steps.add(step);
                    taskSteps.remove(step);
                    break;
                }
            }
        }

        taskSteps.addAll(steps);

        return taskSteps;
    }

    public static List<String> getListOfStepIds(String listOfSortedTaskStepIdsJson) {
        if (listOfSortedTaskStepIdsJson == null || listOfSortedTaskStepIdsJson.isEmpty()) {
            return null;
        }

        Gson gson = new Gson();
        return gson.fromJson(listOfSortedTaskStepIdsJson, new TypeToken<List<String>>() {
        }.getType());
    }

    public static String getStepsListJsonString(List<TaskStep> taskSteps) {
        List<String> listOfSortedTaskStepIds = new ArrayList<>();

        for (TaskStep taskStep : taskSteps) {
            listOfSortedTaskStepIds.add(taskStep.getId());
        }

        Gson gson = new Gson();
        return gson.toJson(listOfSortedTaskStepIds);
    }
}
