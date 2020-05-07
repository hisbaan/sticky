package com.hisbaan.sticky.models;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Item class that contains information about what is displayed as a folder preview (the four images).
 */
public class FolderItem {
    private Bitmap imageBitmap1;
    private Bitmap imageBitmap2;
    private Bitmap imageBitmap3;
    private Bitmap imageBitmap4;
    private String name;
    private int nullColor;

    /**
     * Constructor that check if the images passed to it are null and if they are, sets a default image.
     *
     * @param imageBitmap1 First image.
     * @param imageBitmap2 Second image.
     * @param imageBitmap3 Third image.
     * @param imageBitmap4 Fourth image.
     * @param name         The name of the folder.
     */
    public FolderItem(Bitmap imageBitmap1, Bitmap imageBitmap2, Bitmap imageBitmap3, Bitmap imageBitmap4, String name, int nullColor) {
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
        this.nullColor = nullColor;
    }

    /**
     * Coloring the null imageBitmap with the background color.
     *
     * @return A bitmap that is one solid color matching the background.
     */
    @SuppressLint("ResourceAsColor")
    private Bitmap makeNullBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(nullColor);
        canvas.drawRect(0F, 0F, 1000, 1000, paint);
        return bitmap;
    } //End Method makeNullBitmap.

    public Bitmap getImageBitmap1() {
        return imageBitmap1;
    } //End method getImageBitmap1.

    public Bitmap getImageBitmap2() {
        return imageBitmap2;
    } //End Method getImageBitmap2.

    public Bitmap getImageBitmap3() {
        return imageBitmap3;
    } //End Method getImageBitmap3.

    public Bitmap getImageBitmap4() {
        return imageBitmap4;
    } //End Method getImageBitmap4.

    public String getName() {
        return name;
    } //End Method getName.
} //End Class FolderItem.
