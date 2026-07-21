package com.example.battleship.model;

import com.example.battleship.model.enums.CellState;
import com.example.battleship.model.exceptions.RepeatedShotException;
import com.example.battleship.model.ships.Ship;
import java.io.Serializable;

public class Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    private CellState cellState;
    private final Coordinate coordinate;
    private Ship ship;
    public Cell(Coordinate coordinate){
        if (coordinate==null){
            throw new IllegalArgumentException("Coordinate cannot be null");
        }
        this.coordinate=coordinate;
        cellState=CellState.EMPTY;
        ship=null;
    }
    public CellState getCellState(){return cellState;}
    public void setCellState(CellState cellState){
        if (cellState == null) {
            throw new IllegalArgumentException("State cannot be null");
        }
        this.cellState=cellState;}
    public Coordinate getCoordinate(){return coordinate;}
    public Ship getShip(){return ship;}
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
    public boolean hasShip(){
        return ship!=null;
    }
    public CellState receiveShot()throws RepeatedShotException{
        if (cellState==CellState.EMPTY){
            setCellState(CellState.WATER);
            return cellState;
        }
        else if (cellState==CellState.OCCUPIED){
            setCellState(CellState.HIT);
            if(ship.registerHit()){
               return cellState;
            }
        }else{
            throw new RepeatedShotException("Cell already shot, current state: "+cellState.getSymbol(), cellState );
        }
        return cellState;
    }
    @Override
    public String toString(){
        return "Cell"+coordinate.toString()+"[State="+cellState+"]";
    }
}
