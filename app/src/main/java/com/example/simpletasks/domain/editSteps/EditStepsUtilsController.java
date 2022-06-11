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

/**
 * Utility class for functions used when editing task steps.
 */
public class EditStepsUtilsController implements EditStepsUtils {
    private static final String TAG = "EditStepsUtility";

    private final Context context;

    public EditStepsUtilsController(Context context) {
        this.context = context;
    }

    /**
     * Returns the intent to handle the task steps of a specified type.
     * @param type the type we want to create a task step of
     * @return intent to handle task steps
     * @throws IllegalArgumentException if the passed type is not known (not AUDIO, VIDEO or TEXT)
     */
    @Override
    public Intent getHandlerIntent(TaskStepTypes type) throws IllegalArgumentException {
        switch (type) {
            case TEXT:
                Log.d(TAG, "Returned intent for a TEXT step.");
                return new Intent(context, EditTextStepActivity.class);
            case AUDIO:
                Log.d(TAG, "Returned intent for an AUDIO step.");
                return new Intent(context, EditAudioStepActivity.class);
            case VIDEO:
                Log.d(TAG, "Returned intent for a VIDEO step.");
                return new Intent(context, EditVideoStepActivity.class);

            default:
                throw new IllegalArgumentException("Not a known task step format.");
        }
    }

    /**
     * Get the passed list of taskSteps sorted according to a passed list containing ids of taskSteps.
     * Steps that are not in the order list are appended at the front of the returned list.
     *
     * @param order list containing step ids in the desired order
     * @param taskSteps list of task steps to order according to the order list
     * @return list of task steps ordered according to the order list
     */
    public static List<TaskStep> getSortedTaskSteps(List<String> order, List<TaskStep> taskSteps) {
        // return the task steps unchanged if no order was passed
        if (order == null || order.isEmpty()) {
            Log.d(TAG, "No order to restore. Order by index from database.");
            return taskSteps;
        }

        Log.d(TAG, "Restoring order from list");
        List<TaskStep> steps = new ArrayList<>();

        // order the steps
        for (String id : order) {
            for (TaskStep step : taskSteps) {
                if (step.getId().equals(id)) {
                    steps.add(step);
                    taskSteps.remove(step);
                    break;
                }
            }
        }

        steps.addAll(taskSteps);

        return steps;
    }

    /**
     * Parse a gson String of a list containing task ids to an ArrayList of task ids.
     *
     * @param listOfSortedTaskStepIdsJson the String JSON version of the list
     * @return the list object containing the ids
     */
    public static List<String> getListOfStepIds(String listOfSortedTaskStepIdsJson) {
        // return null if the passed list does not contain anything
        if (listOfSortedTaskStepIdsJson == null || listOfSortedTaskStepIdsJson.isEmpty()) {
            return null;
        }

        // parse the stringified JSON to a List object with Strings (= task ids)
        Gson gson = new Gson();
        return gson.fromJson(listOfSortedTaskStepIdsJson, new TypeToken<List<String>>() {
        }.getType());
    }

    /**
     * Parse a List of task steps to a stringified JSON.
     * @param taskSteps the list object to parse to a stringified JSON
     * @return String representation of the task id list
     */
    public static String getStepsListJsonString(List<TaskStep> taskSteps) {
        List<String> listOfSortedTaskStepIds = new ArrayList<>();

        for (TaskStep taskStep : taskSteps) {
            listOfSortedTaskStepIds.add(taskStep.getId());
        }

        Gson gson = new Gson();
        return gson.toJson(listOfSortedTaskStepIds);
    }
}
