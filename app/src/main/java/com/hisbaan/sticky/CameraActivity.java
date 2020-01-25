package com.hisbaan.sticky;

//Importing android packages
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

/**
 * Displays the image that was captured immediately before this activity is triggered.
 */
public class CameraActivity extends AppCompatActivity {

    //Declaring variable
    private ImageView imageView;

    /**
     * Initializes variables, creates a bitmap of the image that was captured and pushes the bitmap to an imageView in the program when the program.
     *
     * @param savedInstanceState Saved information that has been placed in it.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Toolbar toolbar = findViewById(R.id.toolbar_camera);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle(R.string.title_activity_camera);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(CameraActivity.this);
            }
        });

        //Initializing member variables.
        imageView = findViewById(R.id.image_view);

        //Declaring and initialising bitmap that is used to display the captured image in the activity.
        Bitmap bitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("image_path"));
        //Setting imageView to display the bitmap.
        imageView.setImageBitmap(bitmap);
    } //End Method onCreate.
} //End Class CameraActivity.
