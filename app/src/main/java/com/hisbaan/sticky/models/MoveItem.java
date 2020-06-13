package com.hisbaan.sticky.models;

/**
 * MoveItem model class that stores information for the note name for the recycler view.
 */
public class MoveItem {
    String boardName;

    /**
     * Constructor that sets the board name.
     * @param boardName The name of the board.
     */
    public MoveItem(String boardName) {
        this.boardName = boardName;
    } //End constructor MoveItem.

    /**
     * Getter method for the board name.
     * @return The board name.
     */
    public String getBoardName() {
        return boardName;
    } //End method getBoardName.
} //End class MoveItem.
