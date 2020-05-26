package com.hisbaan.sticky.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(this);

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
    } //End Method onCreate.


    @Override
    protected void onPause() {
        super.onPause();

        String text = "";

        for (int i = 0; i < imageViews.size(); i++) {
            if (i != 0) {
                text += "\n";
            }
            text += drawnImageViews.get(i).getGroupName() + "," + drawnImageViews.get(i).getNoteName() + "," + (int) (imageViews.get(i).getX()) + "," + (int) (imageViews.get(i).getY());
        }

        //TODO get list of sticky notes and their positions here (maybe from canvas?) and make it the variable text. One note per line.
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
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addNote(String boardName, String noteName, int x, int y) {
        ImageView imageView = new ImageView(this);
        Bitmap bmp = BitmapFactory.decodeFile(Objects.requireNonNull(getExternalFilesDir(Environment.DIRECTORY_PICTURES)).toString() + "/" + boardName + "/" + noteName);
        imageView.setImageBitmap(bmp);
        imageView.setOnTouchListener(this);

        //TODO replace 100, 100 with the size of the imageview once editing size is allowed.
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(250, 250);
        params.leftMargin = x;
        params.topMargin = y;

        relativeLayout.addView(imageView, params);

        imageViews.add(imageView);
        drawnImageViews.add(new DrawnImageView(boardName, noteName));
    }

    /**
     * Touch listener to allow for dragging the points around.
     *
     * @param v     View of the item being interacted with.
     * @param event What is happening to the item.
     * @return Whether or not to perform an action on the item.
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (movable(v)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dX = v.getX() - event.getRawX();
                    dY = v.getY() - event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    v.animate().x(event.getRawX() + dX).y(event.getRawY() + dY).setDuration(0).start();
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
    } //End Method onTouch.

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button:
                //TODO open drawer to get info here. Recycler view with images loaded in it (how to do multiple images???) and then click on individual items
                Intent intent = new Intent(this, FolderPickerActivity.class);
                startActivityForResult(intent, REQUEST_NEW_NOTE);
//                startActivity(intent);
                break;
        }
    } //End Method onClick.

    private boolean movable(View v) {

        float x = v.getX();
        float y = v.getY();
        int width = v.getWidth();
        int height = v.getHeight();

        return ((x > 0) && (y > 0) && ((x + width) < screenWidth) && ((y + height) < screenHeight));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NEW_NOTE && resultCode == RESULT_OK) {
            assert data != null;
            String[] result = Objects.requireNonNull(data.getStringExtra("result")).split(",");

            addNote(result[0], result[1] + ".jpg", 100, 100);
        }
    }
} //End Class BoardActivity.