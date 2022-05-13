package com.example.simpletasks.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpletasks.R;
import com.example.simpletasks.data.entities.TaskStep;

import java.util.List;

public class EditTaskStepsListAdapter extends RecyclerView.Adapter<EditTaskStepsListAdapter.TaskListViewHolder> {

    /**
     * this handler is a layer between the code and the xml layout. it fetches the View elements for
     * setting them in the adapter
     */
    class TaskListViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton dragTaskStepButton;
        private final TextView titleTaskStep;
        private final TextView taskStepType;
        private final ImageView taskImage;
        private final ImageButton editButton;
        private final ImageButton deleteButton;


        private TaskListViewHolder(View itemView) {
            super(itemView);
            dragTaskStepButton = itemView.findViewById(R.id.dragTaskStepButton_editTask);
            titleTaskStep = itemView.findViewById(R.id.taskStepTitle_editTask);
            taskStepType = itemView.findViewById(R.id.taskStepType_editTask);
            taskImage = itemView.findViewById(R.id.taskStepImage_editTask);
            editButton = itemView.findViewById(R.id.editTaskStepButton_editTask);
            deleteButton = itemView.findViewById(R.id.deleteTaskStepButton_editTask);
        }
    }

    private static final String TAG = "EditTaskStepsListAdapter";
    private final LayoutInflater mInflater;
    private Context context;
    private List<TaskStep> taskSteps;

    public EditTaskStepsListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.fragment_edit_task_step_list_row_layout, parent, false);
        context = mInflater.getContext();
        return new TaskListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {
        if (taskSteps != null) {
            TaskStep currentTaskStep = taskSteps.get(position);
            holder.dragTaskStepButton.setOnClickListener(v -> {
                //TODO
            });
            holder.titleTaskStep.setText(currentTaskStep.getTitle());
            holder.taskStepType.setText(context.getString(R.string.type, currentTaskStep.getType()));
            holder.taskImage.setImageResource(R.drawable.ic_launcher_background/*TODO change */);
            holder.editButton.setOnClickListener(v -> {
                //todo start new intent which goes to edit task step
                /*Intent intent = new Intent(context, EditTaskActivity.class);
                intent.putExtra(MainActivity.TASK_INTENT_EXTRA, currentTaskStep);
                context.startActivity(intent);*/
            });
            holder.deleteButton.setOnClickListener(v -> {
                //todo implement deletion of step
            });
        } else {
            // Covers the case of data not being ready yet.
            holder.titleTaskStep.setText(R.string.placeholder);
            holder.taskStepType.setText(R.string.placeholder);
            holder.taskImage.setImageResource(R.drawable.image_placeholder);
        }

    }

    public void setTaskSteps(List<TaskStep> taskSteps) {
        this.taskSteps = taskSteps;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (taskSteps != null)
            return taskSteps.size();
        else return 0;
    }
}