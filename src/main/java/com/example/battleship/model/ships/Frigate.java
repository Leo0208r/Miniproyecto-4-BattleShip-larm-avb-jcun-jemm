package com.example.battleship.model.ships;

import com.example.battleship.model.Cell;
import com.example.battleship.model.enums.Orientation;

import java.util.List;

public class Frigate extends Ship {
    public static final int SIZE = 1;

    public Frigate( Orientation orientation, List<Cell> cells) {
        super(SIZE, orientation, cells);
    }
}
