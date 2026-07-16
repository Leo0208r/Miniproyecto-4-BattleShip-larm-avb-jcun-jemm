package com.example.battleship.model;

import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.enums.ShipType;
import com.example.battleship.model.ships.*;

import java.util.List;

public class ShipFactory {


    public static Ship createShip(ShipType type, Orientation orientation, List<Cell> cells){
        switch (type){
            case AIRCRAFTCARRIER ->{return new AircraftCarrier(orientation, cells);}
            case FRIGATE ->{return new Frigate(orientation, cells);}
            case DESTROYER ->{ return new Destroyer(orientation, cells);}
            case SUBMARINE ->{return new Submarine(orientation, cells);}
            default ->throw new IllegalArgumentException("Unknown ship type: " + type);
        }
    }

}
