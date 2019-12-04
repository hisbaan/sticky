package com.hisbaan.sticky;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class CameraActivity extends AppCompatActivity {

//    CameraManager manager = new CameraManager();

    private ImageView imageView;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        back = findViewById(R.id.back_button);
        imageView = findViewById(R.id.image_view);

        Bitmap bitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("image_path"));
        imageView.setImageBitmap(bitmap);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
