package com.hisbaan.sticky.adapters;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hisbaan.sticky.R;
import com.hisbaan.sticky.models.BoardItem;
import com.hisbaan.sticky.models.SquareCardView;

import java.util.ArrayList;

/**
 * Controls the order of items added into the recycler view
 */
public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> {
    private static ArrayList<BoardItem> boardItems;
    private OnItemClickListener listener;

    /**
     * Interface to make the OnItemClickListener work.
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    } //End Interface OnItemClickListener.

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
     * @param boardItems List of items to be added to the recycler view.
     */
    public BoardAdapter(ArrayList<BoardItem> boardItems) {
        this.boardItems = boardItems;
    } //End constructor BoardAdapter.

    /**
     * Inflates and returns the recycler view.
     *
     * @param parent   The parent activity that the recycler view is called from.
     * @param viewType The type of view that the recycler view is.
     * @return A view holder that will be used later on.
     */
    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item, parent, false);
        return new BoardViewHolder(v, listener);
    } //End method BoardViewHolder.

    /**
     * Puts items in the appropriate positions of the recycler view.
     *
     * @param holder   The holder that was returned in the previous method.
     * @param position The position on the recycler view that the items will be put into.
     */
    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        BoardItem currentItem = boardItems.get(position);

        holder.name.setText(currentItem.getBoardName());
    } //End method onBindViewHolder

    /**
     * Returns the size of the recycler view.
     *
     * @return The size of the recycler view.
     */
    @Override
    public int getItemCount() {
        return boardItems.size();
    } //End method getItemCount.

    /**
     * Adds the items to the recycler view.
     */
    static class BoardViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        //Declaring variables that will be initialized via the array list that was created earlier.
        TextView name;
        SquareCardView squareCardView;

        /**
         * Constructor that sets local variables based on array list.
         *
         * @param itemView view of the recycler view.
         */
        BoardViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            squareCardView = itemView.findViewById(R.id.board_layout_card_view);

            squareCardView.setOnCreateContextMenuListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        } //End constructor BoardViewHolder.

        /**
         * Creates the context menu on long press.
         *
         * @param menu The menu that is built.
         * @param v The view that the context menu is called on.
         * @param menuInfo Information about the menu.
         */
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if (getAdapterPosition() != boardItems.size() - 1) {
                menu.add(this.getAdapterPosition(), 121, 0, "Delete");
                menu.add(this.getAdapterPosition(), 122, 1, "Rename");
                menu.add(this.getAdapterPosition(), 123, 2, "Clear Board");
            }
        } //End method onCreateContextMenu.
    } //End class BoardViewHolder
} //End class BoardAdapter.