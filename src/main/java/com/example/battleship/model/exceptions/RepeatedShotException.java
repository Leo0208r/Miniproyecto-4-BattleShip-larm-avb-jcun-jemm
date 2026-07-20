package com.example.battleship.model.exceptions;

import com.example.battleship.model.enums.CellState;

public class RepeatedShotException extends Exception {
    private final CellState cellState;
    public RepeatedShotException(String message, CellState cellState) {
        super(message);
        this.cellState=cellState;
    }
    public CellState getCellState(){return cellState;}
}
