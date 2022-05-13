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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpletasks.EditTaskActivity;
import com.example.simpletasks.MainActivity;
import com.example.simpletasks.R;
import com.example.simpletasks.TaskGuideActivity;
import com.example.simpletasks.data.entities.Task;
import com.example.simpletasks.data.entities.TaskStep;
import com.example.simpletasks.data.entities.TaskWithSteps;
import com.example.simpletasks.data.viewmodels.TaskViewModel;

import java.util.List;

public class ManageTaskListAdapter extends RecyclerView.Adapter<ManageTaskListAdapter.TaskListViewHolder> {

    /**
     * this handler is a layer between the code and the xml layout. it fetches the View elements for
     * setting them in the adapter
     */
    class TaskListViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTask;
        private final TextView countStepsIndicator;
        private final ImageView taskImage;
        private final ImageButton editButton;
        private final ImageButton deleteButton;


        private TaskListViewHolder(View itemView) {
            super(itemView);
            titleTask = itemView.findViewById(R.id.titleTask_editTasks);
            countStepsIndicator = itemView.findViewById(R.id.countStepsIndicator_editTask);
            taskImage = itemView.findViewById(R.id.taskImage_editTasks);
            editButton = itemView.findViewById(R.id.editTaskButton_editTask);
            deleteButton = itemView.findViewById(R.id.deleteTaskButton_editTask);
        }
    }

    private static final String TAG = "ManageTaskListAdapter";
    private final LayoutInflater mInflater;
    private Context context;
    private Fragment fragment;
    private List<TaskWithSteps> tasks;

    public ManageTaskListAdapter(Context context, Fragment fragment) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.fragment_manage_task_list_row_layout, parent, false);
        context = mInflater.getContext();
        return new TaskListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {
        if (tasks != null) {
            TaskWithSteps currentTaskWithSteps = tasks.get(position);
            Task currentTask = currentTaskWithSteps.getTask();
            List<TaskStep> currentSteps = currentTaskWithSteps.getSteps();
            holder.titleTask.setText(currentTask.getTitle());
            holder.countStepsIndicator.setText(context.getString(R.string.total_steps, currentSteps.size()));
            holder.taskImage.setImageResource(R.drawable.ic_launcher_background/*TODO change */);
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
            holder.deleteButton.setOnClickListener(v -> {
                TaskViewModel taskViewModel = new ViewModelProvider(fragment).get(TaskViewModel.class);
                taskViewModel.deleteTask(currentTaskWithSteps);
                Log.d(TAG, "deleted task '" + currentTask.getTitle() + "' finished");
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