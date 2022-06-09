package com.example.simpletasks.domain.editSteps;

import android.content.Intent;

import com.example.simpletasks.data.types.TaskStepTypes;

public interface EditStepsUtility {
    Intent getHandlerIntent(TaskStepTypes type) throws IllegalArgumentException;
}
