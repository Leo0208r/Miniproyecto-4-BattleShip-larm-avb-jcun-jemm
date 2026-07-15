package com.example.battleship.model.enums;

public enum Orientation {
    HORIZONTAL("Horizontal"),
    VERTICAL("Vertical");
    private String symbol;
    Orientation(String symbol){
        this.symbol=symbol;
    }
    public String getSymbol(){
        return symbol;
    }
}
