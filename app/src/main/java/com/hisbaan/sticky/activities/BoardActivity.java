package com.hisbaan.sticky.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.hisbaan.sticky.R;
import com.hisbaan.sticky.models.DrawnImageView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.Scanner;

/**
 * Displays the content of a board with a canvas.
 */
public class BoardActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    ArrayList<ImageView> imageViews = new ArrayList<>();
    ArrayList<DrawnImageView> drawnImageViews = new ArrayList<>();
    RelativeLayout relativeLayout;

    private float dX;
    private float dY;

    private long startClickTime;
    private ImageView contextInteractionView;

    float screenWidth;
    float screenHeight;

    private static final int REQUEST_NEW_NOTE = 1;

    /**
     * Runs on activity create initializing the canvas and the toolbar.
     *
     * @param savedInstanceState If used, stores whatever data was pushed to it on activity pause or destroy via methods such as the onPause() method.
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        //Initializing toolbar and setting up the back button.
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(BoardActivity.this);
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

        //Getting screen height and width.
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        //Initializing button to add new notes.
        Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When the add button is clicked, get the note name form the picker activities.
                Intent intent = new Intent(BoardActivity.this, FolderPickerActivity.class);
                startActivityForResult(intent, REQUEST_NEW_NOTE);
            }
        });

        //Initializing the 'canvas' layout that notes are placed on.
        relativeLayout = findViewById(R.id.board_layout);

        //Getting the board name from the intent that was passed to it from the previous activity.
        String boardName = getIntent().getStringExtra("board_name") + ".txt";
        setTitle(boardName.substring(0, boardName.length() - 4));

        //Reading the file based on the name given.
        String file = null;
        FileInputStream fis = null;
        try {
            fis = openFileInput(boardName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
            file = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //Loading any previous data in the note
        if (file != null) {
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String[] info = sc.nextLine().split(",");
                addNote(info[0], info[1], Integer.parseInt(info[2]), Integer.parseInt(info[3]));
            }
        }
    } //End method onCreate.

    /**
     * Saves the board when the board is exited, the app is closed, etc. Basically an auto-save function.
     */
    @Override
    protected void onPause() {
        super.onPause();

        //Put info from the board into the text variable.
        String text = "";
        for (int i = 0; i < imageViews.size(); i++) {
            if (i != 0) {
                text += "\n";
            }
            text += drawnImageViews.get(i).getGroupName() + "," + drawnImageViews.get(i).getNoteName() + "," + (int) (imageViews.get(i).getX()) + "," + (int) (imageViews.get(i).getY());
        }

        //Save this text variable to a local file.
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(getIntent().getStringExtra("board_name") + ".txt", MODE_PRIVATE);
            fos.write(text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    } //End method onPause.


    /**
     * Method that adds a note to the board at the coordinates given.
     *
     * @param boardName The name of the board where the image is located.
     * @param noteName  The name of the file that the note is.
     * @param x         The desired x value of the note.
     * @param y         The desired y value of the note.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void addNote(String boardName, String noteName, int x, int y) {
        //TODO make an error if the x and y would cause the note to go out of bounds.
        ImageView imageView = new ImageView(this);
        Bitmap bmp = BitmapFactory.decodeFile(Objects.requireNonNull(getExternalFilesDir(Environment.DIRECTORY_PICTURES)).toString() + "/" + boardName + "/" + noteName);
        imageView.setImageBitmap(bmp);
        imageView.setOnClickListener(this);
        imageView.setOnTouchListener(this);

        registerForContextMenu(imageView);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(250, 250);
        params.leftMargin = x;
        params.topMargin = y;

        relativeLayout.addView(imageView, params);

        imageViews.add(imageView);
        drawnImageViews.add(new DrawnImageView(boardName, noteName));
    } //End method addNote.

    /**
     * onClick method that runs code when a view is clicked.
     *
     * @param v The view that is clicked.
     */
    @Override
    public void onClick(View v) {
        openContextMenu(v);
    } //End method onClick.


    /**
     * Touch listener to allow for dragging the points around and when the user clicks on a view quickly, open a context menu.
     *
     * @param v     View of the item being interacted with.
     * @param event What is happening to the item.
     * @return Whether or not to perform an action on the item.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (movable(v)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                    if (clickDuration < 100) {
                        contextInteractionView = (ImageView) v;
                        openContextMenu(v);
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    startClickTime = Calendar.getInstance().getTimeInMillis();
                    dX = v.getX() - event.getRawX();
                    dY = v.getY() - event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    v.animate().x(event.getRawX() + dX).y(event.getRawY() + dY).setDuration(0).start();
                    v.bringToFront();

                    System.out.println(imageViews.toString());

                    ImageView tempView = (ImageView) v;
                    int shiftIndex = imageViews.indexOf(tempView);
                    DrawnImageView tempDrawnImageView = drawnImageViews.get(shiftIndex);

                    imageViews.remove(shiftIndex);
                    drawnImageViews.remove(shiftIndex);

                    imageViews.add(tempView);
                    drawnImageViews.add(tempDrawnImageView);
                    break;
                default:
                    return false;
            }
            return true;
        } else {
            if (v.getX() <= 0) {
                v.setX(1);
            } else if (v.getY() <= 0) {
                v.setY(1);
            } else if ((v.getX() + v.getWidth()) >= screenWidth) {
                v.setX(screenWidth - v.getWidth() - 1);
            } else if ((v.getY() + v.getHeight()) >= screenHeight) {
                v.setY(screenHeight - v.getHeight() - 1);
            }
            return true;
        }
    } //End method onTouch.

    /**
     * Boolean that decides if an object is movable (in bounds).
     *
     * @param v The view being checked.
     * @return Whether or not the view should be moved.
     */
    private boolean movable(View v) {
        float x = v.getX();
        float y = v.getY();
        int width = v.getWidth();
        int height = v.getHeight();

        return ((x > 0) && (y > 0) && ((x + width) < screenWidth) && ((y + height) < screenHeight));
    } //End method movable.

    /**
     * Runs when an activity started for a result returns.
     *
     * @param requestCode The code of the request made.
     * @param resultCode  The result of the code. Either okay or cancelled.
     * @param data        The data returned.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NEW_NOTE && resultCode == RESULT_OK && data != null) {
            String[] result = Objects.requireNonNull(data.getStringExtra("result")).split(",");

            System.out.println(result[0] + " ,,,, " + result[1]);

            if (!result[0].equals("") || !result[1].equals("")) {
                addNote(result[0], result[1] + ".jpg", (int) (screenWidth / 2), (int) (screenHeight / 2));
            } else {
                Toast.makeText(this, "null avoided", Toast.LENGTH_SHORT).show();
            }
        }
    } //End method onActivityResult.

    /**
     * Inflates a context menu.
     *
     * @param menu     The menu that is to be inflated.
     * @param v        The view that the menu is opened on.
     * @param menuInfo Information saved in the menu.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.delete_note_context_menu, menu);
    } //End method onCreateContextMenu.

    /**
     * Performs actions on a given context menu.
     *
     * @param item Information about the menu.
     * @return Whether an action should be performed or not.
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                relativeLayout.removeView(contextInteractionView);
                int index = imageViews.indexOf(contextInteractionView);
                imageViews.remove(index);
                drawnImageViews.remove(index);
                return true;
            case R.id.send_to_back:
                //TODO send the note to the back of the stack so others appear on top of it.
                Toast.makeText(this, "Feature to be added", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    } //End method onContextItemSelected.
} //End class BoardActivity.