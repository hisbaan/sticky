package com.hisbaan.sticky.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hisbaan.sticky.R;
import com.hisbaan.sticky.adapters.InsideFolderAdapter;
import com.hisbaan.sticky.models.InsideFolderItem;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class NotePickerActivity extends AppCompatActivity implements InsideFolderAdapter.OnItemClickListener {
    ArrayList<InsideFolderItem> insideFolderItems;

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

        RecyclerView insideFolderRecyclerView = findViewById(R.id.note_picker_recycler_view);
        InsideFolderAdapter insideFolderAdapter = new InsideFolderAdapter(insideFolderItems);
        RecyclerView.LayoutManager insideFolderGridLayoutManager = new GridLayoutManager(this, 2);
        insideFolderRecyclerView.setLayoutManager(insideFolderGridLayoutManager);
        insideFolderRecyclerView.setAdapter(insideFolderAdapter);
        insideFolderAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int position) {
        String result =  getIntent().getStringExtra("folder_name") + "," + insideFolderItems.get(position).getName();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("result", result);

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
