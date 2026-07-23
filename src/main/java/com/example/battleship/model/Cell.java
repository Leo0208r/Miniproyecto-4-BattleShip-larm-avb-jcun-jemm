package com.example.battleship.model;

import com.example.battleship.model.enums.CellState;
import com.example.battleship.model.exceptions.RepeatedShotException;
import com.example.battleship.model.ships.Ship;
import java.io.Serializable;
/**
 * Represents an individual cell within the Battleship game board.
 * <p>
 * Tracks its grid coordinate, occupation by a vessel, and current cell state
 * (EMPTY, OCCUPIED, WATER, HIT, or SUNK). Handles incoming shots and delegates hit/sink
 * tracking to the assigned ship when applicable.
 * Implements {@link Serializable} for game state saving.
 * </p>

 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class Cell implements Serializable {
    /** Serial version UID for serialization compatibility. */
    private static final long serialVersionUID = 1L;
    /** Current state of the cell. */
    private CellState cellState;
    /** Grid coordinate of this cell. */
    private final Coordinate coordinate;
    /** Reference to the ship assigned to this cell, if any. */
    private Ship ship;
    /**
     * Constructs a new {@code Cell} at the specified coordinate with default state {@link CellState#EMPTY}.
     *
     * @param coordinate Position on the grid.
     * @throws IllegalArgumentException If coordinate is {@code null}.
     */
    public Cell(Coordinate coordinate){
        if (coordinate==null){
            throw new IllegalArgumentException("Coordinate cannot be null");
        }
        this.coordinate=coordinate;
        cellState=CellState.EMPTY;
        ship=null;
    }
    /**
     * Gets the current state of this cell.
     *
     * @return Current {@link CellState}.
     */
    public CellState getCellState(){return cellState;}
    /**
     * Sets the state of this cell.
     *
     * @param cellState New {@link CellState} to set.
     * @throws IllegalArgumentException If cellState is {@code null}.
     */
    public void setCellState(CellState cellState){
        if (cellState == null) {
            throw new IllegalArgumentException("State cannot be null");
        }
        this.cellState=cellState;}
    /**
     * Gets the coordinate of this cell.
     *
     * @return Grid {@link Coordinate}.
     */
    public Coordinate getCoordinate(){return coordinate;}
    /**
     * Gets the ship occupying this cell, if present.
     *
     * @return Occupying {@link Ship}, or {@code null} if empty.
     */
    public Ship getShip(){return ship;}
    /**
     * Assigns a ship to this cell and sets its state to {@link CellState#OCCUPIED}.
     *
     * @param ship The vessel to assign.
     * @throws IllegalArgumentException If ship is {@code null}.
     * @throws IllegalStateException    If the cell already contains a ship.
     */
    public void assignShip(Ship ship){
        if (ship==null){
            throw new IllegalArgumentException("Ship cant be null");
        }
        if (hasShip()){
            throw new IllegalStateException("cell"+coordinate+" is already occupied");
        }
        this.ship=ship;
        this.cellState=CellState.OCCUPIED;
    }
    /**
     * Checks whether this cell is occupied by a ship.
     *
     * @return {@code true} if a ship is assigned; {@code false} otherwise.
     */
    public boolean hasShip(){
        return ship!=null;
    }
    /**
     * Processes an incoming shot directed at this cell.
     * <p>
     * - Transitions EMPTY cells to WATER.<br>
     * - Transitions OCCUPIED cells to HIT and checks if the ship is completely SUNK.
     * </p>
     *
     * @return Resulting {@link CellState} (WATER, HIT, or SUNK).
     * @throws RepeatedShotException If this cell was previously targeted.
     */
    public CellState receiveShot()throws RepeatedShotException{
        if (cellState==CellState.EMPTY){
            setCellState(CellState.WATER);
            return cellState;
        }
        else if (cellState==CellState.OCCUPIED){
            setCellState(CellState.HIT);
            if(ship.registerHit()){
               return CellState.SUNK;
            }
            return CellState.HIT;
        }else{
            throw new RepeatedShotException("Cell already shot, current state: "+cellState.getSymbol(), cellState );
        }
    }
    /**
     * Returns a string representation of the cell including coordinate and state.
     *
     * @return String descriptive of the cell state.
     */
    @Override
    public String toString(){
        return "Cell"+coordinate+"[State="+cellState+"]";
    }
}
