package com.example.battleship.model.players;

import com.example.battleship.model.Board;
import com.example.battleship.model.Coordinate;
import com.example.battleship.model.Fleet;
import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.enums.ShipType;
import com.example.battleship.model.exceptions.InvalidCoordinateException;
import com.example.battleship.model.exceptions.ShipOverLapException;

import java.util.List;
import java.util.Random;

public class MachinePlayer extends Player{
    private Random random=new Random();
    @Override
    public void placeFleet() {
        List<ShipType> composition= Fleet.getStandardComposition();
        List<Orientation> orientations=List.of(Orientation.HORIZONTAL, Orientation.VERTICAL);
        for(ShipType type: composition){
            boolean placed=false;
            while (!placed){
                int row= random.nextInt(10);
                int col= random.nextInt(10);
                int orientationRandom= random.nextInt(2);

                try{
                    Coordinate coordinate=new Coordinate(row,col);
                    board.placeShip(coordinate,orientations.get(orientationRandom),type);
                    placed=true;
                }catch (ShipOverLapException | InvalidCoordinateException e){

                }
            }
        }
    }

    @Override
    public Coordinate chooseShotTarget(Board opponentBoard) {
        return null;
    }
}
