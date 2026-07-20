package com.example.battleship.model.ships;

import com.example.battleship.model.Cell;
import com.example.battleship.model.enums.Orientation;

import java.util.List;

public class AircraftCarrier extends Ship {
    public static final int SIZE = 4;

    public AircraftCarrier(Orientation orientation, List<Cell> cells) {
        super(SIZE, orientation, cells);
    }
}
