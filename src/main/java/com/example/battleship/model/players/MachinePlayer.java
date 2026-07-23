package com.example.battleship.model.players;

import com.example.battleship.model.*;
import com.example.battleship.model.enums.CellState;
import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.enums.ShipType;
import com.example.battleship.model.exceptions.InvalidCoordinateException;
import com.example.battleship.model.exceptions.ShipOverLapException;

import java.util.List;
import java.util.Random;
/**
 * Represents the AI (machine) player in the Battleship game.
 * <p>
 * Handles automated, collision-free random fleet placement and employs an intelligent
 * {@link IShootingStrategy} (Hunt/Target) to select attack coordinates and process shot results.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class MachinePlayer extends Player{
    /** Random number generator used for automated fleet setup. */
    private final Random random=new Random();
    /** AI shooting strategy responsible for target selection. */
    private final IShootingStrategy shootingStrategy;
    /**
     * Constructs a new {@code MachinePlayer} initialized with a {@link HuntTargetStrategy}.
     */
    public MachinePlayer(){
        this.shootingStrategy=new HuntTargetStrategy();
    }
    /**
     * Automatically places the complete standard fleet on the AI's board in random valid locations.
     */
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
    /**
     * Selects the next coordinate to attack based on the active shooting strategy.
     *
     * @return Target {@link Coordinate} chosen by the AI.
     */
    @Override
    public Coordinate chooseShotTarget() {
        return shootingStrategy.selectTarget();
    }
    /**
     * Registers the outcome of a fired shot to update the AI's shooting strategy state.
     *
     * @param coordinate The target coordinate that was attacked.
     * @param result     The resulting state of the target cell (WATER, HIT, SUNK).
     */
    @Override
    public void registerShotResult(Coordinate coordinate, CellState result){
        shootingStrategy.registerResult(coordinate, result);
    }
}
