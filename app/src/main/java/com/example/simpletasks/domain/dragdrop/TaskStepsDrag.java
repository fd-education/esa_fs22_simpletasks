package com.example.simpletasks.domain.dragdrop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpletasks.adapters.EditTaskStepsListAdapter;

public class TaskStepsDrag extends ItemTouchHelper.Callback {
    private static int initialPosition = -1;

    public interface CallbackContract {
        void onItemMove(int fromPosition, int toPosition);
        void onItemClear();
    }

    public interface DragHandleCallback{
        void requestDrag(RecyclerView.ViewHolder viewHolder);
        void updateTaskSteps();
    }

    private final CallbackContract adapter;

    public TaskStepsDrag(CallbackContract adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        if(viewHolder instanceof EditTaskStepsListAdapter.TaskStepListViewHolder){
            EditTaskStepsListAdapter.TaskStepListViewHolder v = (EditTaskStepsListAdapter.TaskStepListViewHolder) viewHolder;
            initialPosition = v.getAdapterPosition();
        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if(viewHolder instanceof EditTaskStepsListAdapter.TaskStepListViewHolder){
            EditTaskStepsListAdapter.TaskStepListViewHolder v = (EditTaskStepsListAdapter.TaskStepListViewHolder) viewHolder;
            adapter.onItemClear();
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // Do nothing intentionally!
    }
}
