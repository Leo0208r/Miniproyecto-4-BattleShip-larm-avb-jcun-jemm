package com.example.battleship.model.enums;

public enum ShipType {
    AIRCRAFTCARRIER(4),
    DESTROYER(2),
    FRIGATE(1),
    SUBMARINE(3);
    private final int size;
    ShipType(int size){
        this.size=size;
    }
    public int getSize(){return size;}
}
