package com.example.simpletasks.domain.popups;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.example.simpletasks.EditAudioStepActivity;
import com.example.simpletasks.EditTextStepActivity;
import com.example.simpletasks.EditVideoStepActivity;
import com.example.simpletasks.MainActivity;
import com.example.simpletasks.R;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.types.TaskStepTypes;

/**
 * Class for the type choice dialog.
 */
public class ChooseTypeDialog {
    // the context for the dialog
    private final Context context;

    public ChooseTypeDialog(Context context){
        this.context = context;
    }

    /**
     * Show a dialog that offers a choice of step types and
     * routes to the corresponding edit step activity.
     *
     * @param taskId the id of the task to assign the step to
     * @param stepIndex the index of the newly created step
     */
    public void showDialog(String taskId, int stepIndex){


        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.popup_chose_type);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.findViewById(R.id.b_choosetype_textstep).setOnClickListener(view -> {
            TaskStep textStep = TaskStep.getEmptyStep(taskId, TaskStepTypes.TEXT, stepIndex);
            Intent textIntent = new Intent(context, EditTextStepActivity.class);
            textIntent.putExtra(MainActivity.TASK_INTENT_EXTRA, textStep);
            context.startActivity(textIntent);
            dialog.dismiss();
        });

        dialog.findViewById(R.id.b_choosetype_audiostep).setOnClickListener(view -> {
            TaskStep audioStep = TaskStep.getEmptyStep(taskId, TaskStepTypes.AUDIO, stepIndex);
            Intent audioIntent = new Intent(context, EditAudioStepActivity.class);
            audioIntent.putExtra(MainActivity.TASK_INTENT_EXTRA, audioStep);
            context.startActivity(audioIntent);
            dialog.dismiss();
        });

        dialog.findViewById(R.id.b_choosetype_videostep).setOnClickListener(view -> {
            TaskStep videoStep = TaskStep.getEmptyStep(taskId, TaskStepTypes.VIDEO, stepIndex);
            Intent videoIntent = new Intent(context, EditVideoStepActivity.class);
            videoIntent.putExtra(MainActivity.TASK_INTENT_EXTRA, videoStep);
            context.startActivity(videoIntent);
            dialog.dismiss();
        });

        dialog.findViewById(R.id.b_choosetype_cancel).setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

}
