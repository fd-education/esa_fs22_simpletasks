package com.example.simpletasks.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.simpletasks.TaskGuideActivity;
import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.entities.TaskWithSteps;

import java.util.Date;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder> {
    /**
     * this handler is a layer between the code and the xml layout. it fetches the View elements for
     * setting them in the adapter
     */
    class TaskListViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTask;
        private final TextView countStepsIndicator;
        private final ImageView taskImage;
        private final ImageButton skipTaskButton;


        private TaskListViewHolder(View itemView) {
            super(itemView);
            titleTask = itemView.findViewById(R.id.titleTask_taskList);
            countStepsIndicator = itemView.findViewById(R.id.countStepsIndicator_taskList);
            taskImage = itemView.findViewById(R.id.taskImage_taskList);
            skipTaskButton = itemView.findViewById(R.id.skipTaskButton_taskList);
        }
    }

    private final LayoutInflater mInflater;
    private Context context;
    private List<TaskWithSteps> tasks;

    public TaskListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.fragment_task_list_row_layout, parent, false);
        context = mInflater.getContext();
        return new TaskListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {
        if (tasks != null) {
            TaskWithSteps taskWithSteps = tasks.get(position);
            Task currentTask = taskWithSteps.getTask();
            List<TaskStep> currentSteps = taskWithSteps.getSteps();
            holder.titleTask.setText(currentTask.getTitle());
            holder.countStepsIndicator.setText(context.getString(R.string.total_steps, currentSteps.size()));
            holder.taskImage.setImageResource(R.drawable.ic_launcher_background/*TODO change */);
            holder.itemView.setOnClickListener(v -> {
                //when clicked on a list item, execute following code
                Intent intent = new Intent(context, TaskGuideActivity.class);
                intent.putExtra(MainActivity.TASK_INTENT_EXTRA, taskWithSteps);
                context.startActivity(intent);
            });
            holder.skipTaskButton.setOnClickListener(v -> {
                long newStartLong = currentTask.getNextStartDate().getTime() + currentTask.getInterval();
                Date newStartDate = new Date(newStartLong);
                currentTask.setNextStartDate(newStartDate);
                MainActivity.updateTasksInDatabase(tasks);
            });
        } else {
            // Covers the case of data not being ready yet.
            holder.titleTask.setText(R.string.placeholder);
            holder.countStepsIndicator.setText(R.string.placeholder);
            holder.taskImage.setImageResource(R.drawable.image_placeholder);
        }

    }

    public void setTasks(List<TaskWithSteps> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (tasks != null)
            return tasks.size();
        else return 0;
    }
}