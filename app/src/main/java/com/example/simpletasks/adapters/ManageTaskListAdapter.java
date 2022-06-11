package com.example.simpletasks.adapters;

import android.annotation.SuppressLint;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpletasks.DialogBuilder;
import com.example.simpletasks.EditTaskActivity;
import com.example.simpletasks.MainActivity;
import com.example.simpletasks.R;
import com.example.simpletasks.TaskGuideActivity;
import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.viewmodels.TaskViewModel;

import java.util.List;

/**
 * Adapter to handle the display of tasks for the manage tasks screen.
 */
public class ManageTaskListAdapter extends RecyclerView.Adapter<ManageTaskListAdapter.TaskListViewHolder> {

    /**
     * TaskListViewHolder acts as a layer between code and xml layout. Fetches View elements to set
     * them in the adapter.
     */
    static class TaskListViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTask;
        private final TextView countStepsIndicator;
        private final ImageView taskImage;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

        /**
         * Constructor for TaskListViewHolder Sets all View elements for the adapter.
         *
         * @param itemView the View from which to get the elements
         */
        private TaskListViewHolder(View itemView) {
            super(itemView);
            titleTask = itemView.findViewById(R.id.titleTask_editTasks);
            countStepsIndicator = itemView.findViewById(R.id.countStepsIndicator_editTasks);
            taskImage = itemView.findViewById(R.id.taskImage_editTasks);
            editButton = itemView.findViewById(R.id.editTaskButton_editTasks);
            deleteButton = itemView.findViewById(R.id.deleteTaskButton_editTasks);
        }
    }

    private static final String TAG = "ManageTaskListAdapter";
    private final LayoutInflater mInflater;
    private Context context;
    private final Fragment fragment;
    private List<TaskWithSteps> tasks;

    public ManageTaskListAdapter(Context context, Fragment fragment) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.fragment = fragment;
    }

    /**
     * Triggered when the RecyclerView needs a new ViewHolder to display a task.
     *
     * @param parent   ViewGroup to add the new View to
     * @param viewType type of the view that is created
     * @return the new ViewHolder
     */
    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.fragment_manage_task_list_row_layout, parent, false);
        context = mInflater.getContext();
        return new TaskListViewHolder(itemView);
    }

    /**
     * Replace tasks on the screen by recycling views. Update the tasks whilst the user is scrolling
     * through them.
     *
     * @param holder   the element the data gets bound on
     * @param position the global position of the view
     */
    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {
        if (tasks != null) {
            TaskWithSteps currentTaskWithSteps = tasks.get(position);
            Task currentTask = currentTaskWithSteps.getTask();
            List<TaskStep> currentSteps = currentTaskWithSteps.getSteps();
            holder.titleTask.setText(currentTask.getTitle());
            holder.countStepsIndicator.setText(context.getString(R.string.total_steps, currentSteps.size()));

            if (currentTask.getTitleImagePath().isEmpty()) {
                holder.taskImage.setImageResource(R.drawable.image_placeholder/*TODO change */);
            } else {
                holder.taskImage.setImageURI(Uri.parse(currentTask.getTitleImagePath()));
            }

            holder.itemView.setOnClickListener(v -> {
                //when clicked on a list item, execute following code
                Intent intent = new Intent(context, TaskGuideActivity.class);
                intent.putExtra(MainActivity.TASK_INTENT_EXTRA, currentTaskWithSteps);
                context.startActivity(intent);
            });
            holder.editButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditTaskActivity.class);
                intent.putExtra(MainActivity.TASK_INTENT_EXTRA, currentTaskWithSteps);
                context.startActivity(intent);
            });
            holder.deleteButton.setOnClickListener(v ->
                    //create a dialog asking the user if he wants to delete the task
                    new DialogBuilder()
                            .setDescriptionText(R.string.delete_task_popup_text)
                            .setContext(context)
                            .setTwoButtonLayout(R.string.cancel_popup, R.string.delete_task_popup_button)
                            .setAction(() -> {
                                //delete task
                                TaskViewModel taskViewModel = new ViewModelProvider(fragment).get(TaskViewModel.class);
                                taskViewModel.deleteTask(currentTaskWithSteps);
                                Log.d(TAG, "deleted task '" + currentTask.getTitle() + "' finished");
                            }).build().show()
            );
        } else {
            // Covers the case of data not being ready yet.
            holder.titleTask.setText(R.string.placeholder);
            holder.countStepsIndicator.setText(R.string.placeholder);
            holder.taskImage.setImageResource(R.drawable.image_placeholder);
        }

    }

    /**
     * Set the tasks and notify registered observers.
     *
     * @param tasks the task steps to set
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setTasks(final List<TaskWithSteps> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    /**
     * Get the number of tasks.
     *
     * @return int for number of tasks
     */
    @Override
    public int getItemCount() {
        if (tasks != null)
            return tasks.size();
        else return 0;
    }
}