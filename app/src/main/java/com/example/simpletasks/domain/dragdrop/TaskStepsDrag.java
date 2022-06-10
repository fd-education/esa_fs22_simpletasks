package com.example.simpletasks.domain.dragdrop;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Class to setup the ItemTouchHelper the way it is required for SimpleTasks.
 */
public class TaskStepsDrag extends ItemTouchHelper.Callback {

    /**
     * Interface must be implemented by the adapter
     */
    public interface CallbackContract {
        void onItemMove(int fromPosition, int toPosition);
    }

    /**
     * Interface must be implemented by the fragment
     */
    public interface DragHandleCallback{
        void requestDrag(RecyclerView.ViewHolder viewHolder);
        void updateTaskSteps();
    }

    private final CallbackContract adapter;

    public TaskStepsDrag(CallbackContract adapter) {
        this.adapter = adapter;
    }

    /**
     * Disable long press as a trigger for the drag, as the drag is triggered by clicking an image button.
     *
     * @return false to disable drag on long press
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    /**
     * Disable swipe actions as we don't need them in simple tasks.
     *
     * @return false to disable swipe actions
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    /**
     * Set the required movement flags fpr the drag
     *
     * @param recyclerView the fragment that acts as a container for all views
     * @param viewHolder the view holders/ list items that become "draggable"
     * @return movement flags for the item touch helper
     */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * Triggered when an item is dragged.
     * Triggers the onItemMove method from the contract.
     *
     * @param recyclerView the fragment that contains the view holders
     * @param viewHolder the view holder/ list item that is being moved
     * @param target the target that the view is being moved to
     * @return true to indicate success
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    /**
     * Triggered when an item is swiped.
     * Left empty as there is no such functionality intended so far.
     *
     * @param viewHolder the view holder that was swiped
     * @param direction the direction of the swipe
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // Do nothing intentionally!
    }
}
