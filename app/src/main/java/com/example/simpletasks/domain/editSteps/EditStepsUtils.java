package com.example.simpletasks.domain.editSteps;

import android.content.Intent;

import com.example.simpletasks.data.types.TaskStepTypes;

/**
 * Interface of the edit steps utility class.
 */
public interface EditStepsUtils {

    /**
     * Get the intent required to create a new task step for a specified type.
     *
     * @param type the type we want to create a task step of
     * @return the intent to start the required activity
     * @throws IllegalArgumentException if the passed type is not known (not AUDIO, VIDEO or TEXT)
     */
    Intent getHandlerIntent(TaskStepTypes type) throws IllegalArgumentException;
}
