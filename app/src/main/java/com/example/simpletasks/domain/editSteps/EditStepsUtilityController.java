package com.example.simpletasks.domain.editSteps;

import android.content.Context;
import android.content.Intent;

import com.example.simpletasks.EditAudioStepActivity;
import com.example.simpletasks.EditTextStepActivity;
import com.example.simpletasks.EditVideoStepActivity;
import com.example.simpletasks.data.types.TaskStepTypes;

public class EditStepsUtilityController implements EditStepsUtility{
    private final Context context;

    public EditStepsUtilityController(Context context){
        this.context = context;
    }

    @Override
    public Intent getHandlerIntent(TaskStepTypes type) throws IllegalArgumentException{
        switch(type){
            case TEXT: return new Intent(context, EditTextStepActivity.class);
            case AUDIO: return new Intent(context, EditAudioStepActivity.class);
            case VIDEO: return new Intent(context, EditVideoStepActivity.class);

            default: throw new IllegalArgumentException("Not a known task step format.");
        }
    }
}
