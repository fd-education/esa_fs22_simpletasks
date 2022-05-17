package com.example.simpletasks.domain.editSteps;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.simpletasks.data.types.TaskStepTypes;

public interface EditStepsUtility {
    Intent getHandlerIntent(TaskStepTypes type) throws IllegalArgumentException;
}
