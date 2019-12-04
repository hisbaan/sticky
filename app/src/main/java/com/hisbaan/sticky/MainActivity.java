package com.hisbaan.sticky;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import java.util.Calendar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private boolean isFABOpen = false;

    private FloatingActionButton mainFab;
    private FloatingActionButton photoFab;
    private FloatingActionButton drawNoteFab;
    private FloatingActionButton newBoardFab;

    private String currentImagePath = null;

    private static final int REQUEST_IMAGE_CAPTURE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFab = findViewById(R.id.main_fab);
        photoFab = findViewById(R.id.new_photo_fab);
        drawNoteFab = findViewById(R.id.new_note_fab);
        newBoardFab = findViewById(R.id.new_board_fab);

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
//                Intent i = new Intent(getApplicationContext(), CameraActivity.class);
//                startActivity(i);

                Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (imageTakeIntent.resolveActivity(getPackageManager()) != null) {

                    File imageFile = getImageFile();

                    if(imageFile != null) {
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

        newBoardFab.setOnClickListener(new View.OnClickListener() {
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
        String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());

//        Date rightNowDate = Calendar.getInstance().getTime();
//        char[] rightNowTemp = rightNowDate.toString().toCharArray();
//        String rightNow = "";
//
//        for(int i = 0; i < rightNowTemp.length; i++) {
//            if (rightNowTemp[i] == ' ') {
//                rightNowTemp[i] = '-';
//            }
//        }
//
//        for (char c : rightNowTemp) {
//            rightNow += c;
//        }

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
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//
//            ImageView imageView = findViewById(R.id.image_view);
//            imageView.setImageBitmap(imageBitmap);

            Intent i = new Intent(getApplicationContext(), CameraActivity.class);
            i.putExtra("image_path", currentImagePath);
            startActivity(i);
        }

    }

    private void showFABMenu() {
        isFABOpen = true;
        mainFab.animate().rotation(45);
        photoFab.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        drawNoteFab.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        newBoardFab.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu() {
        isFABOpen = false;
        mainFab.animate().rotation(0);
        photoFab.animate().translationY(0);
        drawNoteFab.animate().translationY(0);
        newBoardFab.animate().translationY(0);
    }
}