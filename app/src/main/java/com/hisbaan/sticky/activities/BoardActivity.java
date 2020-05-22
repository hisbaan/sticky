package com.hisbaan.sticky.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;

import com.hisbaan.sticky.R;
import com.hisbaan.sticky.models.Canvas;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Displays the content of a board with a canvas.
 */
public class BoardActivity extends AppCompatActivity {

    /**
     * Runs on activity create initializing the canvas and the toolbar.
     *
     * @param savedInstanceState If used, stores whatever data was pushed to it on activity pause or destroy via methods such as the onPause() method.
     */
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

        //Getting the board name from the intent that was passed to it from the previous activity.
        String boardName = getIntent().getStringExtra("board_name") + ".txt";
        setTitle(boardName.substring(0, boardName.length() - 4));

        //Initializing the canvas and sending information about the name of the related file so information can be loaded.
        Canvas canvas = findViewById(R.id.canvas);
        canvas.setBoardName(boardName);
        canvas.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    } //End Method onCreate.

    public void save() {
        String text = "BoardTest,Test.jpg,100,100";
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

} //End Class BoardActivity.
