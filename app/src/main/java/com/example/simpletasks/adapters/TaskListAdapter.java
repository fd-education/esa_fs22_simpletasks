package com.example.simpletasks.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpletasks.R;
import com.example.simpletasks.TaskGuideActivity;
import com.example.simpletasks.data.entity.Task;

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


        private TaskListViewHolder(View itemView) {
            super(itemView);
            titleTask = itemView.findViewById(R.id.titleTask);
            countStepsIndicator = itemView.findViewById(R.id.countStepsIndicator);
            taskImage = itemView.findViewById(R.id.taskImage);
        }
    }

    private final LayoutInflater mInflater;
    private Context context;
    private List<Task> tasks;

    public TaskListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.task_list_row_layout, parent, false);
        context = mInflater.getContext();
        return new TaskListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {
        if (tasks != null) {
            Task current = tasks.get(position);
            holder.titleTask.setText(current.getTitle());
            holder.countStepsIndicator.setText(context.getString(R.string.total_steps, current.getSteps().size()));
            holder.taskImage.setImageResource(R.drawable.ic_launcher_background/*TODO change */);
            holder.itemView.setOnClickListener(v -> {
                //when clicked on a list item, execute following code
                Intent intent = new Intent(context, TaskGuideActivity.class);
                //intent.putExtra(TaskGuideActivity.TASK_INTENT_EXTRA, current.getId());
                intent.putExtra(TaskGuideActivity.TASK_INTENT_EXTRA, current);
                context.startActivity(intent);
            });
        } else {
            // Covers the case of data not being ready yet.
            holder.titleTask.setText(R.string.placeholder);
            holder.countStepsIndicator.setText(R.string.placeholder);
            holder.taskImage.setImageResource(R.drawable.image_placeholder);
        }

    }

    public void setTasks(List<Task> tasks) {
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