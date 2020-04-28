package com.hisbaan.sticky;

//Android imports

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

/**
 * Controls the order of things added into the recycler view
 */
public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {
    private ArrayList<FolderItem> folderItems;

    /**
     * Constructor that passes the array list of items to this adapter to be added to the recycler view.
     *
     * @param folderItems List of items to be added to the recycler view.
     */
    public FolderAdapter(ArrayList<FolderItem> folderItems) {
        this.folderItems = folderItems;
    }

    /**
     * Adds the items to the recycler view.
     */
    static class FolderViewHolder extends RecyclerView.ViewHolder {

        //Declaring variables that will be initialized via the array list that was created earlier.
        ImageView imageView;
        TextView textView1;
        TextView textView2;

        /**
         * Constructor that sets local variables based on array list.
         *
         * @param itemView view of the recycler view.
         */
        FolderViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            textView1 = itemView.findViewById(R.id.text_view);
            textView2 = itemView.findViewById(R.id.text_view_2);
        }
    }

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_layout, parent, false);
        return new FolderViewHolder(v);
    }

    /**
     * Puts items in the appropriate positions of the recycler view.
     *
     * @param holder   The holder that was returned in the previous method.
     * @param position The position on the recycler view that the items will be put into.
     */
    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        FolderItem currentItem = folderItems.get(position);

        holder.imageView.setImageResource(currentItem.getImageResource());
        holder.textView1.setText(currentItem.getText1());
        holder.textView2.setText(currentItem.getText2());
    }

    /**
     * Returns the size of the recycler view.
     *
     * @return The size of the recycler view.
     */
    @Override
    public int getItemCount() {
        return folderItems.size();
    }
}
