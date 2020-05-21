package com.hisbaan.sticky.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.hisbaan.sticky.R;
import com.hisbaan.sticky.utils.NewGroupDialog;

import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Allows the user to select the name and the group of the note that they've just captured.
 */
public class NamingActivity extends AppCompatActivity implements View.OnClickListener, NewGroupDialog.NewGroupDialogListener {
    //Declaring variables for layout.
    ImageView imageView;
    EditText nameTextField;
    Button cancelButton;
    Button continueButton;
    Button recropButton;
    Spinner spinner;
    String groupName;
    String imageName;
    Mat dstImage;
    //TODO make rotate buttons.

    /**
     * Initializes variables, displays image, displays spinner groups.
     *
     * @param savedInstanceState Saved instance state that can store information from previous runs.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naming);

        //Initializing toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(NamingActivity.this);
            }
        });
        setSupportActionBar(toolbar);

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

        //Getting the image from the previous activity.
//        long addr = getIntent().getLongExtra("dst_image_addr", 0);
//        Mat tempImg = new Mat(addr);
//        System.out.println(tempImg);
//        dstImage = tempImg.clone();

        dstImage = CropActivity.transferImage.clone();

        if (dstImage.empty()) {
            System.out.println("### EMPTY ###");
        } else {
            System.out.println(dstImage.cols() + " | " + dstImage.rows());
        }

        imageView = findViewById(R.id.image_view);
        nameTextField = findViewById(R.id.name_edit_text);
        spinner = findViewById(R.id.group_select_spinner);

        cancelButton = findViewById(R.id.cancel_button);
        continueButton = findViewById(R.id.continue_button);
        recropButton = findViewById(R.id.recrop_button);
        cancelButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
        recropButton.setOnClickListener(this);

        //Converting matrix to bitmap.
        Bitmap bmp = null;

        try {
            bmp = Bitmap.createBitmap(dstImage.cols(), dstImage.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(dstImage, bmp);
        } catch (CvException e) {
            System.out.println("CvException ###");
            System.out.println(e.getMessage());
        }

        //Setting bitmap as imageView image.
        imageView = findViewById(R.id.image_view);
        imageView.setImageBitmap(bmp);

        //Searching the pictures directory for file names that will then be displayed as groups.
        File directoryToSearch = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        assert directoryToSearch != null;
        File[] tempDirs = directoryToSearch.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        ArrayList<String> dirsInPictures = new ArrayList<>();

        assert tempDirs != null;
        for (File tempDir : tempDirs) {
            String[] tempArray = tempDir.toString().split("/");
            dirsInPictures.add(tempArray[tempArray.length - 1]);
        }
        dirsInPictures.add("New Group");

        //Adding the strings of directory names into the spinner.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dirsInPictures);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    } //End Method onCreate.

    /**
     * Runs every time a component is clicked that has an onClickListener to perform an action.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            case R.id.recrop_button:
                //Goes back to the crop screen.
                NavUtils.navigateUpFromSameTask(NamingActivity.this);
                break;
            case R.id.continue_button:
                //Gets the image name and if it is empty, prompts the user to enter a name.
                imageName = nameTextField.getText().toString().trim();
                if (imageName.equals("") || imageName.equals(" ")) {
                    Toast.makeText(this, "Please enter a name for the note.", Toast.LENGTH_SHORT).show();
                    break;
                }

                //Gets the group name from the spinner.
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                groupName = spinner.getSelectedItem().toString();

                //If new group is selected, bring up a dialog to make the new group.
                if (groupName.equals("New Group")) {
                    NewGroupDialog newGroupDialog = new NewGroupDialog();
                    newGroupDialog.show(getSupportFragmentManager(), "new group dialog");
                } else {
                    //If the group name already exists, save the image there.
                    Imgproc.cvtColor(dstImage, dstImage, Imgproc.COLOR_RGB2BGR);
                    Imgcodecs.imwrite(storageDir + "/" + groupName + "/" + imageName + ".jpg", dstImage);

                    endActivity();
                }
                break;
        }
    } //End Method onClick.

    /**
     * Runs once the user presses 'OKAY' on the dialog to apply the new group name and create a directory for it.
     *
     * @param newGroupName
     */
    @Override
    public void applyText(String newGroupName) {
        groupName = newGroupName;

        //Creating the new directory for the group.
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File dir = new File(storageDir + "/" + groupName);
        try {
            if (dir.mkdir()) {
                System.out.println("Directory Created");
            } else {
                System.out.println("Directory Creation Failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Saving image in the new group.
        Imgproc.cvtColor(dstImage, dstImage, Imgproc.COLOR_RGB2BGR);
        Imgcodecs.imwrite(storageDir + "/" + groupName + "/" + imageName + ".jpg", dstImage);

        endActivity();
    } //End Method applyText.

    private void endActivity() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

        if (getIntent().getBooleanExtra("is_file_internal", false)) {
            File file = new File(Objects.requireNonNull(getIntent().getStringExtra("filename")));
            if (file.delete()) {
                System.out.println("File deleted successfully.");
            } else {
                System.out.println("File deletion failed.");
            }
        }
    }
} //End Class NamingActivity.
