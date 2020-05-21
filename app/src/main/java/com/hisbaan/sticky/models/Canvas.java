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

public class Canvas extends View {
    Paint imagePaint = new Paint();
    Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    String boardName;

    public Canvas(Context context) {
        super(context);
    }

    public Canvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Canvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Canvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);

        textPaint.setTextSize(100);
        textPaint.setColor(Color.WHITE);

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

        assert file != null;
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {
            String[] info = sc.nextLine().split(",");

            Bitmap bmp = BitmapFactory.decodeFile(Objects.requireNonNull(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)).toString() + "/" + info[0] + "/" + info[1]);
            canvas.drawBitmap(bmp, Integer.parseInt(info[2]), Integer.parseInt(info[3]), imagePaint);
        }
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }
}
