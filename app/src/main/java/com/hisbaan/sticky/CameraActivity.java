package com.hisbaan.sticky;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

// (for corner detection that is off right now)
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
public class CameraActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    //Declaring variables for cropping
    String filename;
    private ImageView imageView;

    //Declaring variables for dragging the points around.
    float dX;
    float dY;
    FloatingActionButton point1;
    FloatingActionButton point2;
    FloatingActionButton point3;
    FloatingActionButton point4;

    //Variables for corner detection (off right now)
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(this);
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

        filename = getIntent().getStringExtra("image_path");

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
//        Bitmap bitmap = BitmapFactory.decodeFile("/storage/emulated/0/Android/data/com.hisbaan.sticky/files/Pictures/TestImage.jpg");
        //Setting imageView to display the bitmap.
        imageView.setImageBitmap(bitmap);
//        update();
    } //End Method onCreate.

    /**
     * Listener that triggers an action based on what button was pressed.
     *
     * @param v View of the button that was pressed.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
            case R.id.toolbar:
                NavUtils.navigateUpFromSameTask(CameraActivity.this);
                break;
            case R.id.select_button:
                //Initializing image matrices.
                Mat srcImage = Imgcodecs.imread(filename);
                Imgproc.cvtColor(srcImage, srcImage, Imgproc.COLOR_RGB2BGR);
                Mat dstImage = new Mat(1000, 1000, srcImage.type());

                //Getting values that wil be used for calculations later on.
                float xAdjustment = imageView.getX();
                float yAdjustment = imageView.getY();
                float imageViewWidth = imageView.getWidth();
                float imageViewHeight = imageView.getHeight();
                float srcWidth = srcImage.cols();
                float srcHeight = srcImage.rows();

                //Converting density independent pixels to regular pixels.
                Resources r = getResources();
                float offCenterAdjustment = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());

                //Calculating all the points.
                float x1 = (point1.getX() - xAdjustment + offCenterAdjustment) * (srcWidth / imageViewWidth);
                float x2 = (point2.getX() - xAdjustment + offCenterAdjustment) * (srcWidth / imageViewWidth);
                float x3 = (point3.getX() - xAdjustment + offCenterAdjustment) * (srcWidth / imageViewWidth);
                float x4 = (point4.getX() - xAdjustment + offCenterAdjustment) * (srcWidth / imageViewWidth);
                float y1 = (point1.getY() - yAdjustment + offCenterAdjustment) * (srcHeight / imageViewHeight);
                float y2 = (point2.getY() - yAdjustment + offCenterAdjustment) * (srcHeight / imageViewHeight);
                float y3 = (point3.getY() - yAdjustment + offCenterAdjustment) * (srcHeight / imageViewHeight);
                float y4 = (point4.getY() - yAdjustment + offCenterAdjustment) * (srcHeight / imageViewHeight);

                //TODO find a way to remove this block without breaking the obj address.
                //Hiding the points that were used for corner selection.
                setFAB(point1, false);
                setFAB(point2, false);
                setFAB(point3, false);
                setFAB(point4, false);

                //Creating matrices more matrices of the target and destination point values.
                Mat src = new MatOfPoint2f(new Point(x1, y1), new Point(x2, y2), new Point(x3, y3), new Point(x4, y4));
                Mat dst = new MatOfPoint2f(new Point(0, 0), new Point(dstImage.width() - 1, 0), new Point(0, dstImage.height() - 1), new Point(dstImage.width() - 1, dstImage.height() - 1));

                //Doing the transformation.
                Mat transform = Imgproc.getPerspectiveTransform(src, dst);
                Imgproc.warpPerspective(srcImage, dstImage, transform, dstImage.size());

                Intent intent = new Intent(getApplicationContext(), NamingActivity.class);
                long addr = dstImage.getNativeObjAddr();
                intent.putExtra("dst_image_addr", addr);
                startActivity(intent);
                break;
        }

    }

    /**
     * Method that is in place because something else breaks without it (see above TO-DO).
     */
    @Override
    protected void onResume() {
        super.onResume();

        setFAB(point1, true);
        setFAB(point2, true);
        setFAB(point3, true);
        setFAB(point4, true);
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
    }

    /**
     * Sets the state of the FAB passed to it to be clickable or un-clickable.
     *
     * @param fab   FloatingActionButton that is to have its intractability modified.
     * @param state Decides whether the passed FloatingActionButton will be clickable or not.
     */
    private void setFAB(FloatingActionButton fab, Boolean state) {
        fab.setEnabled(state);
        fab.setClickable(state);
        fab.setFocusable(state);
        if (state) {
            fab.show();
        } else {
            fab.hide();
        }
    } //End Method setFAB.

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
