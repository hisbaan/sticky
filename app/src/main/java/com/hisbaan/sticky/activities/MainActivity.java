package com.hisbaan.sticky.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.hisbaan.sticky.R;
import com.hisbaan.sticky.fragments.BoardFragment;
import com.hisbaan.sticky.fragments.NoteOrganizerFragment;
import com.hisbaan.sticky.utils.NewBoardDialog;

import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Creates the main activity for the program, launches other activities, and allows for user image capture.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, NewBoardDialog.NewBoardDialogListener {

    //Final variables that are used to retrieve information.
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String THEME = "theme";
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_EXTERNAL_IMAGE_SELECTION = 100;
    private static final int PERMISSION_REQUEST = 0;

    //Initializing openCV.
    static {
        if (!OpenCVLoader.initDebug()) {
            Log.d("ERROR", "Unable to load OpenCV");
        } else {
            Log.d("SUCCESS", "Successfully loaded OpenCV");
        }
    }

    //Tracks the filepath of the current image that has been captured.
    private String currentImagePath = null;

    //Tracks whether the FAB drawer is open.
    private boolean isFABOpen = false;

    //Declaring Buttons.
    private FloatingActionButton mainFAB;
    private FloatingActionButton photoFAB;
    private FloatingActionButton drawNoteFAB;
    private FloatingActionButton uploadPhotoFAB;
    private DrawerLayout drawerLayout;

    //Declaring animations.
    private Animation rotateAnimation;
    private Animation rotateBackAnimation;
    private Animation scaleAnimation;
    private Animation scaleBackAnimation;
    private Animation setScaleAnimation;

    /**
     * Runs when the activity is created to initialize variables and create onClickListeners.
     *
     * @param savedInstanceState If used, stores whatever data was pushed to it on activity pause or destroy via methods such as the onPause() method.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing toolbar with a menu button that opens the drawer.
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFABOpen) {
                    closeFABMenu();
                }
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        //Finding button in XML and initializing matching java component.
        drawerLayout = findViewById(R.id.drawer_layout);

        //Initializing listener for the buttons on the nav drawer.
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Restores shared preference of the app theme when the activity is restored.
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int mode = sharedPreferences.getInt(THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(mode);

        //Sets status bar colour based on current theme
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

        //Initializing FloatingActionButtons.
        mainFAB = findViewById(R.id.main_fab);
        photoFAB = findViewById(R.id.new_photo_fab);
        drawNoteFAB = findViewById(R.id.new_note_fab);
        uploadPhotoFAB = findViewById(R.id.upload_fab);

        //Initializing animations from XML files located in src/res/anim/.
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotateBackAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_back);
        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);
        scaleBackAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_back);
        setScaleAnimation = AnimationUtils.loadAnimation(this, R.anim.set_scale);

        //Setting the initial scale of the buttons in the FAB to 0 so that they don't show up on start.
        photoFAB.startAnimation(setScaleAnimation);
        drawNoteFAB.startAnimation(setScaleAnimation);
        uploadPhotoFAB.startAnimation(setScaleAnimation);

        //Setting the state of the button to unresponsive so that they are not interacted with when invisible.
        setFAB(photoFAB, false);
        setFAB(drawNoteFAB, false);
        setFAB(uploadPhotoFAB, false);

        //Opens the Board fragment so that the app does not start with a blank activity, only on the first run.
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new BoardFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_board);
        }

        //Setting OnClickListeners to the FABs.
        mainFAB.setOnClickListener(this);
        photoFAB.setOnClickListener(this);
        drawNoteFAB.setOnClickListener(this);
        uploadPhotoFAB.setOnClickListener(this);
    } //End method onCreate.

    /**
     * Handles the presses of buttons that have had the OnClickListener added on to it.
     *
     * @param v The View of the item interacted with.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_fab: //Opens and closes the FAB drawer.
                if (isFABOpen) {
                    closeFABMenu();
                } else {
                    showFABMenu();
                }
                break;
            case R.id.new_photo_fab: //Triggers the camera and saves the photo.
                closeFABMenu();
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
                break;
            case R.id.new_note_fab:
                closeFABMenu();
                Toast.makeText(this, "Feature to be added", Toast.LENGTH_SHORT).show();
                break;
            case R.id.upload_fab:
                //TODO trigger a file browser and return the path. Then pass that image onto the crop activity.
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    closeFABMenu();
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_EXTERNAL_IMAGE_SELECTION);
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Toast.makeText(this, "External storage permission is needed to import notes.", Toast.LENGTH_SHORT).show();
                    }

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
                }


                break;
        }
    } //End method OnClickListener

    /**
     * Handles the clicks of buttons in the navigation drawer.
     *
     * @param item The item in the navigation drawer that was clicked on.
     * @return Whether or not a button was clicked on.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_board:
                //Opens the board fragment.
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new BoardFragment())
                        .commit();
                break;
            case R.id.nav_organizer:
                //Opens the note organizer fragment.
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new NoteOrganizerFragment())
                        .commit();
                break;
            case R.id.nav_tips:
                //Opens the tips menu. This will show some tips and tricks.
                startActivity(new Intent(getApplicationContext(), TipsActivity.class));
                break;
            case R.id.nav_feedback:
                //Links to a google form where the user can provide feedback. This could be a bug, personal issue or suggested feature.
                Uri uri = Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLScqm8FnLu_HyxQM1pKXTxy-C05B9tu9s3l3_F7HUeuGrEGFDA/viewform?usp=sf_link");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
                break;
            case R.id.nav_settings: //Opens the settings activity.
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    } //End method onNavigationItemsSelected.

    /**
     * Handles the pressing of the back button.
     */
    @Override
    public void onBackPressed() {
        //TODO if the user enables the setting, make the back button open the drawer instead of closing the app.
        //TODO aks the user for confirmation before closing the app --> Closing the app with the back button will kill the process (make this a setting).
        //If the navigation drawer is open, close it instead of exiting out of the app.
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    } //End method onBackPressed.

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
    } //End method getImageFile.

    /**
     * Initialize the contents of the Activity's standard options menu.
     *
     * @param menu The menu in which items are placed.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    } //End method onCreateOptionsMenu.

    /**
     * This hook is called whenever an item in your options menu is selected.
     *
     * @param item Item that is selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
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
    } //End method onOptionsItemSelected.

    /**
     * Saves the file if a picture was successfully taken and deletes it if the action was cancelled.
     *
     * @param requestCode Code of the request that identifies where it came from.
     * @param resultCode  Code of the result that indicates whether or not the action was cancelled.
     * @param data        Not used
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //If the request came from the camera action and the action was not cancelled, run the CropActivity which will display the saved image.
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Intent i = new Intent(getApplicationContext(), CropActivity.class);
            i.putExtra("image_path", currentImagePath);
            startActivity(i);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED) { //If the request code came from the camera action and the action was cancelled, delete the file remnant.
            File file = new File(currentImagePath);
            if (file.delete()) {
                System.out.println("File deleted successfully.");
            } else {
                System.out.println("File deletion failed.");
            }
        } else if (requestCode == REQUEST_EXTERNAL_IMAGE_SELECTION && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "There was an error importing the image. Please try again", Toast.LENGTH_SHORT).show();
            } else {
                Uri imageUri = data.getData();
                String filePath = getPathFromURI(imageUri);

                Intent intent = new Intent(getApplicationContext(), CropActivity.class);
                intent.putExtra("image_path", filePath);
                intent.putExtra("is_file_internal", false);
                startActivity(intent);
            }
        }
    } //End method onActivityResult.

    /**
     * Gets the filepath of the image chosen by the user in their galley app of choice.
     *
     * @param uri The uri of the image of which the filepath is being retrieved.
     * @return The absolute filepath that leads to the image.
     */
    private String getPathFromURI(Uri uri) {
        if (uri == null) {
            return null;
        } else {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String imagePath = cursor.getString(colIndex);
                cursor.close();
                return imagePath;
            }
        }
        return uri.getPath();
    } //End method getPathFromUri.

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
        uploadPhotoFAB.startAnimation(scaleAnimation);

        //Setting the photoFAB and the drawNoteFAB to be clickable so they can be clicked on.
        setFAB(photoFAB, true);
        setFAB(drawNoteFAB, true);
        setFAB(uploadPhotoFAB, true);
    } //End method showFABMenu.

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
        uploadPhotoFAB.startAnimation(scaleBackAnimation);

        //Setting the photoFAB and drawNoteFAB to be un-clickable because they will be invisible.
        setFAB(photoFAB, false);
        setFAB(drawNoteFAB, false);
        setFAB(uploadPhotoFAB, false);
    } //End method closeFABMenu.

    /**
     * Sets the state of the FAB passed to it to be clickable or un-clickable.
     *
     * @param fab   FloatingActionButton that is to have its interactability modified.
     * @param state Decides whether the passed FloatingActionButton will be clickable or not.
     */
    private void setFAB(FloatingActionButton fab, Boolean state) {
        fab.setEnabled(state);
        fab.setClickable(state);
        fab.setFocusable(state);
    } //End method setFAB.

    /**
     * Creates a new board file that is displayed in the recycler view.
     *
     * @param newBoardName The name of a board the user creates.
     */
    @Override
    public void applyText(String newBoardName) {
        File newBoardFile = new File(getFilesDir().getPath() + "/" + newBoardName + ".txt");
        try {
            if (newBoardFile.createNewFile()) {
                System.out.println("New board created successfully");
            } else {
                System.out.println("Error creating new board");
                Toast.makeText(this, "A board with that name already exists", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
        intent.putExtra("board_name", newBoardName);
        startActivity(intent);
    } //End method applyText.
} //End class MainActivity.