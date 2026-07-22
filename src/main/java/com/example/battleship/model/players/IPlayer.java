package com.example.battleship.model.players;

import com.example.battleship.model.Board;
import com.example.battleship.model.Coordinate;
import com.example.battleship.model.enums.CellState;

public interface IPlayer {
    Board getBoard();
    void placeFleet();
    Coordinate chooseShotTarget();
    void registerShotResult(Coordinate coordinate, CellState result);
}

