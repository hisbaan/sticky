package com.hisbaan.sticky;

//Importing android packages

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NavUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

////Importing openCV packages
//import org.opencv.android.Utils;
//import org.opencv.core.Core;
//import org.opencv.core.CvException;
//import org.opencv.core.CvType;
//import org.opencv.core.Mat;
//import org.opencv.core.Point;
//import org.opencv.core.Scalar;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.imgproc.Imgproc;
//Java imports

/**
 * Displays the image that was captured immediately before this activity is triggered.
 */
public class CameraActivity extends AppCompatActivity {

    //Declaring variable
    private ImageView imageView;

    //Variables for corner detection
//    private Mat src = new Mat();
//    private Mat srcGray = new Mat();
//    private int maxCorners = 12;
//    private static final int MAX_THRESHOLD = 100;
//    private int threshold = 175;
//    private Random rng = new Random(12345);

    /**
     * Initializes variables, creates a bitmap of the image that was captured and pushes the bitmap to an imageView in the program when the program.
     *
     * @param savedInstanceState Saved information that has been placed in it.
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Toolbar toolbar = findViewById(R.id.toolbar_camera);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(CameraActivity.this);
            }
        });
        setSupportActionBar(toolbar);

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

        Button selectButton = findViewById(R.id.select_button);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CameraActivity.this, "Sticky note cropped", Toast.LENGTH_SHORT).show();

            }
        });

        FloatingActionButton point1 = findViewById(R.id.point_1);
        FloatingActionButton point2 = findViewById(R.id.point_2);
        FloatingActionButton point3 = findViewById(R.id.point_3);
        FloatingActionButton point4 = findViewById(R.id.point_4);

        TouchListener touchListener = new TouchListener();
        point1.setOnTouchListener(touchListener);
        point2.setOnTouchListener(touchListener);
        point3.setOnTouchListener(touchListener);
        point4.setOnTouchListener(touchListener);


        String filename = getIntent().getStringExtra("image_path");
        Toast.makeText(this, filename, Toast.LENGTH_SHORT).show();

        //Corner detection
//        src = Imgcodecs.imread(filename);
//        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2RGB);
//        if (src.empty()) {
//            //TODO send an error here and open feedback to contact the developer
//        }
//
//        Imgproc.cvtColor(src, srcGray, Imgproc.COLOR_RGB2GRAY);
//        update();

        //Displaying image for manual cropping
        imageView = findViewById(R.id.image_view);
        //Declaring and initialising bitmap that is used to display the captured image in the activity.
        Bitmap bitmap = BitmapFactory.decodeFile(filename);
        //Setting imageView to display the bitmap.
        imageView.setImageBitmap(bitmap);
//        update();
    } //End Method onCreate.

    public class TouchListener implements View.OnTouchListener {
        float dX, dY;

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    dX = v.getX() - event.getRawX();
                    dY = v.getY() - event.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:

                    v.animate()
                            .x(event.getRawX() + dX)
                            .y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                    break;
                default:
                    return false;
            }
            return true;
        }
    }

//    private View.OnTouchListener onTouchListener() {
//        return new View.OnTouchListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                final int X = (int) event.getRawX();
//                final int Y = (int) event.getRawY();
//                switch (event.getAction() & MotionEvent.ACTION_MASK) {
//                    case MotionEvent.ACTION_DOWN:
//                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
//                        xDelta = X - lParams.leftMargin;
//                        yDelta = Y - lParams.topMargin;
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        break;
//                    case MotionEvent.ACTION_POINTER_DOWN:
//                        break;
//                    case MotionEvent.ACTION_POINTER_UP:
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
//                        layoutParams.leftMargin = X - xDelta;
//                        layoutParams.topMargin = Y - yDelta;
//                        layoutParams.rightMargin = -250;
//                        layoutParams.bottomMargin = -250;
//                        view.setLayoutParams(layoutParams);
//                        break;
//                }
//                root.invalidate();
//                return true;
//            }
//        };
//    }


//    private View.OnTouchListener onTouchListener() {
//        return new View.OnTouchListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int X = (int) event.getRawX();
//                final int Y = (int) event.getRawY();
//
//                switch (event.getAction() & MotionEvent.ACTION_MASK) {
//                    case MotionEvent.ACTION_DOWN:
//                        ConstraintLayout.LayoutParams layoutParamsDown = (ConstraintLayout.LayoutParams) v.getLayoutParams();
//                        xDelta = X - layoutParamsDown.leftMargin;
//                        yDelta = Y - layoutParamsDown.topMargin;
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        ConstraintLayout.LayoutParams layoutParamsMove = (ConstraintLayout.LayoutParams) v.getLayoutParams();
//                        layoutParamsMove.leftMargin = X - xDelta;
//                        layoutParamsMove.topMargin = Y - yDelta;
//                        layoutParamsMove.rightMargin = -250;
//                        layoutParamsMove.bottomMargin = -250;
//                        imageView.setLayoutParams(layoutParamsMove);
//                        break;
//                }
//                mainLayout.invalidate();
//                return true;
//            }
//        };
//
//    }

/**
 * Runs the corner detection, saves the file and then puts it on the screen.
 */
//    private void update() {
//        maxCorners = Math.max(maxCorners, 1);
//        MatOfPoint corners = new MatOfPoint();
//        double qualityLevel = 0.1;
//        double minDistance = 500;
//        int blockSize = 3;
//        int gradientSize = 3;
//        boolean useHarrisDetector = false;
//        double k = 0.04;
//
//        Mat copy = new Mat();
//
//        for (int i = 1; i < 31; i += 2) {
//            Imgproc.GaussianBlur(src, copy, new Size(i, i), 0, 0);
//        }
//
//        Imgproc.goodFeaturesToTrack(srcGray, corners, maxCorners, qualityLevel, minDistance, new Mat(), blockSize, gradientSize, useHarrisDetector, k);
//        System.out.println("Number of corners detected: " + corners.rows());
//        int[] cornersData = new int[(int) (corners.total() * corners.channels())];
//        corners.get(0, 0, cornersData);
//        int radius = 40;
//
//        for (int i = 0; i < corners.rows(); i++) {
//            Imgproc.circle(copy, new Point(cornersData[i * 2], cornersData[i * 2 + 1]), radius, new Scalar(255, 0, 0), Core.FILLED);
//        }
//
//        String filename = getIntent().getStringExtra("image_path");
//        assert filename != null;
//        File file = new File(getFilesDir(), filename);
//
//        Imgcodecs.imwrite(filename + "-corners.jpg", copy);
//
////        Displaying the matrix with the dots on it.
//        Bitmap bmp = null;
//
//        try {
//            bmp = Bitmap.createBitmap(copy.cols(), copy.rows(), Bitmap.Config.ARGB_8888);
//            Utils.matToBitmap(copy, bmp);
//        } catch (CvException e) {
//            System.out.println(e.getMessage());
//        }
//
//        imageView = findViewById(R.id.image_view);
//        imageView.setImageBitmap(bmp);
//    }
} //End Class CameraActivity.
