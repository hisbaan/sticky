package com.hisbaan.sticky.models;

public class DrawnImageView {
    private String groupName;
    private String noteName;

    public DrawnImageView(String groupName, String noteName) {
        this.groupName = groupName;
        this.noteName = noteName;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getNoteName() {
        return noteName;
    }
}
