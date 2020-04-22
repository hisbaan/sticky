package com.hisbaan.sticky;

//Importing android packages
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

//Importing openCV packages
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
//import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * Displays the image that was captured immediately before this activity is triggered.
 */
public class CameraActivity extends AppCompatActivity {

    //Declaring variable
    private ImageView imageView;

    //Variables for corner detection
    private Mat srcGray = new Mat();
    private Mat dst = new Mat();
    private Mat dstNorm = new Mat();
    private Mat dstNormScaled = new Mat();
    private static final int MAX_THRESHOLD = 255;
    private int threshold = 200;

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

        String filename = getIntent().getStringExtra("image_path");

        //Corner detection
        Mat src = Imgcodecs.imread(filename);
        if (src.empty()) {
            //TODO send an error here and open feedback to contact the developer
        }

        Imgproc.cvtColor(src, srcGray, Imgproc.COLOR_BGR2GRAY);


        //Displaying image for manual cropping
        imageView = findViewById(R.id.image_view);
        //Declaring and initialising bitmap that is used to display the captured image in the activity.
        Bitmap bitmap = BitmapFactory.decodeFile(filename);
        //Setting imageView to display the bitmap.
        imageView.setImageBitmap(bitmap);
    } //End Method onCreate.

    private void addComponentsToPane() {
        //add a slider here that changes the threshold variable and then runs the update method again
        //also make this method initialize the Mat blackImg and add the image to the screen
        //basically, look at the method given and adapt it to work with android
        //website --> https://docs.opencv.org/3.4/d4/d7d/tutorial_harris_detector.html
    }

    private void update() {
        //again follow the website.
        dst = Mat.zeros(srcGray.size(), CvType.CV_32F);

        int blockSize = 2;
        int apertureSize = 3;
        double k = 0.04;

        Imgproc.cornerHarris(srcGray, dst, blockSize, apertureSize, k);

        Core.normalize(dst, dstNorm, 0, 255, Core.NORM_MINMAX);
        Core.convertScaleAbs(dstNorm, dstNormScaled);

        float[] dstNormData = new float[(int) (dstNorm.total() * dstNorm.channels())];
        dstNorm.get(0,0, dstNormData);

        for (int i = 0; i < dstNorm.rows(); i++) {
            for (int j = 0; j < dstNorm.cols(); j++) {
                if ((int) dstNormData[i* dstNorm.cols() + j] > threshold) {
                    Imgproc.circle(dstNormScaled, new Point(j, i), 5, new Scalar(0), 2, 8, 0);
                }
            }
        }

        //draw circle on the image and refresh it.
        //cornerLabel.setIcon(new ImageIcon(HighGui.toBufferedImage(dstNormScaled)));

    }


} //End Class CameraActivity.
