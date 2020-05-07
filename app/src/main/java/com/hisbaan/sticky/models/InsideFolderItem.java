package com.hisbaan.sticky.models;

import android.graphics.Bitmap;

public class InsideFolderItem {
    private Bitmap image;
    private String name;

    public InsideFolderItem(Bitmap image, String name) {
        this.image = image;
        this.name = name;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
