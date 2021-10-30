package com.hisbaan.sticky.models;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * FolderItem model class that contains information about what is displayed as a folder preview.
 */
public class FolderItem {
    private final Bitmap imageBitmap1;
    private final Bitmap imageBitmap2;
    private final Bitmap imageBitmap3;
    private final Bitmap imageBitmap4;
    private String name;
    private final int nullColor;

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
    } //End method makeNullBitmap.

    /**
     * Getter method for the first image.
     *
     * @return The first image.
     */
    public Bitmap getImageBitmap1() {
        return imageBitmap1;
    } //End method getImageBitmap1.

    /**
     * Getter method for the second image.
     *
     * @return The second image.
     */
    public Bitmap getImageBitmap2() {
        return imageBitmap2;
    } //End method getImageBitmap2.

    /**
     * Getter method for the third image.
     *
     * @return The third image.
     */
    public Bitmap getImageBitmap3() {
        return imageBitmap3;
    } //End method getImageBitmap3.

    /**
     * Getter method for the fourth image.
     *
     * @return The fourth image.
     */
    public Bitmap getImageBitmap4() {
        return imageBitmap4;
    } //End method getImageBitmap4.

    /**
     * Getter method for the folder name.
     *
     * @return The name of the folder.
     */
    public String getName() {
        return name;
    } //End method getName.

    /**
     * Setter method for the folder name.
     *
     * @param name The new name of the folder.
     */
    public void setName(String name) {
        this.name = name;
    } //End method setName.
} //End class FolderItem.
