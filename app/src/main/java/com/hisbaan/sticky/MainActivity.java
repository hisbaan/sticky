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

/**
 * Creates the main activity for the program, launches other activities, and allows for user image capture.
 */
public class MainActivity extends AppCompatActivity {

    //Code for image capture request. This is used to ensure that an intent meant to trigger the camera.
    private static final int REQUEST_IMAGE_CAPTURE = 101;

    //Tracks the filepath of the current image that has been captured.
    private String currentImagePath = null;

    //Tracks whether the FAB drawer is open.
    private boolean isFABOpen = false;

    //Member variables.
    private FloatingActionButton mainFAB;
    private FloatingActionButton photoFAB;
    private FloatingActionButton drawNoteFAB;

    //Declaring animations.
    private Animation rotateAnimation;
    private Animation rotateBackAnimation;
    private Animation scaleAnimation;
    private Animation scaleBackAnimation;
    private Animation setScaleAnimation;

    /**
     * Runs when the activity is created to initialize variables and create onClickListeners.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing FloatingActionButtons.
        mainFAB = findViewById(R.id.main_fab);
        photoFAB = findViewById(R.id.new_photo_fab);
        drawNoteFAB = findViewById(R.id.new_note_fab);

        //Initializing animations from XML files located in src/res/anim/.
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotateBackAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_back);

        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);
        scaleBackAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_back);
        setScaleAnimation = AnimationUtils.loadAnimation(this, R.anim.set_scale);

        //Setting the initial scale of the buttons in the FAB to 0 so that they don't show up on start.
        photoFAB.startAnimation(setScaleAnimation);
        drawNoteFAB.startAnimation(setScaleAnimation);

        //Setting the state of the button to unresponsive so that they are not interacted with when invisible.
        setFAB(photoFAB, false);
        setFAB(drawNoteFAB, false);

        //Setting an onClickListener on the main FAB that opens and closes the FAB drawer.
        mainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFABOpen) {
                    closeFABMenu();
                } else {
                    showFABMenu();
                }
            }
        });

        //Setting an onClickListener on the photoFAB that triggers the camera.
        photoFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent that triggers the camera intent.
                Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (imageTakeIntent.resolveActivity(getPackageManager()) != null) {
                    //Gets name of file from the getImageFile() method.
                    File imageFile = getImageFile();

                    //If the image path is found, save the image to the filepath once the image has been taken.
                    if (imageFile != null) {
                        Uri imageUri = FileProvider.getUriForFile(MainActivity.this, "com.hisbaan.android.fileprovider", imageFile);
                        imageTakeIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                        startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);
                    }

                }

            }
        });

        //Setting an onClickListener to the drawNoteFAB which shows a toast saying that the feature has yet to be added.
        drawNoteFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Feature to be added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Gets a unique name for an image file based on the current date and time.
     *
     * @return the name of the image.
     */
    private File getImageFile() {
        //Gets a time stamp basted on the current year, month, day, hour, minute, and second. This makes it completely unique.
        String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CANADA).format(new Date());

        //Directory where pictures are stored.
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = null;
        try {
            //Creates a file with the unique name and adds the photo directory along with suffixing it with .jpg as an image file format.
            imageFile = File.createTempFile(timeStamp, ".jpg", storageDir);
            currentImagePath = imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageFile;
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

    /**
     * Saves the file if a picture was successfully taken and deletes it if the action was cancelled.
     *
     * @param requestCode Code of the request that identifies where it came from.
     * @param resultCode Code of the result that indicates whether or not the action was cancelled.
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //If the request came from the camera action and the action was not cancelled, run the CameraActivity which will display the saved image.
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Intent i = new Intent(getApplicationContext(), CameraActivity.class);
            i.putExtra("image_path", currentImagePath);
            startActivity(i);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED) { //If the request code came from the camera action and the action was cancelled, delete the file remnant.
            File file = new File(currentImagePath);
            file.delete();
        }

    }

    /**
     * Shows the FAB drawer when the mainFAB is clicked on.
     */
    private void showFABMenu() {
        isFABOpen = true;

        //Rotate the mainFAB 45 degrees.
        mainFAB.startAnimation(rotateAnimation);

        //Scale the photoFAB and drawNoteFAB from 0 to 1.2 to 1.
        photoFAB.startAnimation(scaleAnimation);
        drawNoteFAB.startAnimation(scaleAnimation);

        //Setting the photoFAB and the drawNoteFAB to be clickable so they can be clicked on.
        setFAB(photoFAB, true);
        setFAB(drawNoteFAB, true);
    }

    /**
     * Hides the FAB drawer when the mainFAB is clicked on.
     */
    private void closeFABMenu() {
        isFABOpen = false;

        //Rotate the mainFAB back to 0 degrees.
        mainFAB.startAnimation(rotateBackAnimation);

        //Scale the photoFAB and the drawNoteFAB back to 0 so they are invisible.
        photoFAB.startAnimation(scaleBackAnimation);
        drawNoteFAB.startAnimation(scaleBackAnimation);

        //Setting the photoFAB and drawNoteFAB to be un-clickable because they will be invisible.
        setFAB(photoFAB, false);
        setFAB(drawNoteFAB, false);
    }

    /**
     * Sets the state of the FAB passed to it to be clickable or un-clickable.
     *
     * @param fab FloatingActionButton that is to have its interactability modified.
     * @param state Decides whether the passed FloatingActionButton will be clickable or not.
     */
    private void setFAB(FloatingActionButton fab, Boolean state) {
        fab.setEnabled(state);
        fab.setClickable(state);
        fab.setFocusable(state);
    }
}