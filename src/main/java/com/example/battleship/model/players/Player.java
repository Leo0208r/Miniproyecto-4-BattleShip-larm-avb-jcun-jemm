package com.example.battleship.model.players;

import com.example.battleship.model.Board;
import com.example.battleship.model.Coordinate;
import com.example.battleship.model.enums.CellState;
import com.example.battleship.model.enums.ShipType;
import java.io.Serializable;
/**
 * Abstract base class representing a player in the Battleship game.
 * <p>
 * Holds the player's primary {@link Board} instance and defines abstract and default methods
 * for fleet management, shot targeting, and registering attack outcomes.
 * Implements {@link Serializable} to support match persistence.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public abstract class Player implements Serializable {
    /** Serial version UID for serialization compatibility. */
    private static final long serialVersionUID = 1L;
    /** The game board instance associated with this player. */
    protected final Board board;
    /**
     * Constructs a new {@code Player} instance and initializes their game board.
     */
    public Player(){
        board= new Board();
    }
    /**
     * Gets the game board belonging to this player.
     *
     * @return The player's {@link Board} instance.
     */
    public Board getBoard(){return board;}
    /**
     * Abstract method to place the complete fleet of ships onto the board.
     * Implemented differently for human and machine players.
     */
    public abstract void placeFleet();
    /**
     * Abstract method to choose the next target coordinate for an attack.
     *
     * @return Target {@link Coordinate} for the shot.
     */
    public abstract Coordinate chooseShotTarget();
    /**
     * Registers the result of a shot to update internal state or strategy.
     * Default implementation does nothing; overridden by sub-classes as needed.
     *
     * @param coordinate The target coordinate that was attacked.
     * @param result     The resulting state of the target cell (e.g., WATER, HIT, SUNK).
     */
    public void registerShotResult(Coordinate coordinate, CellState result){

    }
}
