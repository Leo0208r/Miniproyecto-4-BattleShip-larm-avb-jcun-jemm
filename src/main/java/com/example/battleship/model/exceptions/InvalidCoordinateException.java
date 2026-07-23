package com.example.battleship.model.exceptions;
/**
 * Exception thrown when an invalid grid coordinate is accessed or targeted.
 * <p>
 * Stores the specific row and column indices that triggered the exception
 * to facilitate error tracking and debugging.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class InvalidCoordinateException extends RuntimeException {
    /** Row index that caused the exception. */
    private final int row;
    /** Column index that caused the exception. */
    private final int col;
    /**
     * Constructs a new {@code InvalidCoordinateException} with a detailed message
     * and the problematic coordinate values.
     *
     * @param message Detailed error message.
     * @param row     The invalid row index.
     * @param col     The invalid column index.
     */
    public InvalidCoordinateException(String message, int row, int col){
        super(message);
        this.row=row;
        this.col=col;
    }
    /**
     * Gets the invalid row index that caused the exception.
     *
     * @return Row index.
     */
    public int getRow(){return row;}
    /**
     * Gets the invalid column index that caused the exception.
     *
     * @return Column index.
     */
    public int getCol(){return col;}
}
