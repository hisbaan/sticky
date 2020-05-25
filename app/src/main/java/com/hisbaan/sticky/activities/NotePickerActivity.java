package com.hisbaan.sticky.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.hisbaan.sticky.R;
import com.hisbaan.sticky.adapters.FolderAdapter;
import com.hisbaan.sticky.models.FolderItem;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class NotePickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_picker);

        //Initializing toolbar and setting up the back button.
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(NotePickerActivity.this);
            }
        });

        //Sets status bar colour based on the current theme.
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        View decorView = getWindow().getDecorView();
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                break;
        }

        //Creating an array list of items to be added into the recycler view and then adding items to that list.
        final ArrayList<FolderItem> folderItems = new ArrayList<>();

        //Getting the location of the Pictures directory.
        File directoryToSearch = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

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
            int nullColor = ContextCompat.getColor(this, R.color.colorSubText);

            //Create the object and add it to the array list.
            folderItems.add(new FolderItem(imageBitmap1, imageBitmap2, imageBitmap3, imageBitmap4, name, nullColor));
        }

        RecyclerView folderRecyclerView = findViewById(R.id.note_picker_recycler_view);
        final RecyclerView.LayoutManager folderGridLayoutManager = new GridLayoutManager(NotePickerActivity.this, 2);
        FolderAdapter folderAdapter = new FolderAdapter(folderItems);
        folderRecyclerView.setHasFixedSize(true);
        folderRecyclerView.setLayoutManager(folderGridLayoutManager);
        folderRecyclerView.setAdapter(folderAdapter);
    }
}
