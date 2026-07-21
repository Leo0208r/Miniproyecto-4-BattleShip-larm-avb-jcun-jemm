package com.example.battleship.model.players;

import com.example.battleship.model.*;
import com.example.battleship.model.enums.CellState;
import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.enums.ShipType;
import com.example.battleship.model.exceptions.InvalidCoordinateException;
import com.example.battleship.model.exceptions.ShipOverLapException;

import java.util.List;
import java.util.Random;

public class MachinePlayer extends Player{
    private final Random random=new Random();
    private final IShootingStrategy shootingStrategy;
    public MachinePlayer(){
        this.shootingStrategy=new HuntTargetStrategy();
    }
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
    public Coordinate chooseShotTarget() {
        return shootingStrategy.selectTarget();
    }
    @Override
    public void registerShotResult(Coordinate coordinate, CellState result){
        shootingStrategy.registerResult(coordinate, result);
    }
}
