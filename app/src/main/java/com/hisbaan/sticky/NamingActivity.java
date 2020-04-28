package com.hisbaan.sticky;

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

import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public class NamingActivity extends AppCompatActivity implements View.OnClickListener, NewGroupDialog.NewGroupDialogListener {
    ImageView imageView;
    EditText nameTextField;
    Button cancelButton;
    Button continueButton;
    Spinner spinner;
    String groupName;
    String imageName;

    Mat dstImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naming);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(this);
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

        long addr = getIntent().getLongExtra("dst_image_addr", 0);
        Mat tempImg = new Mat(addr);
        System.out.println(tempImg);
        dstImage = tempImg.clone();

        imageView = findViewById(R.id.image_view);
        nameTextField = findViewById(R.id.name_edit_text);
        spinner = findViewById(R.id.group_select_spinner);

        cancelButton = findViewById(R.id.cancel_button);
        continueButton = findViewById(R.id.continue_button);
        cancelButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);

        //TODO issue here about the code on the getNativeObjAddr. Can't put anything ontop of it or it breaks.
        //Displaying the transformed image.
        Bitmap bmp = null;

        try {
            bmp = Bitmap.createBitmap(dstImage.cols(), dstImage.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(dstImage, bmp);
        } catch (CvException e) {
            System.out.println("CvException ###");
            System.out.println(e.getMessage());
        }

        imageView = findViewById(R.id.image_view);
        imageView.setImageBitmap(bmp);

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dirsInPictures);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            case R.id.recrop_button:
            case R.id.toolbar:
                NavUtils.navigateUpFromSameTask(NamingActivity.this);
                break;
            case R.id.continue_button:
                imageName = nameTextField.getText().toString().trim();
                if (imageName.equals("") || imageName.equals(" ")) {
                    Toast.makeText(this, "Please enter a name for the note.", Toast.LENGTH_SHORT).show();
                    break;
                }

                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                groupName = spinner.getSelectedItem().toString();

                if (groupName.equals("New Group")) {
                    openDialog();
                } else {

                    Imgproc.cvtColor(dstImage, dstImage, Imgproc.COLOR_RGB2BGR);
                    Imgcodecs.imwrite(storageDir + "/" + groupName + "/" + imageName + ".jpg", dstImage);
                }
                break;
        }
    }

    private void openDialog() {
        NewGroupDialog newGroupDialog = new NewGroupDialog();
        newGroupDialog.show(getSupportFragmentManager(), "new group dialog");
    }

    @Override
    public void applyText(String newGroupName) {
        groupName = newGroupName;

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

        Imgproc.cvtColor(dstImage, dstImage, Imgproc.COLOR_RGB2BGR);
        Imgcodecs.imwrite(storageDir + "/" + groupName + "/" + imageName + ".jpg", dstImage);
    }
}
