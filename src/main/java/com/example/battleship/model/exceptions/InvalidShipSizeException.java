package com.example.battleship.model.exceptions;

import com.example.battleship.model.Cell;

import java.util.ArrayList;
import java.util.List;

public class InvalidShipSizeException extends RuntimeException {
    private final int sizeShip;
    private final int sizeCells;
    public InvalidShipSizeException(String message, int sizeShip, int sizeCells){
        super(message);
        this.sizeShip=sizeShip;
        this.sizeCells=sizeCells;
    }
    public int getSizeShip(){return sizeShip;}
    public int getSizeCells(){return sizeCells;}
}
