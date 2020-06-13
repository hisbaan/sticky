package com.hisbaan.sticky.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hisbaan.sticky.R;
import com.hisbaan.sticky.adapters.InsideFolderAdapter;
import com.hisbaan.sticky.models.InsideFolderItem;
import com.hisbaan.sticky.utils.Refactor;
import com.hisbaan.sticky.utils.RenameDialog;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Activity to display the inside of a folder and manipulate the notes inside of it.
 */
public class InsideFolderActivity extends AppCompatActivity implements InsideFolderAdapter.OnItemClickListener, RenameDialog.RenameDialogListener {
    ArrayList<InsideFolderItem> insideFolderItems = new ArrayList<>();
    InsideFolderAdapter insideFolderAdapter;
    int renameIndex;

    public static final int REQUEST_NEW_BOARD = 10;

    /**
     * onCreate method that initializes the variables and sets up the recycler view.
     *
     * @param savedInstanceState Saved instance state that can be used to gather information from previous runs.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_folder);

        //Setting the title of the activity.
        setTitle(getIntent().getStringExtra("folder_name"));

        //Initializing toolbar and setting up the back button.
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(InsideFolderActivity.this);
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
        RecyclerView insideFolderRecyclerView = findViewById(R.id.inside_folder_recycler_view);
        insideFolderAdapter = new InsideFolderAdapter(insideFolderItems);
        RecyclerView.LayoutManager insideFolderGridLayoutManager = new GridLayoutManager(this, 2);
        insideFolderRecyclerView.setLayoutManager(insideFolderGridLayoutManager);
        insideFolderRecyclerView.setAdapter(insideFolderAdapter);
        insideFolderAdapter.setOnItemClickListener(this);
    } //End method onCreate.

    /**
     * Method that runs when a context menu item is selected and runs code based on which one was.
     *
     * @param item Item in the recycler view that the context menu item was triggered on.
     * @return Whether or not an item was interacted with.
     */
    @Override
    public boolean onContextItemSelected(@NonNull final MenuItem item) {
        switch (item.getItemId()) {
            case 121: //If deleted button is pressed, get user confirmation then delete the note.
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Confirm Delete");
                alert.setMessage("Are you sure you want to delete?\nThis action cannot be undone");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + getIntent().getStringExtra("folder_name") + "/" + insideFolderItems.get(item.getGroupId()).getName() + ".jpg").delete()) {
                            System.out.println("File deleted successfully");
                        } else {
                            System.out.println("File deletion failed.");
                        }
                        insideFolderItems.remove(item.getGroupId());
                        insideFolderAdapter.notifyItemRemoved(item.getGroupId());
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alert.show();

                return true;
            case 122: //If rename button is pressed, get the new name from the user then rename the note.
                renameIndex = item.getGroupId();
                RenameDialog renameDialog = new RenameDialog();
                renameDialog.show(getSupportFragmentManager(), "rename dialog");
                return true;
            case 123: //If the move button is pressed, get where the user wants to move it then move it.
                renameIndex = item.getGroupId();
                Intent intent = new Intent(this, MoveActivity.class);
                startActivityForResult(intent, REQUEST_NEW_BOARD);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }//End method onContextItemSelected.

    /**
     * Runs appropriate code when an activity returns.
     *
     * @param requestCode Code that the activity was requested with to differentiate it from other returns.
     * @param resultCode  Code that determines whether or not the action was completed.
     * @param data        Data that was returned.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NEW_BOARD && resultCode == RESULT_OK && data != null) {
            File oldBoardName = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + Objects.requireNonNull(getIntent().getStringExtra("folder_name")) + "/" + insideFolderItems.get(renameIndex).getName() + ".jpg");
            File result = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + Objects.requireNonNull(data.getStringExtra("result")) + "/" + insideFolderItems.get(renameIndex).getName() + ".jpg");

            if (oldBoardName.renameTo(result)) {
                System.out.println("File moved successfully");
            } else {
                System.out.println("File moving failed");
            }

            insideFolderItems.remove(renameIndex);
            insideFolderAdapter.notifyItemRemoved(renameIndex);
        }
    } //End method onActivityResult.

    /**
     * Runs when the dialog for the rename note option is selected.
     *
     * @param newName The new name of the note that is chosen.
     */
    @Override
    public void applyText(String newName) {
        System.out.println(newName);
        File currentFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + getIntent().getStringExtra("folder_name") + "/" + insideFolderItems.get(renameIndex).getName() + ".jpg");
        File newFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + getIntent().getStringExtra("folder_name") + "/" + newName + ".jpg");

        if (newFile.exists()) {
            Toast.makeText(this, "File already exists", Toast.LENGTH_SHORT).show();
        } else {
            if (currentFile.renameTo(newFile)) {
                System.out.println("File renamed successfully.");
            } else {
                System.out.println("File renaming failed.");
            }
        }

        Refactor refactor = new Refactor();
        refactor.renameNote(this, getIntent().getStringExtra("folder_name"), insideFolderItems.get(renameIndex).getName(), newName);

        insideFolderItems.get(renameIndex).setName(newName);
        insideFolderAdapter.notifyItemChanged(renameIndex);
    } //End method applyText.

    /**
     * Method that runs when a note is clicked on.
     *
     * @param position The position in the array that the interacted note is at.
     */
    @Override
    public void onItemClick(int position) {

    } //End method onItemClick.
} //End class InsideFolderActivity.
