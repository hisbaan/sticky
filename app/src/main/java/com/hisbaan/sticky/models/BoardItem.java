package com.hisbaan.sticky.models;

/**
 * BoardItem model class that stores information about the boards for the recycler view.
 */
public class BoardItem {
    private String boardName;

    /**
     * Constructor that sets variables.
     *
     * @param boardName The name of the board.
     */
    public BoardItem(String boardName) {
        this.boardName = boardName;
    } //End constructor BoardItem.

    /**
     * Getter method for the board name.
     *
     * @return The board name.
     */
    public String getBoardName() {
        return boardName;
    } //End method getBoardName.

    /**
     * Setter method for the board name.
     *
     * @param boardName The new name of the board.
     */
    public void setBoardName(String boardName) {
        this.boardName = boardName;
    } //End method setBoardName.
} //End class BoardItem.