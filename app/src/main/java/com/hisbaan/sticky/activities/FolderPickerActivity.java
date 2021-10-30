package com.hisbaan.sticky.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hisbaan.sticky.R;
import com.hisbaan.sticky.adapters.FolderPickerAdapter;
import com.hisbaan.sticky.models.FolderItem;

import java.io.File;
import java.util.ArrayList;

/**
 * Activity that displays the folder and returns the clicked on folder to the parent.
 */
public class FolderPickerActivity extends AppCompatActivity implements FolderPickerAdapter.OnItemClickListener {
    ArrayList<FolderItem> folderItems;
    private static final int REQUEST_NEW_NOTE = 1;

    /**
     * onCreate method that initializes things and displays the recycler view.
     *
     * @param savedInstanceState Saved instance state that can be used to gather information from previous runs.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_picker);

        //Initializing toolbar and setting up the back button.
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> NavUtils.navigateUpFromSameTask(FolderPickerActivity.this));

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
        folderItems = new ArrayList<>();

        //Getting the location of the Pictures directory.
        File directoryToSearch = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        //Creating an array of directory names in the Pictures directory.
        assert directoryToSearch != null;
        final File[] tempDirs = directoryToSearch.listFiles(File::isDirectory);

        //Getting a list of the files inside of the directories that end in .jpg for every single directory then creating an object to go along with it.
        assert tempDirs != null;
        for (File tempDir : tempDirs) {
            File[] tempImages = tempDir.listFiles((dir, name) -> name.endsWith(".jpg"));

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

        //Initializing recycler view.
        RecyclerView folderRecyclerView = findViewById(R.id.folder_picker_recycler_view);
        final RecyclerView.LayoutManager folderGridLayoutManager = new GridLayoutManager(FolderPickerActivity.this, 2);
        FolderPickerAdapter folderAdapter = new FolderPickerAdapter(folderItems);
        folderRecyclerView.setHasFixedSize(true);
        folderRecyclerView.setLayoutManager(folderGridLayoutManager);
        folderRecyclerView.setAdapter(folderAdapter);
        folderAdapter.setOnItemClickListener(this);
    } //End method onClick.

    /**
     * onItemClick method that runs when a folder is clicked.
     *
     * @param position The position in the array that the clicked folder is located at.
     */
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, NotePickerActivity.class);
        intent.putExtra("folder_name", folderItems.get(position).getName());
        startActivityForResult(intent, REQUEST_NEW_NOTE);
    } //End method onItemClick.

    /**
     * Runs when an activity started for a result returns.
     *
     * @param requestCode The code of the request made.
     * @param resultCode  The result of the code. Either okay or cancelled.
     * @param data        The data returned.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NEW_NOTE && resultCode == RESULT_OK && data != null) {
            String result = data.getStringExtra("result");

            Intent resultIntent = new Intent();
            resultIntent.putExtra("result", result);

            System.out.println("THIS IS A THING " + result);

            setResult(RESULT_OK, resultIntent);
            finish();
        }
    } //End method onActivityResult.
} //End class FolderPickerActivity.