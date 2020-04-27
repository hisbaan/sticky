package com.hisbaan.sticky;

/**
 * Item class that contains information about what is displayed as a folder preview (the four images).
 */
public class FolderItem {
    private int imageResource;
    private String text1;
    private String text2;

    FolderItem(int imageResource, String text1, String text2) {
        this.imageResource = imageResource;
        this.text1 = text1;
        this.text2 = text2;
    }

    int getImageResource() {
        return imageResource;
    }

    String getText1() {
        return text1;
    }

    String getText2() {
        return text2;
    }
}
