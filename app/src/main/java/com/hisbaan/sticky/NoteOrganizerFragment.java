package com.hisbaan.sticky;

//Android imports.

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//Java imports.
import java.util.ArrayList;

/**
 * Fragment that sits inside of the main activity to display different things.
 */
public class NoteOrganizerFragment extends Fragment {

    /**
     * Inflates the fragment when it is created.
     *
     * @param inflater           Layout inflater.
     * @param container          The activity that the fragment is inside.
     * @param savedInstanceState Saved instance state that can contain information from previous runs.
     * @return Returns an inflated fragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_organizer, container, false);
    }

    /**
     * Similar to an onCreateMethod that runs code when the fragment is created.
     *
     * @param view               The view that is being created.
     * @param savedInstanceState Saved instance state that can contain information from previous runs.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Creating an array list of items to be added into the recycler view and then adding items to that list.
        ArrayList<FolderItem> folderItems = new ArrayList<>();
        folderItems.add(new FolderItem(R.drawable.ic_apps, "Line 1", "Line 2"));
        folderItems.add(new FolderItem(R.drawable.ic_add, "Line 3", "Line 4"));
        folderItems.add(new FolderItem(R.drawable.ic_dashboard, "Line 5", "Line 6"));

        //Creating the recycler view and adding it to the current fragment.
        RecyclerView recyclerView = requireView().findViewById(R.id.folders);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.Adapter adapter = new FolderAdapter(folderItems);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
