package com.hisbaan.sticky;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
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
        //TODO system for inserting information information into list here.
        //Find directory, scan through the first 4 images and set them. If there are less than 4, then set them as blank images.
        //directory name is the name argument.
        File directoryToSearch = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        assert directoryToSearch != null;
        File[] tempDirs = directoryToSearch.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        assert tempDirs != null;
        for (File tempDir : tempDirs) {
            File[] tempImages = tempDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.substring(name.length() - 4).equals(".jpg");
                }
            });

            Bitmap imageBitmap1;
            Bitmap imageBitmap2;
            Bitmap imageBitmap3;
            Bitmap imageBitmap4;
            String name;

            assert tempImages != null;
            if (tempImages.length >= 1 && tempImages[0] != null) {
                imageBitmap1 = BitmapFactory.decodeFile(tempImages[0].toString());
            } else {
                imageBitmap1 = null;
            }

            if (tempImages.length >= 2 && tempImages[1] != null) {
                imageBitmap2 = BitmapFactory.decodeFile(tempImages[1].toString());
            } else {
                imageBitmap2 = null;
            }

            if (tempImages.length >= 3 && tempImages[2] != null) {
                imageBitmap3 = BitmapFactory.decodeFile(tempImages[2].toString());
            } else {
                imageBitmap3 = null;
            }

            //TODO going out of bounds so make it so that it will only go into the statement if the length allows it
            //like tempImages.length >= 4 && tempImages[3]

            if (tempImages.length >= 4 && tempImages[3] != null) {
                imageBitmap4 = BitmapFactory.decodeFile(tempImages[3].toString());
            } else {
                imageBitmap4 = null;
            }

            String[] tempName = tempDir.toString().split("/");
            name = tempName[tempName.length - 1];

            folderItems.add(new FolderItem(imageBitmap1, imageBitmap2, imageBitmap3, imageBitmap4, name));
        }

        //create an array list of the names of the directories
        //scan through each one in a fori for 4
        //if there are less than 4, pass null, the other side will handle it.

        //Creating the recycler view and adding it to the current fragment.
        RecyclerView recyclerView = requireView().findViewById(R.id.folders);
        recyclerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        RecyclerView.Adapter adapter = new FolderAdapter(folderItems);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
