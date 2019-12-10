package com.hisbaan.sticky;

//Importing android packages
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Displays the image that was captured immediately before this activity is triggered.
 */
public class CameraActivity extends AppCompatActivity {

    //Declaring variable
    private ImageView imageView;
    private Button back;

    /**
     * Initializes variables, creates a bitmap of the image that was captured and pushes the bitmap to an imageView in the program when the program.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        //Initializing member variables.
        back = findViewById(R.id.back_button);
        imageView = findViewById(R.id.image_view);

        //Declaring and initialising bitmap that is used to display the captured image in the activity.
        Bitmap bitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("image_path"));
        //Setting imageView to display the bitmap.
        imageView.setImageBitmap(bitmap);

        //Setting an onClickListener on the back button that closes the current activity and goes back to the activity that triggered the current activity.
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    } //End Method onCreate.
} //End Class CameraActivity.
