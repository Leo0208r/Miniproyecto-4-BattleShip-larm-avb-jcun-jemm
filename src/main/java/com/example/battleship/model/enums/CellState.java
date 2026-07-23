package com.example.battleship.model.enums;
/**
 * Represents the current state of a single cell on the Battleship grid.
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public enum CellState {
    /** Cell is empty and has not been targeted. */
    EMPTY("Empty"),
    /** Cell was targeted and contained water (a missed shot). */
    WATER("Water"),
    /** Cell contains a ship segment that has not been hit yet. */
    OCCUPIED("Occupied"),
    /** Cell was targeted and hit a ship segment. */
    HIT("Hit"),
    /** Cell belongs to a ship that has been completely destroyed and revealed. */
    SUNK("Sunk");
    /** Descriptive string symbol representing the cell state. */
    private String symbol;
    /**
     * Constructs a {@code CellState} with the associated text symbol representation.
     *
     * @param symbol Descriptive text string for the state.
     */
    CellState(String symbol){
        this.symbol=symbol;
    }
    /**
     * Gets the text symbol representation of the cell state.
     *
     * @return String description of the state.
     */
    public String getSymbol(){
        return symbol;
    }
}
