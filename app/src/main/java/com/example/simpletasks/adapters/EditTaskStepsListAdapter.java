package com.example.simpletasks.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpletasks.MainActivity;
import com.example.simpletasks.R;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.domain.editSteps.EditStepsUtility;
import com.example.simpletasks.domain.editSteps.EditStepsUtilityController;

import java.util.List;

/**
 * Adapter to handle the display of task steps for the editing screen.
 */
public class EditTaskStepsListAdapter extends RecyclerView.Adapter<EditTaskStepsListAdapter.TaskStepListViewHolder> {

    /**
     * TaskListViewHolder acts as a layer between code and xml layout.
     * Fetches View elements to set them in the adapter.
     */
    static class TaskStepListViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton dragTaskStepButton;
        private final TextView titleTaskStep;
        private final TextView taskStepType;
        private final ImageView taskImage;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

        /**
         * Constructor for TaskListViewHolder
         * Sets all View elements for the adapter.
         *
         * @param itemView the View from which to get the elements
         */
        private TaskStepListViewHolder(View itemView) {
            super(itemView);
            dragTaskStepButton = itemView.findViewById(R.id.dragTaskStepButton_editTask);
            titleTaskStep = itemView.findViewById(R.id.taskStepTitle_editTask);
            taskStepType = itemView.findViewById(R.id.taskStepType_editTask);
            taskImage = itemView.findViewById(R.id.taskStepImage_editTask);
            editButton = itemView.findViewById(R.id.editTaskStepButton_editTask);
            deleteButton = itemView.findViewById(R.id.deleteTaskStepButton_editTask);
        }
    }

    private static final String TAG = "EditTaskStepsListAdap";
    private final LayoutInflater mInflater;
    private Context context;
    private List<TaskStep> taskSteps;
    private final EditStepsUtility editStepsUtility;

    public EditTaskStepsListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;

        editStepsUtility = new EditStepsUtilityController(context);
    }

    /**
     * Triggered when the RecyclerView needs a new ViewHolder to display a task step.
     *
     * @param parent ViewGroup to add the new View to
     * @param viewType type of the view that is created
     *
     * @return the new ViewHolder
     */
    @NonNull
    @Override
    public TaskStepListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.fragment_edit_task_step_list_row_layout, parent, false);
        context = mInflater.getContext();
        return new TaskStepListViewHolder(itemView);
    }

    /**
     * Replace task steps on the screen by recycling views.
     * Update the tasks steps whilst the user is scrolling through them.
     *
     * @param holder the element the data gets bound on
     * @param position the global position of the view
     */
    @Override
    public void onBindViewHolder(@NonNull TaskStepListViewHolder holder, int position) {
        if (taskSteps != null) {
            TaskStep currentTaskStep = taskSteps.get(position);
            holder.dragTaskStepButton.setOnClickListener(v -> {
                // TODO Implement drag to position
            });
            holder.titleTaskStep.setText(currentTaskStep.getTitle());
            holder.taskStepType.setText(context.getString(R.string.type, currentTaskStep.getType()));

            if(taskSteps.get(position).getImagePath() != null){
                Log.e(TAG, Uri.parse(taskSteps.get(position).getImagePath()).toString());
                holder.taskImage.setImageURI(Uri.parse(taskSteps.get(position).getImagePath()));
            } else {
                holder.taskImage.setImageResource(R.drawable.image_placeholder);
            }

            // Go to the edit screen corresponding to the current step format
            holder.editButton.setOnClickListener(v -> {
                // TODO start new intent which goes to edit task step
                Intent intent = editStepsUtility.getHandlerIntent(currentTaskStep.getTypeAsTaskStepType());

                intent.putExtra(MainActivity.TASK_INTENT_EXTRA, currentTaskStep);
                context.startActivity(intent);
            });

            holder.deleteButton.setOnClickListener(v -> {
                // TODO implement deletion of step
            });
        } else {
            // Handle the case of data not being ready yet
            holder.titleTaskStep.setText(R.string.placeholder);
            holder.taskStepType.setText(R.string.placeholder);
            holder.taskImage.setImageResource(R.drawable.image_placeholder);
        }

    }

    /**
     * Set the task steps and notify registered observers.
     *
     * @param taskSteps the task steps to set
     */
    public void setTaskSteps(final List<TaskStep> taskSteps) {
        this.taskSteps = taskSteps;
        notifyDataSetChanged();
    }

    /**
     * Get the number of task steps.
     *
     * @return int for number of task steps
     */
    @Override
    public int getItemCount() {
        return taskSteps != null? taskSteps.size() : 0;
    }
}