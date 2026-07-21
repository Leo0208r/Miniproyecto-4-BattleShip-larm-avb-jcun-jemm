package com.example.battleship.model;

import com.example.battleship.model.enums.CellState;

import java.io.Serializable;

public interface IShootingStrategy extends Serializable {
    Coordinate selectTarget();
    void registerResult(Coordinate coordinate, CellState result);
    void reset();
}
