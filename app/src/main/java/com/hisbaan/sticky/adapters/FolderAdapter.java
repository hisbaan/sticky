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
import com.hisbaan.sticky.models.FolderItem;
import com.hisbaan.sticky.models.SquareCardView;

import java.util.ArrayList;

/**
 * Controls the order of things added into the recycler view
 */
public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {
    private ArrayList<FolderItem> folderItems;
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
     * @param folderItems List of items to be added to the recycler view.
     */
    public FolderAdapter(ArrayList<FolderItem> folderItems) {
        this.folderItems = folderItems;
    } //End constructor FolderAdapter.

    /**
     * Inflates and returns the recycler view.
     *
     * @param parent   The parent activity that the recycler view is called from.
     * @param viewType The type of view that the recycler view is.
     * @return A view holder that will be used later on.
     */
    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_item, parent, false);
        return new FolderViewHolder(v, listener);
    } //End method onCreateViewHolder.

    /**
     * Puts items in the appropriate positions of the recycler view.
     *
     * @param holder   The holder that was returned in the previous method.
     * @param position The position on the recycler view that the items will be put into.
     */
    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        FolderItem currentItem = folderItems.get(position);

        holder.imageView1.setImageBitmap(currentItem.getImageBitmap1());
        holder.imageView2.setImageBitmap(currentItem.getImageBitmap2());
        holder.imageView3.setImageBitmap(currentItem.getImageBitmap3());
        holder.imageView4.setImageBitmap(currentItem.getImageBitmap4());
        holder.folderName.setText(currentItem.getName());
    } //End method onBindViewHolder.

    /**
     * Returns the size of the recycler view.
     *
     * @return The size of the recycler view.
     */
    @Override
    public int getItemCount() {
        return folderItems.size();
    } //End method getItemCount.

    /**
     * Adds the items to the recycler view.
     */
    static class FolderViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        //Declaring variables that will be initialized via the array list that was created earlier.
        ImageView imageView1;
        ImageView imageView2;
        ImageView imageView3;
        ImageView imageView4;
        TextView folderName;
        SquareCardView squareCardView;

        /**
         * Constructor that sets local variables based on array list.
         *
         * @param itemView view of the recycler view.
         */
        FolderViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            imageView1 = itemView.findViewById(R.id.image_view_1);
            imageView2 = itemView.findViewById(R.id.image_view_2);
            imageView3 = itemView.findViewById(R.id.image_view_3);
            imageView4 = itemView.findViewById(R.id.image_view_4);
            folderName = itemView.findViewById(R.id.folder_name);
            squareCardView = itemView.findViewById(R.id.folder_layout);

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
        } //End constructor FolderViewHolder.

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), 121, 0, "Delete");
            menu.add(this.getAdapterPosition(), 122, 1, "Rename");
            menu.add(this.getAdapterPosition(), 123, 2, "Clear Folder");
        }
    } //End class FolderViewHolder.
} //End class FolderAdapter.
