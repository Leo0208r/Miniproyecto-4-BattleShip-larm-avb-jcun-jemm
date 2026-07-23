package com.example.battleship.model.enums;
/**
 * Represents the directional orientation of a ship when placed on the grid.
 * <p>
 * Contains a text symbol description for logging or display purposes.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public enum Orientation {
    /** Ship is positioned horizontally along columns in the same row. */
    HORIZONTAL("Horizontal"),
    /** Ship is positioned vertically along rows in the same column. */
    VERTICAL("Vertical");
    /** Descriptive string symbol representing the orientation. */
    private String symbol;
    /**
     * Constructs an {@code Orientation} with its associated text symbol.
     *
     * @param symbol Descriptive text string for the orientation.
     */
    Orientation(String symbol){
        this.symbol=symbol;
    }
    /**
     * Gets the text symbol representation of the orientation.
     *
     * @return String description of the orientation.
     */
    public String getSymbol(){
        return symbol;
    }
}
