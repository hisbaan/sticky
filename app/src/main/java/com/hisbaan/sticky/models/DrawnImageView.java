package com.hisbaan.sticky.models;

/**
 * DrawnImageView model class to store information about the notes drawn in the board activity.
 */
public class DrawnImageView {
    private final String groupName;
    private final String noteName;

    /**
     * Constructor that sets the folder name and the note name for the note.
     *
     * @param groupName The name of the folder the note is in.
     * @param noteName  The name of the note.
     */
    public DrawnImageView(String groupName, String noteName) {
        this.groupName = groupName;
        this.noteName = noteName;
    } //End constructor DrawnImageView.

    /**
     * Getter method for the folder name.
     *
     * @return The folder name.
     */
    public String getGroupName() {
        return groupName;
    } //End method getGroupName.

    /**
     * Getter method for the note name.
     *
     * @return The note name.
     */
    public String getNoteName() {
        return noteName;
    } //End method getNoteName.
} //End class DrawnImageView.
