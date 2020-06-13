package com.hisbaan.sticky.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hisbaan.sticky.R;
import com.hisbaan.sticky.adapters.MoveAdapter;
import com.hisbaan.sticky.models.MoveItem;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 * Activity that lets the user select which folder to move a note to.
 */
public class MoveActivity extends AppCompatActivity implements MoveAdapter.OnItemClickListener {
    ArrayList<MoveItem> moveItems = new ArrayList<>();

    /**
     * onCreate method that initializes the recycler view.
     *
     * @param savedInstanceState Saved instance state that can be used to gather information from previous runs.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);

        setTitle("Move to");

        //Initializing toolbar and setting up the back button.
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(MoveActivity.this);
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

        //Getting the location of the Pictures directory.
        File directoryToSearch = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        //Creating an array of directory names in the Pictures directory.
        assert directoryToSearch != null;
        final File[] boardNames = directoryToSearch.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        assert boardNames != null;
        for (File boardName : boardNames) {
            String[] boardNamesSplit = boardName.toString().split("/");
            moveItems.add(new MoveItem(boardNamesSplit[boardNamesSplit.length - 1]));
        }

        //Initializing the recycler view.
        RecyclerView moveRecyclerView = findViewById(R.id.move_recycler_view);
        MoveAdapter moveAdapter = new MoveAdapter(moveItems);
        RecyclerView.LayoutManager moveLayoutManager = new LinearLayoutManager(this);
        moveRecyclerView.setLayoutManager(moveLayoutManager);
        moveRecyclerView.setAdapter(moveAdapter);
        moveAdapter.setOnItemClickListener(this);
    } //End method onCreate.

    /**
     * Method that runs when an item in the recycler view is clicked and sends the name back to the previous activity.
     *
     * @param position The position in the array that the clicked item is at.
     */
    @Override
    public void onItemClick(int position) {
        String result = moveItems.get(position).getBoardName();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("result", result);

        setResult(RESULT_OK, resultIntent);
        finish();
    } //End method onItemClick.
} //End class MoveActivity.