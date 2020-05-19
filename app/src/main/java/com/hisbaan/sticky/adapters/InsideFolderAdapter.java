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

public class InsideFolderAdapter extends RecyclerView.Adapter<InsideFolderAdapter.InsideFolderViewHolder> {

    private ArrayList<InsideFolderItem> insideFolderItems;

    public InsideFolderAdapter(ArrayList<InsideFolderItem> insideFolderItems) {
        this.insideFolderItems = insideFolderItems;
    }

    @NonNull
    @Override
    public InsideFolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inside_folder_item, parent, false);
        return new InsideFolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InsideFolderViewHolder holder, int position) {
        InsideFolderItem currentItem = insideFolderItems.get(position);
        holder.imageView.setImageBitmap(currentItem.getImage());
        holder.textView.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return insideFolderItems.size();
    }

    static class InsideFolderViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        InsideFolderViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.note_image);
            textView = itemView.findViewById(R.id.note_name);
        }
    }
}