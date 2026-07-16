package com.example.battleship.model;

import com.example.battleship.model.enums.CellState;

public class Cell {
    private CellState cellState;
    private Coordinate coordinate;
    public Cell(Coordinate coordinate){
        this.coordinate=coordinate;
        cellState=CellState.EMPTY;
    }
    public CellState getCellState(){return cellState;}
    public void setCellState(CellState cellState){this.cellState=cellState;}
    public Coordinate getCoordinate(){return coordinate;}

}
