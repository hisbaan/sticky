package com.hisbaan.sticky.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hisbaan.sticky.R;
import com.hisbaan.sticky.adapters.FolderAdapter;
import com.hisbaan.sticky.adapters.InsideFolderAdapter;
import com.hisbaan.sticky.models.FolderItem;
import com.hisbaan.sticky.models.InsideFolderItem;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;

//TODO make toolbar here and in the other fragment that has a menu button

/**
 * Fragment that sits inside of the main activity to display different things.
 */
public class NoteOrganizerFragment extends Fragment {
    private PopupWindow popupWindow;
    private LayoutInflater popupLayoutInflater;
    View view;

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
        view = inflater.inflate(R.layout.fragment_note_organizer, container, false);
        return view;
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
        final ArrayList<FolderItem> folderItems = new ArrayList<>();

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
        FolderAdapter folderAdapter = new FolderAdapter(folderItems);
        folderRecyclerView.setHasFixedSize(true);
        folderRecyclerView.setLayoutManager(folderGridLayoutManager);
        folderRecyclerView.setAdapter(folderAdapter);

        folderAdapter.setOnItemClickListener(new FolderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                popupLayoutInflater = (LayoutInflater) requireContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert popupLayoutInflater != null;
                ViewGroup container = (ViewGroup) popupLayoutInflater.inflate(R.layout.popup_layout, null);
                ConstraintLayout layout = requireView().findViewById(R.id.popup_layout);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int x = displayMetrics.widthPixels;
                int y = displayMetrics.heightPixels;

                popupWindow = new PopupWindow(container, (int) (x * 0.8), (int) (y * 0.8), true);
                popupWindow.setAnimationStyle(R.style.popup_window_animation);
                popupWindow.showAtLocation(getView(), Gravity.NO_GRAVITY, (int) (x * 0.1), (int) (y * 0.1));
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);

                container.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });

                //Inside Folder Recycler View
                //TODO make second recyclerView here
                //TODO get the array of file directories from above and create a new item (add to the array list) with the bitmap and the name.

                ArrayList<InsideFolderItem> insideFolderItems = new ArrayList<>();

                File directoryToSearch = new File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + folderItems.get(position).getName());
                File[] images = directoryToSearch.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.substring(name.length() - 4).equals(".jpg");
                    }
                });

                assert images != null;
                for (int i = 0; i < images.length; i++) {
                    String[] nameArray = images[i].toString().split("/");

                    insideFolderItems.add(new InsideFolderItem(BitmapFactory.decodeFile(images[i].toString()), nameArray[nameArray.length - 1]));
                }

                RecyclerView insideFolderRecyclerView = requireView().findViewById(R.id.inside_folder_recycler_view);
                RecyclerView.Adapter insideFolderAdapter = new InsideFolderAdapter(insideFolderItems);
                final RecyclerView.LayoutManager insideFolderGridLayoutManager = new GridLayoutManager(getActivity(), 2);
                insideFolderRecyclerView.setLayoutManager(insideFolderGridLayoutManager);
                insideFolderRecyclerView.setAdapter(insideFolderAdapter);

//                openPopupWindow(folderItems.get(position).getName());
            }
        });
    } //End Method onViewCreated.

    public void openPopupWindow(String folderName) {
        ArrayList<InsideFolderItem> insideFolderItems = new ArrayList<>();

        File directoryToSearch = new File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + folderName);
        File[] images = directoryToSearch.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.substring(name.length() - 4).equals(".jpg");
            }
        });

        System.out.println(directoryToSearch);

        assert images != null;
        for (int i = 0; i < images.length; i++) {
            String[] nameArray = images[i].toString().split("/");
            System.out.println(images[i]);
            insideFolderItems.add(new InsideFolderItem(BitmapFactory.decodeFile(images[i].toString()), nameArray[nameArray.length - 1]));
        }

        if (getView().findViewById(R.id.inside_folder_recycler_view) != null) {
            System.out.println("#### VIEW NOT NULL ####");
            System.out.println(getView());
        } else {
            System.out.println("#### VIEW NULL ####");
            System.out.println(getView());
        }



        RecyclerView insideFolderRecyclerView = view.findViewById(R.id.inside_folder_recycler_view);
        RecyclerView.Adapter insideFolderAdapter = new InsideFolderAdapter(insideFolderItems);
        RecyclerView.LayoutManager insideFolderGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        insideFolderRecyclerView.setLayoutManager(insideFolderGridLayoutManager);
        insideFolderRecyclerView.setAdapter(insideFolderAdapter);
    }
} //End Class NoteOrganizerFragment.
