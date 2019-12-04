package com.hisbaan.sticky;

//Android imports.

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

//Java imports.
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private boolean isFABOpen = false;

    private String currentImagePath = null;

    //Member variables.
    private FloatingActionButton mainFab;
    private FloatingActionButton photoFab;
    private FloatingActionButton drawNoteFab;

    //Declaring animations
    private Animation rotateAnimation;
    private Animation rotateBackAnimation;

    private Animation scaleAnimation;
    private Animation scaleBackAnimation;

    private Animation setScaleAnimation;

    private static final int REQUEST_IMAGE_CAPTURE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFab = findViewById(R.id.main_fab);
        photoFab = findViewById(R.id.new_photo_fab);
        drawNoteFab = findViewById(R.id.new_note_fab);

        //Initializing animations.
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotateBackAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_back);

        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);
        scaleBackAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_back);

        setScaleAnimation = AnimationUtils.loadAnimation(this, R.anim.set_scale);

        //Setting the initial scale of the buttons to 0 so that they don't show up
        photoFab.startAnimation(setScaleAnimation);
        setFAB(photoFab, false);

        drawNoteFab.startAnimation(setScaleAnimation);
        setFAB(drawNoteFab, false);


        mainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });

        photoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (imageTakeIntent.resolveActivity(getPackageManager()) != null) {
                    File imageFile = getImageFile();

                    if (imageFile != null) {
                        Uri imageUri = FileProvider.getUriForFile(MainActivity.this, "com.hisbaan.android.fileprovider", imageFile);
                        imageTakeIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                        startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);
                    }

                }

            }
        });

        drawNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Feature to be added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private File getImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CANADA).format(new Date());

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = null;
        try {
            imageFile = File.createTempFile(timeStamp, ".jpg", storageDir);
            currentImagePath = imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Intent i = new Intent(getApplicationContext(), CameraActivity.class);
            i.putExtra("image_path", currentImagePath);
            startActivity(i);
        }

    }

    private void showFABMenu() {
        isFABOpen = true;

        mainFab.startAnimation(rotateAnimation);

        photoFab.startAnimation(scaleAnimation);
        setFAB(photoFab, true);

        drawNoteFab.startAnimation(scaleAnimation);
        setFAB(drawNoteFab, true);
    }

    private void closeFABMenu() {
        isFABOpen = false;

        mainFab.startAnimation(rotateBackAnimation);

        photoFab.startAnimation(scaleBackAnimation);
        setFAB(photoFab, false);

        drawNoteFab.startAnimation(scaleBackAnimation);
        setFAB(drawNoteFab, false);
    }

    private void setFAB(FloatingActionButton fab, Boolean state) {
        fab.setEnabled(state);
        fab.setClickable(state);
        fab.setFocusable(state);
    }
}