package com.example.battleship.model.exceptions;

import com.example.battleship.model.enums.CellState;
/**
 * Exception thrown when a player attempts to shoot at a coordinate that has already been targeted.
 * <p>
 * Stores the current {@link CellState} of the target cell to provide contextual information
 * regarding why the shot was rejected (e.g., cell is already marked as WATER, HIT, or SUNK).
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class RepeatedShotException extends Exception {
    /** The state of the targeted cell that triggered the exception. */
    private final CellState cellState;
    /**
     * Constructs a new {@code RepeatedShotException} with a descriptive message
     * and the state of the previously targeted cell.
     *
     * @param message   Detailed explanation of the repeated shot error.
     * @param cellState The current state of the cell (WATER, HIT, SUNK).
     */
    public RepeatedShotException(String message, CellState cellState) {
        super(message);
        this.cellState=cellState;
    }
    /**
     * Gets the cell state at the attempted shooting coordinate.
     *
     * @return The {@link CellState} of the targeted cell.
     */
    public CellState getCellState(){return cellState;}
}
