package com.hisbaan.sticky.adapters;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hisbaan.sticky.R;
import com.hisbaan.sticky.models.InsideFolderItem;
import com.hisbaan.sticky.models.SquareCardView;

import java.util.ArrayList;

/**
 * Controls the order of the items added into the recycler view.
 */
public class InsideFolderAdapter extends RecyclerView.Adapter<InsideFolderAdapter.InsideFolderViewHolder> {
    private ArrayList<InsideFolderItem> insideFolderItems;
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
     * @param insideFolderItems List of items to be added to the recycler view.
     */
    public InsideFolderAdapter(ArrayList<InsideFolderItem> insideFolderItems) {
        this.insideFolderItems = insideFolderItems;
    } //End constructor InsideFolderAdapter.

    /**
     * Inflates and returns the recycler view.
     *
     * @param parent   The parent activity that the recycler view is called from.
     * @param viewType The type of view that the recycler view is.
     * @return A view holder that will be used later on.
     */
    @NonNull
    @Override
    public InsideFolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inside_folder_item, parent, false);
        return new InsideFolderViewHolder(view, listener);
    } //End method onCreateViewHolder.

    /**
     * Puts items in the appropriate positions of the recycler view.
     *
     * @param holder   The holder that was returned in the previous method.
     * @param position The position on the recycler view that the items will be put into.
     */
    @Override
    public void onBindViewHolder(@NonNull InsideFolderViewHolder holder, int position) {
        InsideFolderItem currentItem = insideFolderItems.get(position);

        holder.imageView.setImageBitmap(currentItem.getImage());
        holder.textView.setText(currentItem.getName());
    } //End method onBindViewHolder.

    /**
     * Returns the size of the recycler view.
     *
     * @return The size of the recycler view.
     */
    @Override
    public int getItemCount() {
        return insideFolderItems.size();
    } //End method getItemCount.

    /**
     * Adds the items to the recycler view.
     */
    static class InsideFolderViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        //Declaring variables that will be initialized via the array list that was created earlier.
        ImageView imageView;
        TextView textView;
        SquareCardView insideFolderLayout;

        /**
         * Constructor that sets local variables based on array list.
         *
         * @param itemView view of the recycler view.
         */
        InsideFolderViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.note_image);
            textView = itemView.findViewById(R.id.note_name);
            insideFolderLayout = itemView.findViewById(R.id.inside_folder_layout);
            insideFolderLayout.setOnCreateContextMenuListener(this);

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
        } //End constructor InsideFolderViewHolder.

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), 121, 0, "Delete");
            menu.add(this.getAdapterPosition(), 122, 1, "Rename");
            menu.add(this.getAdapterPosition(), 123, 2, "Move");
        }
    } //End class InsideFolderViewHolder.
} //End class InsideFolderAdapter.
