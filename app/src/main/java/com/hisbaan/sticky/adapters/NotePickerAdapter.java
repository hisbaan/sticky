package com.hisbaan.sticky.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hisbaan.sticky.R;
import com.hisbaan.sticky.models.InsideFolderItem;

import java.util.ArrayList;

/**
 * Controls the order of the items added into the recycler view.
 */
public class NotePickerAdapter extends RecyclerView.Adapter<NotePickerAdapter.NotePickerViewHolder> {
    private ArrayList<InsideFolderItem> InsideFolderItems;
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
     * @param NotePickerItems List of items to be added to the recycler view.
     */
    public NotePickerAdapter(ArrayList<InsideFolderItem> InsideFolderItems) {
        this.InsideFolderItems = InsideFolderItems;
    } //End constructor NotePickerAdapter.

    /**
     * Inflates and returns the recycler view.
     *
     * @param parent   The parent activity that the recycler view is called from.
     * @param viewType The type of view that the recycler view is.
     * @return A view holder that will be used later on.
     */
    @NonNull
    @Override
    public NotePickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inside_folder_item, parent, false);
        return new NotePickerViewHolder(view, listener);
    } //End method onCreateViewHolder.

    /**
     * Puts items in the appropriate positions of the recycler view.
     *
     * @param holder   The holder that was returned in the previous method.
     * @param position The position on the recycler view that the items will be put into.
     */
    @Override
    public void onBindViewHolder(@NonNull NotePickerViewHolder holder, int position) {
        InsideFolderItem currentItem = InsideFolderItems.get(position);

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
        return InsideFolderItems.size();
    } //End method getItemCount.

    /**
     * Adds the items to the recycler view.
     */
    static class NotePickerViewHolder extends RecyclerView.ViewHolder {

        //Declaring variables that will be initialized via the array list that was created earlier.
        ImageView imageView;
        TextView textView;

        /**
         * Constructor that sets local variables based on array list.
         *
         * @param itemView view of the recycler view.
         */
        NotePickerViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.note_image);
            textView = itemView.findViewById(R.id.note_name);

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
        } //End constructor NotePickerViewHolder.
    } //End class NotePickerViewHolder.
} //End class NotePickerAdapter.
