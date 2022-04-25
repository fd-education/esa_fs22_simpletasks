package com.example.simpletasks.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.simpletasks.R;
import com.example.simpletasks.TaskGuideActivity;
import com.example.simpletasks.TaskTestToDelete;

import java.util.ArrayList;

public class TaskListAdapter implements ListAdapter {
    ArrayList<TaskTestToDelete> arrayList;
    Context context;

    public TaskListAdapter(Context context, ArrayList<TaskTestToDelete> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskTestToDelete task = arrayList.get(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.task_list_row_layout, null);
            convertView.setOnClickListener(v -> {
                //when clicked on a list item, execute following code
                Intent intent = new Intent(context, TaskGuideActivity.class);
                context.startActivity(intent);
            });
            TextView title = convertView.findViewById(R.id.titleTask);
            TextView stepCounter = convertView.findViewById(R.id.countStepsIndicator);
            title.setText(task.getTitle());
            stepCounter.setText(context.getString(R.string.total_steps, task.getCountersteps()));
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return arrayList.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
