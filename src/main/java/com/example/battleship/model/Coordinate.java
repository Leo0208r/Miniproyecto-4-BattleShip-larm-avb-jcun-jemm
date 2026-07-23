package com.example.battleship.model;

import com.example.battleship.model.exceptions.InvalidCoordinateException;
import java.io.Serializable;
/**
 * Represents an immutable 2D grid position on the Battleship board.
 * <p>
 * Enforces valid board boundaries (0–9 for rows and columns) upon creation and
 * provides utility formatting for standard Battleship notation (e.g., A1, B5, J10).
 * Implements {@link Serializable} for state saving.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class Coordinate implements Serializable {
    /** Serial version UID for serialization compatibility. */
    private static final long serialVersionUID = 1L;
    /** Zero-indexed row coordinate (0–9). */
    private final int row;
    /** Zero-indexed column coordinate (0–9). */
    private final int col;
    /**
     * Constructs a new {@code Coordinate} with validated row and column values.
     *
     * @param row Row index (0 to 9).
     * @param col Column index (0 to 9).
     * @throws InvalidCoordinateException If row or column falls outside the 10x10 board bounds.
     */
    public Coordinate(int row, int col) throws InvalidCoordinateException{
        if(validate(row,col)){
            this.row=row;
            this.col=col;
        }else{
            throw new InvalidCoordinateException("Row or Col outside the table", row ,col);
        }
    }
    /**
     * Gets the zero-indexed row position.
     *
     * @return Row index.
     */
    public int getRow(){return row;}
    /**
     * Gets the zero-indexed column position.
     *
     * @return Column index.
     */
    public int getCol(){return col;}
    /**
     * Validates whether the given row and column indices lie within the 10x10 grid boundaries.
     *
     * @param row Row index.
     * @param col Column index.
     * @return {@code true} if indices are between 0 and 9 inclusive; {@code false} otherwise.
     */
    private boolean validate(int row, int col){
        return row>=0 && row<=9 && col>=0 && col<=9;
    }
    /**
     * Returns the formatted Battleship notation string (e.g., "A1", "C7").
     *
     * @return Grid position in letter-number format.
     */
    @Override
    public String toString(){
        char columnLetter=(char)('A'+col);
        String stringColumnLetter=String.valueOf(columnLetter);
        return stringColumnLetter+(row+1);
    }
    /**
     * Compares this coordinate with another object for structural equality.
     *
     * @param obj Target object to compare.
     * @return {@code true} if both coordinates share identical row and column indices.
     */
    @Override
    public boolean equals(Object obj) {
        if (this==obj){
            return true;
        }
        if(!(obj instanceof Coordinate other)){return false;}
        return row==other.row && col==other.col;
    }
    /**
     * Computes the hash code for efficient map/set operations.
     *
     * @return Unique hash code representation.
     */
    @Override
    public int hashCode(){
        return 31*row+col;
    }

}
