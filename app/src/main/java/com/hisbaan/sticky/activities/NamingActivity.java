package com.hisbaan.sticky.activities;

import android.annotation.SuppressLint;
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

import com.hisbaan.sticky.R;
import com.hisbaan.sticky.utils.NewGroupDialog;

import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
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
    ImageView imageView;
    EditText nameTextField;
    Button cancelButton;
    Button continueButton;
    Button recropButton;
    Spinner spinner;

    Button rotateLeftButton;
    Button rotateRightButton;

    String groupName;
    String imageName;
    Mat dstMat;

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
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CropActivity.class);
            intent.putExtra("image_path", getIntent().getStringExtra("file_path"));
            intent.putExtra("is_file_internal", getIntent().getBooleanExtra("is_file_internal", true));
            startActivity(intent);
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

        dstMat = CropActivity.transferImage.clone();

        imageView = findViewById(R.id.image_view);
        nameTextField = findViewById(R.id.name_edit_text);
        spinner = findViewById(R.id.group_select_spinner);

        cancelButton = findViewById(R.id.cancel_button);
        continueButton = findViewById(R.id.continue_button);
        recropButton = findViewById(R.id.recrop_button);
        rotateLeftButton = findViewById(R.id.rotate_left_button);
        rotateRightButton = findViewById(R.id.rotate_right_button);

        cancelButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
        recropButton.setOnClickListener(this);
        rotateLeftButton.setOnClickListener(this);
        rotateRightButton.setOnClickListener(this);

        //Converting matrix to bitmap.
        setImageViewMatrix(dstMat);

        //Searching the pictures directory for file names that will then be displayed as groups.
        File directoryToSearch = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        assert directoryToSearch != null;
        File[] tempDirs = directoryToSearch.listFiles(File::isDirectory);

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
    } //End method onCreate.

    /**
     * Runs every time a component is clicked that has an onClickListener to perform an action.
     *
     * @param v The view that was clicked.
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
            case R.id.recrop_button:
                //Goes back to the crop screen.
                Intent intent = new Intent(this, CropActivity.class);
                intent.putExtra("image_path", getIntent().getStringExtra("file_path"));
                intent.putExtra("is_file_internal", getIntent().getBooleanExtra("is_file_internal", true));
                startActivity(intent);
                break;
            case R.id.continue_button:
                //Gets the image name and if it is empty, prompts the user to enter a name.
                imageName = nameTextField.getText().toString().trim();

                //Filtering to make sure the user's name does not break the app.
                if (imageName.trim().equals("")) {
                    Toast.makeText(this, "Please enter a name for the note.", Toast.LENGTH_SHORT).show();
                    break;
                } else if (imageName.contains(",")) {
                    Toast.makeText(this, "Please enter a name that does not include a comma (,).", Toast.LENGTH_SHORT).show();
                    break;
                } else if (imageName.contains("/")) {
                    Toast.makeText(this, "Please enter a name that does not include a forward slash (/).", Toast.LENGTH_SHORT).show();
                    break;
                } else if (imageName.contains("\\")) {
                    Toast.makeText(this, "Please enter a name that does not include a back slash (\\).", Toast.LENGTH_SHORT).show();
                    break;
                }

                //Gets the group name from the spinner.
                groupName = spinner.getSelectedItem().toString();

                //If new group is selected, bring up a dialog to make the new group.
                if (groupName.equals("New Group")) {
                    NewGroupDialog newGroupDialog = new NewGroupDialog();
                    newGroupDialog.show(getSupportFragmentManager(), "new group dialog");
                } else {
                    //If the group name already exists, save the image there.
                    File toSave = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + groupName + "/" + imageName + ".jpg");

                    if (toSave.exists()) {
                        Toast.makeText(this, "A note with this name already exists.", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    Imgproc.cvtColor(dstMat, dstMat, Imgproc.COLOR_RGB2BGR);
                    Imgcodecs.imwrite(toSave.toString(), dstMat);

                    endActivity();
                }
                break;
            case R.id.rotate_left_button:
                dstMat = rotateMatrix(dstMat, 90).clone();
                setImageViewMatrix(dstMat);
                break;
            case R.id.rotate_right_button:
                dstMat = rotateMatrix(dstMat, -90).clone();
                setImageViewMatrix(dstMat);
                break;
        }
    } //End method onClick.

    /**
     * Method that sets the image view to display a matrix (thus updating it).
     *
     * @param dst The matrix to be displayed in the image view.
     */
    private void setImageViewMatrix(Mat dst) {
        //Creating the bitmap from the matrix.
        Bitmap bmp = null;
        try {
            bmp = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(dst, bmp);
        } catch (CvException e) {
            System.out.println("CvException ###");
            System.out.println(e.getMessage());
        }

        //Setting bitmap as imageView image.
        imageView = findViewById(R.id.image_view);
        imageView.setImageBitmap(bmp);
    } //End method setImageViewMatrix.

    /**
     * Method that rotates a matrix given to it by the angle given to it.
     *
     * @param src   Source matrix.
     * @param angle Angle to be rotated by.
     * @return The rotated matrix.
     */
    private Mat rotateMatrix(Mat src, double angle) {
        Point center = new Point(src.width() / 2.0, src.height() / 2.0);
        Mat rotMat = Imgproc.getRotationMatrix2D(center, angle, 1.0);
        Size size = new Size(src.width(), src.height());
        Imgproc.warpAffine(src, src, rotMat, size, Imgproc.INTER_LINEAR + Imgproc.CV_WARP_FILL_OUTLIERS);
        return src;
    } //End method rotateMatrix.

    /**
     * Runs once the user presses 'OKAY' on the dialog to apply the new group name and create a directory for it.
     *
     * @param newGroupName The name of the new group being created.
     */
    @Override
    public void applyText(String newGroupName) {
        groupName = newGroupName.trim();

        //Filtering to make sure the user's name does not break the app.
        if (groupName.trim().equals("")) {
            Toast.makeText(this, "Please enter a name for the note.", Toast.LENGTH_SHORT).show();
        } else if (groupName.contains(",")) {
            Toast.makeText(this, "Please enter a name that does not include a comma (,).", Toast.LENGTH_SHORT).show();
        } else if (groupName.contains("/")) {
            Toast.makeText(this, "Please enter a name that does not include a forward slash (/).", Toast.LENGTH_SHORT).show();
        } else if (groupName.contains("\\")) {
            Toast.makeText(this, "Please enter a name that does not include a back slash (\\).", Toast.LENGTH_SHORT).show();
        } else {
            //Creating the new directory for the group.
            File dir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + groupName);
            try {
                if (dir.mkdir()) {
                    System.out.println("Directory Created");

                    //Saving image in the new group.
                    Imgproc.cvtColor(dstMat, dstMat, Imgproc.COLOR_RGB2BGR);
                    Imgcodecs.imwrite(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + groupName + "/" + imageName + ".jpg", dstMat);

                    endActivity();
                } else {
                    System.out.println("Directory Creation Failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    } //End method applyText.

    /**
     * Method that ends the activity. This is required due to the asynchronous nature of android.
     */
    private void endActivity() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

        if (getIntent().getBooleanExtra("is_file_internal", false)) {
            File file = new File(Objects.requireNonNull(getIntent().getStringExtra("file_path")));
            if (file.delete()) {
                System.out.println("File deleted successfully.");
            } else {
                System.out.println("File deletion failed.");
            }
        }
    } //End method endActivity.
} //End class NamingActivity.
