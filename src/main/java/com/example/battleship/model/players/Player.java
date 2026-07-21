package com.example.battleship.model.players;

import com.example.battleship.model.Board;
import com.example.battleship.model.Coordinate;
import com.example.battleship.model.enums.CellState;
import com.example.battleship.model.enums.ShipType;
import java.io.Serializable;

public abstract class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    protected final Board board;
    public Player(){
        board= new Board();
    }
    public Board getBoard(){return board;}
    public abstract void placeFleet();
    public abstract Coordinate chooseShotTarget();
    public void registerShotResult(Coordinate coordinate, CellState result){

    }
}
