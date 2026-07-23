package com.example.battleship.model.exceptions;

import com.example.battleship.model.Cell;

import java.util.ArrayList;
import java.util.List;
/**
 * Exception thrown when a ship's expected size does not match the actual number of grid cells provided.
 * <p>
 * Stores both the expected ship size and the actual cell count that triggered the validation error.
 * </p>

 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class InvalidShipSizeException extends RuntimeException {
    /** The expected size of the ship in cells. */
    private final int sizeShip;
    /** The actual number of cells provided for the ship. */
    private final int sizeCells;
    /**
     * Constructs a new {@code InvalidShipSizeException} with a descriptive error message,
     * the expected ship size, and the actual cell count received.
     *
     * @param message   Detailed error explanation.
     * @param sizeShip  Expected size of the ship.
     * @param sizeCells Actual number of cells supplied.
     */
    public InvalidShipSizeException(String message, int sizeShip, int sizeCells){
        super(message);
        this.sizeShip=sizeShip;
        this.sizeCells=sizeCells;
    }
    /**
     * Gets the expected size of the ship in grid cells.
     *
     * @return Expected ship size.
     */
    public int getSizeShip(){return sizeShip;}
    /**
     * Gets the actual number of cells provided.
     *
     * @return Actual cell count.
     */
    public int getSizeCells(){return sizeCells;}
}
