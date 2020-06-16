package com.hisbaan.sticky.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import androidx.exifinterface.media.ExifInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hisbaan.sticky.R;
import com.hisbaan.sticky.models.Canvas;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.Objects;

/**
 * Displays the image that was captured immediately before this activity is triggered.
 */
public class CropActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    //Declaring variables.
    private float dX;
    private float dY;
    private Canvas canvas;
    private FloatingActionButton point1;
    private FloatingActionButton point2;
    private FloatingActionButton point3;
    private FloatingActionButton point4;
    private ImageView imageView;

    public static float savedX1;
    public static float savedY1;
    public static float savedX2;
    public static float savedY2;
    public static float savedX3;
    public static float savedY3;
    public static float savedX4;
    public static float savedY4;

    public static float offCenterAdjustment;

    static Mat transferImage;

    /**
     * Initializes variables, creates a bitmap of the image that was captured and pushes the bitmap to an imageView in the program when the program.
     *
     * @param savedInstanceState Saved information that has been placed in it.
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        //Setting up toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(CropActivity.this);
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

        //Initializes points that are used for selecting the corners of the sticky note.
        point1 = findViewById(R.id.point_1);
        point2 = findViewById(R.id.point_2);
        point3 = findViewById(R.id.point_3);
        point4 = findViewById(R.id.point_4);
        point1.setOnTouchListener(this);
        point2.setOnTouchListener(this);
        point3.setOnTouchListener(this);
        point4.setOnTouchListener(this);

        Button selectButton = findViewById(R.id.select_button);
        selectButton.setOnClickListener(this);

        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);

        //Displaying image for manual cropping
        imageView = findViewById(R.id.image_view);

        //On Samsung devices, the imageView will display the image sideways so this is a fix for that.
        int angle = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(Objects.requireNonNull(getIntent().getStringExtra("image_path")));
            angle = Integer.parseInt(Objects.requireNonNull(exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(angle + " ###############");
        if (angle == 6) {
            angle = 90;
        }

        //Rotating and setting the bitmap.
        Bitmap bitmap = rotateBitmap(BitmapFactory.decodeFile(getIntent().getStringExtra("image_path")), angle);
        imageView.setImageBitmap(bitmap);

        Resources r = getResources();
        offCenterAdjustment = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());

        //Setting up the canvas initially.
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float screenWidth = displayMetrics.widthPixels;
        float screenHeight = displayMetrics.heightPixels;

        canvas = findViewById(R.id.canvas);
        canvas.initialSetup(screenWidth, screenHeight);
        canvas.invalidate();
    } //End method onCreate.

    /**
     * Rotates the bitmap based on the current angle.
     *
     * @param source The source bitmap.
     * @param angle The angle that the bitmap needs to be rotated.
     * @return The rotated bitmap.
     */
    private Bitmap rotateBitmap(Bitmap source, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    } //End method rotateBitmap.

    /**
     * Listener that triggers an action based on what button was pressed.
     *
     * @param v View of the button that was pressed.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
                NavUtils.navigateUpFromSameTask(CropActivity.this);
                break;
            case R.id.select_button:
                //Initializing image matrices.
                Mat srcImage = Imgcodecs.imread(getIntent().getStringExtra("image_path"));
                Imgproc.cvtColor(srcImage, srcImage, Imgproc.COLOR_RGB2BGR);
                Mat dstImage = new Mat(1000, 1000, srcImage.type());

                //Getting values that wil be used for calculations later on.
                float xAdjustment = imageView.getX();
                float yAdjustment = imageView.getY();
                float imageViewWidth = imageView.getWidth();
                float imageViewHeight = imageView.getHeight();
                float srcWidth = srcImage.cols();
                float srcHeight = srcImage.rows();

                //Calculating all the points.
                float x1 = (point1.getX() - xAdjustment + offCenterAdjustment) * (srcWidth / imageViewWidth);
                float x2 = (point2.getX() - xAdjustment + offCenterAdjustment) * (srcWidth / imageViewWidth);
                float x3 = (point3.getX() - xAdjustment + offCenterAdjustment) * (srcWidth / imageViewWidth);
                float x4 = (point4.getX() - xAdjustment + offCenterAdjustment) * (srcWidth / imageViewWidth);
                float y1 = (point1.getY() - yAdjustment + offCenterAdjustment) * (srcHeight / imageViewHeight);
                float y2 = (point2.getY() - yAdjustment + offCenterAdjustment) * (srcHeight / imageViewHeight);
                float y3 = (point3.getY() - yAdjustment + offCenterAdjustment) * (srcHeight / imageViewHeight);
                float y4 = (point4.getY() - yAdjustment + offCenterAdjustment) * (srcHeight / imageViewHeight);

                savedX1 = point1.getX();
                savedY1 = point1.getY();
                savedX2 = point2.getX();
                savedY2 = point2.getY();
                savedX3 = point3.getX();
                savedY3 = point3.getY();
                savedX4 = point4.getX();
                savedY4 = point4.getY();

                //Creating matrices more matrices of the target and destination point values.
                Mat src = new MatOfPoint2f(new Point(x1, y1), new Point(x2, y2), new Point(x3, y3), new Point(x4, y4));
                Mat dst = new MatOfPoint2f(new Point(0, 0), new Point(dstImage.width() - 1, 0), new Point(0, dstImage.height() - 1), new Point(dstImage.width() - 1, dstImage.height() - 1));

                //Doing the transformation.
                Mat transform = Imgproc.getPerspectiveTransform(src, dst);
                Imgproc.warpPerspective(srcImage, dstImage, transform, dstImage.size());

                transferImage = dstImage.clone();

                Intent intent = new Intent(getApplicationContext(), NamingActivity.class);
                intent.putExtra("file_path", getIntent().getStringExtra("image_path"));
                intent.putExtra("is_file_internal", getIntent().getBooleanExtra("is_file_internal", true));
                startActivity(intent);
                break;
        }
    } //End method onClick.

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
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dX = v.getX() - event.getRawX();
                dY = v.getY() - event.getRawY();
                canvas.updatePoints(point1.getX(), point1.getY(), point2.getX(), point2.getY(), point3.getX(), point3.getY(), point4.getX(), point4.getY());
                canvas.invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                v.animate().x(event.getRawX() + dX).y(event.getRawY() + dY).setDuration(0).start();
                canvas.updatePoints(point1.getX(), point1.getY(), point2.getX(), point2.getY(), point3.getX(), point3.getY(), point4.getX(), point4.getY());
                canvas.invalidate();
                break;
            default:
                return false;
        }
        return true;
    } //End method onTouch.
} //End class CropActivity.