package com.hisbaan.sticky.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hisbaan.sticky.R;
import com.hisbaan.sticky.adapters.NotePickerAdapter;
import com.hisbaan.sticky.models.InsideFolderItem;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

/**
 * Activity that displays the notes in a given folder and returns the clicked on note to the parent activity.
 */
public class NotePickerActivity extends AppCompatActivity implements NotePickerAdapter.OnItemClickListener {
    ArrayList<InsideFolderItem> insideFolderItems;

    /**
     * onCreate method that initializes things and displays the note recycler view.
     *
     * @param savedInstanceState Bundle where information from previous runs can be recovered from.
     */
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

        //Getting a list of the notes that need to be displayed.
        insideFolderItems = new ArrayList<>();

        File directoryToSearch = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + getIntent().getStringExtra("folder_name"));
        File[] images = directoryToSearch.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.substring(name.length() - 4).equals(".jpg");
            }
        });

        assert images != null;
        for (File image : images) {
            String[] nameArray = image.toString().split("/");
            String name = nameArray[nameArray.length - 1].substring(0, nameArray[nameArray.length - 1].length() - 4);

            insideFolderItems.add(new InsideFolderItem(BitmapFactory.decodeFile(image.toString()), name));
        }

        //Initializing the recycler view.
        RecyclerView insideFolderRecyclerView = findViewById(R.id.note_picker_recycler_view);
        NotePickerAdapter insideFolderAdapter = new NotePickerAdapter(insideFolderItems);
        RecyclerView.LayoutManager insideFolderGridLayoutManager = new GridLayoutManager(this, 2);
        insideFolderRecyclerView.setLayoutManager(insideFolderGridLayoutManager);
        insideFolderRecyclerView.setAdapter(insideFolderAdapter);
        insideFolderAdapter.setOnItemClickListener(this);
    } //End method onCreate.

    /**
     * onItemClick method that runs when an item from the recycler view is clicked.
     *
     * @param position The position in the array where the clicked item is.
     */
    @Override
    public void onItemClick(int position) {
        String result = getIntent().getStringExtra("folder_name") + "," + insideFolderItems.get(position).getName();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("result", result);

        //TODO find bug where null is created if you exit out of the folder/note select with the back arrow
        System.out.println("THIS IS A THING " + result);

        setResult(RESULT_OK, resultIntent);
        finish();
    } //End method onItemClick.
} //End class NotePickerActivity.