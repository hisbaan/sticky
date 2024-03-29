package com.hisbaan.sticky.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hisbaan.sticky.R;

import java.util.ArrayList;

/**
 * Controls the order of the items added into the recycler view.
 */
public class MoveAdapter extends RecyclerView.Adapter<MoveAdapter.MoveViewHolder> {
    private final ArrayList<String> MoveItems;
    private OnItemClickListener listener;

    /**
     * Interface to make the OnItemClickListener work.
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    } //End interface OnItemClickListener.

    /**
     * Setter method that sets the listener.
     *
     * @param listener OnItemClickListener passed to the method.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    } //End method setOnItemClickListener.

    /**
     * Constructor that passes the array list of items to this adapter to be added to the recycler view.
     *
     * @param MoveItems List of items to be added to the recycler view.
     */
    public MoveAdapter(ArrayList<String> MoveItems) {
        this.MoveItems = MoveItems;
    } //End constructor MoveAdapter.

    /**
     * Inflates and returns the recycler view.
     *
     * @param parent   The parent activity that the recycler view is called from.
     * @param viewType The type of view that the recycler view is.
     * @return A view holder that will be used later on.
     */
    @NonNull
    @Override
    public MoveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.move_item, parent, false);
        return new MoveViewHolder(view, listener);
    } //End method onCreateViewHolder.

    /**
     * Puts items in the appropriate positions of the recycler view.
     *
     * @param holder   The holder that was returned in the previous method.
     * @param position The position on the recycler view that the items will be put into.
     */
    @Override
    public void onBindViewHolder(@NonNull MoveViewHolder holder, int position) {
        String currentItem = MoveItems.get(position);

        holder.boardName.setText(currentItem);
    } //End method onBindViewHolder.

    /**
     * Returns the size of the recycler view.
     *
     * @return The size of the recycler view.
     */
    @Override
    public int getItemCount() {
        return MoveItems.size();
    } //End method getItemCount.

    /**
     * Adds the items to the recycler view.
     */
    static class MoveViewHolder extends RecyclerView.ViewHolder {

        //Declaring variables that will be initialized via the array list that was created earlier.
        TextView boardName;

        /**
         * Constructor that sets local variables based on array list.
         *
         * @param itemView view of the recycler view.
         */
        MoveViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            boardName = itemView.findViewById(R.id.board_name);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        } //End constructor MoveViewHolder.
    } //End class MoveViewHolder.
} //End class MoveAdapter.
