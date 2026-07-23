package com.example.battleship.model;

import com.example.battleship.model.enums.CellState;
import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.ships.Ship;
import com.example.battleship.model.enums.ShipType;
import com.example.battleship.model.exceptions.InvalidCoordinateException;
import com.example.battleship.model.exceptions.RepeatedShotException;
import com.example.battleship.model.exceptions.ShipOverLapException;
import javafx.scene.input.ScrollEvent;

import java.io.Serializable;
import java.util.*;

import static com.example.battleship.model.enums.Orientation.HORIZONTAL;
import static com.example.battleship.model.enums.Orientation.VERTICAL;
/**
 * Represents the 10x10 Battleship game board.
 * <p>
 * Manages grid coordinates, cell states, ship placement, target shooting execution,
 * and board event notification listeners. Implements {@link Serializable} to support game persistence.
 * </p>

 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class Board implements Serializable {
    /** Serial version UID for serialization compatibility. */
    private static final long serialVersionUID = 1L;
    /** Map holding all grid coordinates and their corresponding cell states. */
    private final Map<Coordinate,Cell> board;
    /** Fleet of ships assigned to this board. */
    private final Fleet fleet;
    /** Transient list of listeners observing state changes on this board. */
    private transient List<BoardListener> listeners = new ArrayList<>();
    /**
     * Constructs a new 10x10 {@code Board} and initializes all grid cells and fleet manager.
     */
    public Board() {
        board=new HashMap<>();
        fleet= new Fleet();
        fillBoard();
    }
    /**
     * Initializes the 10x10 grid by populating coordinates and fresh cell instances.
     */
    private void fillBoard() {
        for (int row=0;row<=9;row++){
            for (int col=0; col<=9; col++){
                Coordinate coordinate= new Coordinate(row,col);
                board.put(coordinate, new Cell(coordinate));
            }
        }
    }
    /**
     * Gets an unmodifiable map of all board coordinates and cells.
     *
     * @return Unmodifiable {@link Map} of {@link Coordinate} to {@link Cell}.
     */
    public Map<Coordinate,Cell> getBoard(){
        return Collections.unmodifiableMap(board);
    }
    /**
     * Gets the fleet attached to this board.
     *
     * @return The board's {@link Fleet}.
     */
    public Fleet getFleet(){
        return fleet;
    }
    /**
     * Registers a listener to monitor cell updates on this board.
     *
     * @param listener The {@link BoardListener} to add.
     */
    public void addListener(BoardListener listener){
        if (listener==null) return;
        if (listeners==null) listeners=new ArrayList<>();
        listeners.add(listener);
    }
    /**
     * Removes a registered listener from this board.
     *
     * @param listener The {@link BoardListener} to remove.
     */
    public void removeListener(BoardListener listener){
        if (listeners!=null) listeners.remove(listener);
    }
    /**
     * Restores the transient listeners list after deserialization.
     */
    public void restoreTransientState() {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
    }
    /**
     * Places a ship onto the board starting at a specific coordinate and orientation.
     *
     * @param coordinate  Starting coordinate for the ship placement.
     * @param orientation Alignment direction (HORIZONTAL or VERTICAL).
     * @param shipType    Type of ship to construct and place.
     * @throws ShipOverLapException If any target cell already contains a ship.
     */
    public void placeShip(Coordinate coordinate, Orientation orientation, ShipType shipType)throws ShipOverLapException{
        List<Coordinate> coordinatesShip=calculateShipCoordinates(coordinate, orientation, shipType);
        if (validateCells(coordinatesShip)){
            List<Cell> cells=new ArrayList<>();
            for (Coordinate coordinates: coordinatesShip){
                cells.add(getCell(coordinates));
            }
            Ship ship = ShipFactory.createShip(shipType,orientation,cells);
            fleet.addShip(ship);
            for (Cell cell: cells){
                cell.assignShip(ship);
                // notify listeners about ship placement per cell
                if (listeners!=null){
                    for (BoardListener l: listeners){
                        try{ l.onCellUpdated(cell.getCoordinate(), cell.getCellState()); }catch(Exception ignore){}
                    }
                }
            }
        }
    }
    /**
     * Validates whether all specified coordinates are free of existing ships.
     *
     * @param coordinatesList List of coordinates to evaluate.
     * @return {@code true} if all cells are available.
     * @throws ShipOverLapException If a cell is already occupied.
     */
    private boolean validateCells(List<Coordinate>coordinatesList)throws ShipOverLapException{
        for (Coordinate coordinate: coordinatesList){
            if(getCell(coordinate).hasShip()){
                throw new ShipOverLapException("Cell is already occupied by another ship");
            }
        }
        return true;
    }
    /**
     * Calculates the series of coordinates covered by a ship given its origin, orientation, and size.
     *
     * @param start       Starting coordinate.
     * @param orientation Alignment direction.
     * @param type        Type of ship determining its length.
     * @return List of occupied {@link Coordinate} instances.
     * @throws IllegalArgumentException If orientation is invalid.
     */
    private List<Coordinate> calculateShipCoordinates(Coordinate start, Orientation orientation, ShipType type){
        List<Coordinate> coordinates= new ArrayList<>();
        if (orientation==HORIZONTAL){
            int startRow=start.getRow();
            int endCol=start.getCol();
            for (int startCol=start.getCol(); startCol<endCol+type.getSize(); startCol++){
                coordinates.add(new Coordinate(startRow,startCol));
            }
        }
        else if (orientation==VERTICAL){
            int startCol=start.getCol();
            int endRow=start.getRow();
            for (int startRow=start.getRow(); startRow<endRow+type.getSize(); startRow++){
                coordinates.add(new Coordinate(startRow,startCol));
            }
        }else{
            throw new IllegalArgumentException("it is not vertical and horizontal");
        }
        return coordinates;
    }
    /**
     * Retrieves the cell object at a given coordinate.
     *
     * @param coordinate Target coordinate.
     * @return {@link Cell} corresponding to the coordinate.
     * @throws IllegalArgumentException If coordinate is {@code null}.
     */
    public Cell getCell(Coordinate coordinate){
        if (coordinate == null) {
            throw new IllegalArgumentException("Coordinate cannot be null");
        }
        return board.get(coordinate);
    }
    /**
     * Executes a shot at a target coordinate and notifies listeners of state updates.
     *
     * @param coordinate Target coordinate to shoot at.
     * @return The resulting {@link CellState} (WATER, HIT, or SUNK).
     * @throws RepeatedShotException If the coordinate has already been targeted.
     */
    public CellState shoot(Coordinate coordinate)throws RepeatedShotException {
        CellState result = getCell(coordinate).receiveShot();
        if (listeners!=null){
            for (BoardListener l: listeners){
                try{ l.onCellUpdated(coordinate, result);}catch(Exception ignore){}
            }
        }
        return result;
    }
    /**
     * Checks if all ships in the board's fleet have been completely sunk.
     *
     * @return {@code true} if fleet is defeated; {@code false} otherwise.
     */
    public boolean isFleetDefeated(){
        return fleet.isFleetSunk();
    }
}
