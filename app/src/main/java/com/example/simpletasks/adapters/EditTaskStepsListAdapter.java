package com.example.simpletasks.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpletasks.MainActivity;
import com.example.simpletasks.R;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.types.TaskStepTypes;
import com.example.simpletasks.data.viewmodels.TaskStepViewModel;
import com.example.simpletasks.domain.dragdrop.TaskStepsDrag;
import com.example.simpletasks.domain.editSteps.EditStepsUtils;
import com.example.simpletasks.domain.editSteps.EditStepsUtilsController;

import java.util.List;

/**
 * Adapter to handle the display of task steps for the editing screen.
 */
public class EditTaskStepsListAdapter extends RecyclerView.Adapter<EditTaskStepsListAdapter.TaskStepListViewHolder> implements TaskStepsDrag.CallbackContract {

    /**
     * TaskListViewHolder acts as a layer between code and xml layout.
     * Fetches View elements to set them in the adapter.
     */
    public static class TaskStepListViewHolder extends RecyclerView.ViewHolder {
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
    private final Fragment fragment;
    private TaskStepsDrag.DragHandleCallback dragHandle;
    private List<TaskStep> taskSteps;
    private final EditStepsUtils editStepsUtility;

    public EditTaskStepsListAdapter(Context context, Fragment fragment) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.fragment = fragment;

        if (fragment instanceof TaskStepsDrag.DragHandleCallback) {
            dragHandle = (TaskStepsDrag.DragHandleCallback) fragment;
        }

        editStepsUtility = new EditStepsUtilsController(context);
    }

    /**
     * Triggered when the RecyclerView needs a new ViewHolder to display a task step.
     *
     * @param parent   ViewGroup to add the new View to
     * @param viewType type of the view that is created
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
     * @param holder   the element the data gets bound on
     * @param position the global position of the view
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull TaskStepListViewHolder holder, int position) {

        if (taskSteps != null) {
            TaskStep currentTaskStep = taskSteps.get(position);

            holder.dragTaskStepButton.setOnTouchListener((view, event) -> {
                // make click sound at touch
                view.performClick();

                // request the drag if the event is a long touch on the drag button
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    dragHandle.requestDrag(holder);
                    return true;
                }

                return false;
            });

            holder.titleTaskStep.setText(currentTaskStep.getTitle());
            holder.taskStepType.setText(context.getString(R.string.type, currentTaskStep.getType()));

            // set the preview image in the task view
            if (taskSteps.get(position).getTypeAsTaskStepType() == TaskStepTypes.VIDEO) {
                holder.taskImage.setImageResource(R.drawable.ic_baseline_video_library_48);
            } else if (taskSteps.get(position).getImagePath() != null && !taskSteps.get(position).getImagePath().isEmpty()) {
                holder.taskImage.setImageURI(Uri.parse(taskSteps.get(position).getImagePath()));
            } else {
                holder.taskImage.setImageResource(R.drawable.image_placeholder);
            }

            // go to the edit screen for the current step format
            holder.editButton.setOnClickListener(v -> {
                Intent intent = editStepsUtility.getHandlerIntent(currentTaskStep.getTypeAsTaskStepType());
                intent.putExtra(MainActivity.TASK_INTENT_EXTRA, currentTaskStep);
                context.startActivity(intent);
            });

            holder.deleteButton.setOnClickListener(v -> {
                        TaskStepViewModel viewModel = new ViewModelProvider(fragment).get(TaskStepViewModel.class);
                        viewModel.deleteTaskStep(currentTaskStep);
                    }
            );
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
    @SuppressLint("NotifyDataSetChanged")
    public void setTaskSteps(final List<TaskStep> taskSteps) {
        Log.d(TAG, "Setting task steps: " + taskSteps.toString());

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
        return taskSteps != null ? taskSteps.size() : 0;
    }

    /**
     * Trigger the notification to tell the adapter, that the item is moving,
     * when the user drags it.
     *
     * @param fromPosition initial position
     * @param toPosition target position
     */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Log.d(TAG, String.format("DRAGGING: Moving %s from %d to %d", taskSteps.get(toPosition).getTitle(), fromPosition, toPosition));

        notifyItemMoved(fromPosition, toPosition);
    }
}