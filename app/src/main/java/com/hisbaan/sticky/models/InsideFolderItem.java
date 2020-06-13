package com.hisbaan.sticky.models;

import android.graphics.Bitmap;

/**
 * InsideFolderItem model class that stores information for the recycler view inside of the folders.
 */
public class InsideFolderItem {
    private Bitmap image;
    private String name;

    /**
     * Constructor that sets the image and note name.
     *
     * @param image The image of the note.
     * @param name  The name of the note.
     */
    public InsideFolderItem(Bitmap image, String name) {
        this.image = image;
        this.name = name;
    } //End constructor InsideFolderItem.

    /**
     * Setter method for the name of the note.
     *
     * @param name The name of the note.
     */
    public void setName(String name) {
        this.name = name;
    } //End method setName.

    /**
     * Getter method for the image.
     *
     * @return The image tied to the note.
     */
    public Bitmap getImage() {
        return image;
    } //End method getImage.

    /**
     * Getter method for the note name.
     *
     * @return The name of the note.
     */
    public String getName() {
        return name;
    } //End method getName.
} //End class InsideFolderItem.
