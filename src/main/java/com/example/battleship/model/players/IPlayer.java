package com.example.battleship.model.players;

import com.example.battleship.model.Board;
import com.example.battleship.model.Coordinate;
import com.example.battleship.model.enums.CellState;
/**
 * Defines the core operations and behaviors required for a Battleship player.
 * <p>
 * Outlines methods for board management, fleet placement, target selection,
 * and registering shot outcomes across different player implementations (e.g., Human vs. AI).
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public interface IPlayer {
    /**
     * Gets the game board associated with this player.
     *
     * @return The player's {@link Board} instance.
     */
    Board getBoard();
    /**
     * Places the complete fleet of ships onto the player's board.
     */
    void placeFleet();
    /**
     * Determines and selects the next target coordinate for an attack action.
     *
     * @return Target {@link Coordinate} for the shot.
     */
    Coordinate chooseShotTarget();
    /**
     * Registers the outcome of a fired shot to update the player's internal state or strategy.
     *
     * @param coordinate The target coordinate that was attacked.
     * @param result     The resulting state of the target cell (e.g., WATER, HIT, SUNK).
     */
    void registerShotResult(Coordinate coordinate, CellState result);
}

