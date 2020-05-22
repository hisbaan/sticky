package com.hisbaan.sticky.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Scanner;

/**
 * Canvas that draws the notes detailed in the file it is provided.
 */
public class Canvas extends View {
    //Initializing objects.
    Paint imagePaint = new Paint();
    String boardName;

    public Canvas(Context context) {
        super(context);
    } //End Constructor Canvas.

    public Canvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    } //End Constructor Canvas.

    public Canvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    } //End Constructor Canvas.

    public Canvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    } //End Constructor Canvas.

    /**
     * Draw method that paints the notes based on information it is given.
     *
     * @param canvas Canvas that is passed to the method.
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);

        //Reading the file based on the name given.
        String file = null;
        FileInputStream fis = null;
        try {
            fis = getContext().openFileInput(boardName);
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

        //TODO save any changes the user makes on pause or something like that. Whatever pause method works best. Save what the user does while they're doing it or get info from the canvas somehow.

        //Loading any previous data in the note
        if (file != null) {
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String[] info = sc.nextLine().split(",");

                Bitmap bmp = BitmapFactory.decodeFile(Objects.requireNonNull(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)).toString() + "/" + info[0] + "/" + info[1]);
                canvas.drawBitmap(bmp, Integer.parseInt(info[2]), Integer.parseInt(info[3]), imagePaint);
            }
        }
    } //End Method onDraw.

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    } //End Method setBoardName.
} //End Class Canvas.
