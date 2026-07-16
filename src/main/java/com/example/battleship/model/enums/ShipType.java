package com.example.battleship.model.enums;

public enum ShipType {
    AIRCRAFT_CARRIER("AircraftCarrier"),
    DESTROYER("Destroyer"),
    FRIGATE("Frigate"),
    SUBMARINE("Submarine");
    private String symbol;
    ShipType(String symbol){
        this.symbol=symbol;
    }
    public String getSymbol(){return symbol;}
}
