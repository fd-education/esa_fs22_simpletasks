package com.example.simpletasks.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpletasks.DialogBuilder;
import com.example.simpletasks.MainActivity;
import com.example.simpletasks.R;
import com.example.simpletasks.TaskGuideActivity;
import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.viewmodels.TaskViewModel;

import java.util.Date;
import java.util.List;

/**
 * Adapter to handle the display a list of tasks.
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder> {

    /**
     * TaskListViewHolder acts as a layer between code and xml layout. Fetches View elements to set
     * them in the adapter.
     */
    static class TaskListViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTask;
        private final TextView countStepsIndicator;
        private final ImageView taskImage;
        private final ImageButton skipTaskButton;

        /**
         * Constructor for TaskListAdapter Sets all View elements for the adapter.
         *
         * @param itemView the View from which to get the elements
         */
        private TaskListViewHolder(View itemView) {
            super(itemView);
            titleTask = itemView.findViewById(R.id.titleTask_taskList);
            countStepsIndicator = itemView.findViewById(R.id.countStepsIndicator_taskList);
            taskImage = itemView.findViewById(R.id.taskImage_taskList);
            skipTaskButton = itemView.findViewById(R.id.skipTaskButton_taskList);
        }
    }

    private static final String TAG = "TaskListAdapter";
    private final LayoutInflater mInflater;
    private Context context;
    private List<TaskWithSteps> tasks;
    private ViewModelStoreOwner owner;

    public TaskListAdapter(Context context, ViewModelStoreOwner owner) {
        mInflater = LayoutInflater.from(context);
        this.owner = owner;
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
        View itemView = mInflater.inflate(R.layout.fragment_task_list_row_layout, parent, false);
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
            TaskWithSteps taskWithSteps = tasks.get(position);
            Task currentTask = taskWithSteps.getTask();
            List<TaskStep> currentSteps = taskWithSteps.getSteps();
            holder.titleTask.setText(currentTask.getTitle());
            holder.countStepsIndicator.setText(context.getString(R.string.total_steps, currentSteps.size()));
            holder.taskImage.setImageResource(R.drawable.image_placeholder/*TODO change */);
            holder.itemView.setOnClickListener(v -> {
                //when clicked on a list item, execute following code
                Intent intent = new Intent(context, TaskGuideActivity.class);
                intent.putExtra(MainActivity.TASK_INTENT_EXTRA, taskWithSteps);
                context.startActivity(intent);
            });
            holder.skipTaskButton.setOnClickListener(v ->
                    new DialogBuilder()
                            .setDescriptionText(R.string.popup_skip_text)
                            .setContext(context)
                            .setTwoButtonLayout(R.string.cancel_popup, R.string.skip_task_popup)
                            .setAction(() -> {
                                //calculate and set new next start date
                                long newStartLong = currentTask.getNextStartDate().getTime() + currentTask.getInterval();
                                Date newStartDate = new Date(newStartLong);
                                currentTask.setNextStartDate(newStartDate);

                                //update the task in the database
                                TaskViewModel taskViewModel = new ViewModelProvider(owner).get(TaskViewModel.class);
                                taskViewModel.updateTask(currentTask);
                                Log.d(TAG, "updating task finished");
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