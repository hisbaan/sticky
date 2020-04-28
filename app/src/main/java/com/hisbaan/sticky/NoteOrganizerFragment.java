package com.hisbaan.sticky;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

        //Getting the location of the Pictures directory.
        File directoryToSearch = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        //Creating an array of directory names in the Pictures directory.
        assert directoryToSearch != null;
        File[] tempDirs = directoryToSearch.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        //Getting a list of the files inside of the directories that end in .jpg for every single directory then creating an object to go along with it.
        assert tempDirs != null;
        for (File tempDir : tempDirs) {
            File[] tempImages = tempDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.substring(name.length() - 4).equals(".jpg");
                }
            });

            //Declaring the variables.
            Bitmap imageBitmap1;
            Bitmap imageBitmap2;
            Bitmap imageBitmap3;
            Bitmap imageBitmap4;
            String name;

            //If there are less than 1 images found, set it to null so it can be filled with a blank.
            assert tempImages != null;
            if (tempImages.length >= 1 && tempImages[0] != null) {
                imageBitmap1 = BitmapFactory.decodeFile(tempImages[0].toString());
            } else {
                imageBitmap1 = null;
            }

            //If there are less than 2 images found, set it to null so it can be filled with a blank.
            if (tempImages.length >= 2 && tempImages[1] != null) {
                imageBitmap2 = BitmapFactory.decodeFile(tempImages[1].toString());
            } else {
                imageBitmap2 = null;
            }

            //If there are less than 3 images found, set it to null so it can be filled with a blank.
            if (tempImages.length >= 3 && tempImages[2] != null) {
                imageBitmap3 = BitmapFactory.decodeFile(tempImages[2].toString());
            } else {
                imageBitmap3 = null;
            }

            //If there are less than 4 images found, set it to null so it can be filled with a blank.
            if (tempImages.length >= 4 && tempImages[3] != null) {
                imageBitmap4 = BitmapFactory.decodeFile(tempImages[3].toString());
            } else {
                imageBitmap4 = null;
            }

            //Get only the directory name (not the entire path) to pass it to the object.
            String[] tempName = tempDir.toString().split("/");
            name = tempName[tempName.length - 1];

            //Get the null color based on day or night mode.
            int nullColor = ContextCompat.getColor(requireContext(), R.color.colorSubText);

            //Create the object and add it to the array list.
            folderItems.add(new FolderItem(imageBitmap1, imageBitmap2, imageBitmap3, imageBitmap4, name, nullColor));
        }

        //Creating the recycler view and adding it to the current fragment.
        RecyclerView recyclerView = requireView().findViewById(R.id.folders);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        RecyclerView.Adapter adapter = new FolderAdapter(folderItems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    } //End Method onViewCreated.
} //End Class NoteOrganizerFragment.
