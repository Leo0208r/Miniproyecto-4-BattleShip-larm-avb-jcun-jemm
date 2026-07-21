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

public class Board implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<Coordinate,Cell> board;
    private final Fleet fleet;
    private transient List<BoardListener> listeners = new ArrayList<>();
    public Board() {
        board=new HashMap<>();
        fleet= new Fleet();
        fillBoard();
    }

    private void fillBoard() {
        for (int row=0;row<=9;row++){
            for (int col=0; col<=9; col++){
                Coordinate coordinate= new Coordinate(row,col);
                board.put(coordinate, new Cell(coordinate));
            }
        }
    }

    public Map<Coordinate,Cell> getBoard(){
        return Collections.unmodifiableMap(board);
    }

    public Fleet getFleet(){
        return fleet;
    }

    public void addListener(BoardListener listener){
        if (listener==null) return;
        if (listeners==null) listeners=new ArrayList<>();
        listeners.add(listener);
    }

    public void removeListener(BoardListener listener){
        if (listeners!=null) listeners.remove(listener);
    }

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

    private boolean validateCells(List<Coordinate>coordinatesList)throws ShipOverLapException{
        for (Coordinate coordinate: coordinatesList){
            if(getCell(coordinate).hasShip()){
                throw new ShipOverLapException("Cell is already occupied by another ship");
            }
        }
        return true;
    }

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

    public Cell getCell(Coordinate coordinate){
        if (coordinate == null) {
            throw new IllegalArgumentException("Coordinate cannot be null");
        }
        return board.get(coordinate);
    }

    public CellState shoot(Coordinate coordinate)throws RepeatedShotException {
        CellState result = getCell(coordinate).receiveShot();
        if (listeners!=null){
            for (BoardListener l: listeners){
                try{ l.onCellUpdated(coordinate, result);}catch(Exception ignore){}
            }
        }
        return result;
    }

    public boolean isFleetDefeated(){
        return fleet.isFleetSunk();
    }
}
