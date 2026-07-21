package com.example.battleship.model.ships;

import com.example.battleship.model.Cell;
import com.example.battleship.model.enums.CellState;
import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.exceptions.InvalidShipSizeException;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public abstract class Ship implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int size;
    private final Orientation orientation;
    private final List<Cell> cells;
    public Ship(int size, Orientation orientation, List<Cell> cells){
        this.size=size;
        this.orientation=orientation;
        if(validateSize(size,cells.size())){
            this.cells=cells;
        }else{
            throw new InvalidShipSizeException("The size of the ship does not match the size on the list",size,cells.size());
        }
    }
    public int getSize(){return size;}
    public Orientation getOrientation(){return orientation;}
    public List<Cell> getCells(){return Collections.unmodifiableList(cells);}
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
    public boolean isSunk(){
        for (Cell cell: cells){
            if (cell.getCellState() != CellState.SUNK){
                return false;
            }
        }
        return true;
    }
    private boolean validateSize(int size, int sizeCells){
        return sizeCells==size;
    }
}
