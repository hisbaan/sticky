package com.hisbaan.sticky.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hisbaan.sticky.R;
import com.hisbaan.sticky.activities.InsideFolderActivity;
import com.hisbaan.sticky.activities.MainActivity;
import com.hisbaan.sticky.adapters.FolderAdapter;
import com.hisbaan.sticky.models.FolderItem;
import com.hisbaan.sticky.utils.RenameDialog;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Fragment that sits inside of the main activity to display different things.
 */
public class NoteOrganizerFragment extends Fragment {
    private PopupWindow popupWindow;
    private LayoutInflater popupLayoutInflater;
    public static FolderAdapter folderAdapter;
    public static ArrayList<FolderItem> folderItems;
    public static int renameIndex;

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
    } //End method onCreateView.

    /**
     * Similar to an onCreateMethod that runs code when the fragment is created.
     *
     * @param view               The view that is being created.
     * @param savedInstanceState Saved instance state that can contain information from previous runs.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Setting the title of the toolbar.
        requireActivity().setTitle("Note Organizer");

        //Creating an array list of items to be added into the recycler view and then adding items to that list.
        folderItems = new ArrayList<>();

        //Getting the location of the Pictures directory.
        File directoryToSearch = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        //Creating an array of directory names in the Pictures directory.
        assert directoryToSearch != null;
        final File[] tempDirs = directoryToSearch.listFiles(new FileFilter() {
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
        RecyclerView folderRecyclerView = requireView().findViewById(R.id.folder_recycler_view);
        final RecyclerView.LayoutManager folderGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        folderAdapter = new FolderAdapter(folderItems);
        folderRecyclerView.setHasFixedSize(true);
        folderRecyclerView.setLayoutManager(folderGridLayoutManager);
        folderRecyclerView.setAdapter(folderAdapter);

        folderAdapter.setOnItemClickListener(new FolderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(requireContext(), InsideFolderActivity.class);
                intent.putExtra("folder_name", folderItems.get(position).getName());
                startActivity(intent);
            }
        });
    } //End method onViewCreated.

    /**
     * Method that runs when a context menu is selected.
     * @param item The item on which the context menu was triggered on.
     * @return Whether or not the action was completed.
     */
    @Override
    public boolean onContextItemSelected(@NonNull final MenuItem item) {
        switch (item.getItemId()) {
            case 121: //Ask for confirmation then delete the folder.
                AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
                alert.setTitle("Confirm Delete");
                alert.setMessage("Are you sure you want to delete?\nThis action cannot be undone");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            FileUtils.deleteDirectory(new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + folderItems.get(item.getGroupId()).getName()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        folderItems.remove(item.getGroupId());
                        folderAdapter.notifyItemRemoved(item.getGroupId());
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alert.show();

                return true;
            case 122: //Get the new name for the board then rename it.
                MainActivity.applyTextState = MainActivity.NOTE_ORGANIZER_ACTIVITY;
                renameIndex = item.getGroupId();
                RenameDialog renameDialog = new RenameDialog();
                renameDialog.show(requireActivity().getSupportFragmentManager(), "rename dialog");
                return true;
            case 123: //Clear the inside of a folder.
                //TODO delete items inside of the folder
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    } //End method onContextItemSelected.

    /**
     * Method that removes a given item from the recycler view with an animation.
     * @param position The position in the array of the item to be removed.
     */
    private void removeItem(int position) {
        try {
            FileUtils.deleteDirectory(new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + folderItems.get(position).getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        folderItems.remove(position);
        folderAdapter.notifyItemRemoved(position);
    } //End method removeItem.
} //End class NoteOrganizerFragment.
