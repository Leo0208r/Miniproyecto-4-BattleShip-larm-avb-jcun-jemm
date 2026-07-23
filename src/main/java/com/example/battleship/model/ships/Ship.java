package com.example.battleship.model.ships;

import com.example.battleship.model.Cell;
import com.example.battleship.model.enums.CellState;
import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.exceptions.InvalidShipSizeException;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
/**
 * Abstract base class representing a naval vessel on the game board.
 * <p>
 * Encapsulates the vessel's size, orientation, and occupied grid cells.
 * Provides mechanisms to validate ship dimensions, verify hit status, and update
 * cell states when a ship is fully destroyed (sunk).
 * Implements {@link Serializable} to support match persistence.
 * </p>

 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public abstract class Ship implements Serializable {
    /** Serial version UID for serialization compatibility. */
    private static final long serialVersionUID = 1L;
    /** The length of the ship in grid cells. */
    private final int size;
    /** The layout orientation (HORIZONTAL or VERTICAL) of the ship. */
    private final Orientation orientation;
    /** List of grid cells occupied by this ship. */
    private final List<Cell> cells;
    /**
     * Constructs a new {@code Ship} with a designated size, orientation, and list of occupied cells.
     *
     * @param size        Expected number of cells occupied by the ship.
     * @param orientation Alignment direction of the ship on the board.
     * @param cells       List of {@link Cell} objects assigned to this ship.
     * @throws InvalidShipSizeException If the number of cells provided does not match the expected size.
     */
    public Ship(int size, Orientation orientation, List<Cell> cells){
        this.size=size;
        this.orientation=orientation;
        if(validateSize(size,cells.size())){
            this.cells=cells;
        }else{
            throw new InvalidShipSizeException("The size of the ship does not match the size on the list",size,cells.size());
        }
    }
    /**
     * Gets the expected length of the ship in grid cells.
     *
     * @return Ship size.
     */
    public int getSize(){return size;}
    /**
     * Gets the placement orientation of the ship.
     *
     * @return {@link Orientation} enum value.
     */
    public Orientation getOrientation(){return orientation;}
    /**
     * Gets an unmodifiable view of the cells occupied by this ship.
     *
     * @return Unmodifiable {@link List} of {@link Cell} instances.
     */
    public List<Cell> getCells(){return Collections.unmodifiableList(cells);}
    /**
     * Checks if all cells occupied by this ship have been hit.
     * <p>
     * If all cells are hit, their states are updated to {@link CellState#SUNK}.
     * </p>
     *
     * @return {@code true} if all cells are hit and transitioned to SUNK; {@code false} otherwise.
     */
    public boolean registerHit(){
        for(Cell cell: cells){
            if (cell.getCellState()!=CellState.HIT){
                return false;
            }
        }
        for (Cell cell: cells){
            cell.setCellState(CellState.SUNK);
        }
        return true;
    }
    /**
     * Checks whether the ship has been completely sunk.
     *
     * @return {@code true} if every cell in the ship has state SUNK; {@code false} otherwise.
     */
    public boolean isSunk(){
        for (Cell cell: cells){
            if (cell.getCellState() != CellState.SUNK){
                return false;
            }
        }
        return true;
    }
    /**
     * Validates that the number of assigned cells matches the ship's defined size.
     *
     * @param size      Expected ship size.
     * @param sizeCells Actual count of cells.
     * @return {@code true} if dimensions match; {@code false} otherwise.
     */
    private boolean validateSize(int size, int sizeCells){
        return sizeCells==size;
    }
}
