package com.example.battleship.model.ships;

import com.example.battleship.model.Cell;
import com.example.battleship.model.enums.Orientation;

import java.util.List;

public class Destroyer extends Ship {
    public static final int SIZE = 2;

    public Destroyer(Orientation orientation, List<Cell> cells) {
        super(SIZE, orientation, cells);
    }
}
