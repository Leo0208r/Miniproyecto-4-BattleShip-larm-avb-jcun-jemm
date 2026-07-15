package com.example.battleship.model;

public enum CellState {
    EMPTY("Empty"),
    WATER("Water"),
    OCCUPIED("Occupied"),
    HIT("Hit"),
    SUNK("Sunk");
    private String symbol;
    CellState(String symbol){
        this.symbol=symbol;
    }
    public String getSymbol(){
        return symbol;
    }
}
