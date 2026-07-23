package com.example.battleship.model.exceptions;
/**
 * Exception thrown when attempting to place a ship on coordinates that overlap
 * with another ship already positioned on the board.
 * <p>
 * Ensures that ships maintain valid, non-overlapping placements on the grid layout.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class ShipOverLapException extends Exception {
    /**
     * Constructs a new {@code ShipOverLapException} with a descriptive message
     * explaining the placement overlap error.
     *
     * @param message Detailed error message describing the overlap conflict.
     */
    public ShipOverLapException(String message) {
        super(message);
    }
}
