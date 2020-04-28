package com.hisbaan.sticky;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

/**
 * Item class that contains information about what is displayed as a folder preview (the four images).
 */
public class FolderItem {
    private Bitmap imageBitmap1;
    private Bitmap imageBitmap2;
    private Bitmap imageBitmap3;
    private Bitmap imageBitmap4;
    private String name;

    public FolderItem(Bitmap imageBitmap1, Bitmap imageBitmap2, Bitmap imageBitmap3, Bitmap imageBitmap4, String name) {
        if (imageBitmap1 == null) {
            this.imageBitmap1 = makeNullBitmap();
        } else {
            this.imageBitmap1 = imageBitmap1;
        }
        if (imageBitmap2 == null) {
            this.imageBitmap2 = makeNullBitmap();
        } else {
            this.imageBitmap2 = imageBitmap2;
        }
        if (imageBitmap3 == null) {
            this.imageBitmap3 = makeNullBitmap();
        } else {
            this.imageBitmap3 = imageBitmap3;
        }
        if (imageBitmap4 == null) {
            this.imageBitmap4 = makeNullBitmap();
        } else {
            this.imageBitmap4 = imageBitmap4;
        }

        this.name = name;
    }

    @SuppressLint("ResourceAsColor")
    private Bitmap makeNullBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(R.color.colorSubText);
        canvas.drawRect(0F, 0F, 1000, 1000, paint);
        return bitmap;
    }

    Bitmap getImageBitmap1() {
        return imageBitmap1;
    }

    Bitmap getImageBitmap2() {
        return imageBitmap2;
    }

    Bitmap getImageBitmap3() {
        return imageBitmap3;
    }

    Bitmap getImageBitmap4() {
        return imageBitmap4;
    }

    public String getName() {
        return name;
    }
}
