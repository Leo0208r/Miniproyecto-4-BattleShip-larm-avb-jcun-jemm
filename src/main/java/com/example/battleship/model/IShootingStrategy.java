package com.example.battleship.model;

import com.example.battleship.model.enums.CellState;

import java.io.Serializable;
/**
 * Strategy interface defining targeting algorithms for AI players in Battleship.
 * <p>
 * Outlines procedures for target coordinate selection, shot feedback tracking,
 * and state resets. Implements {@link Serializable} to support AI strategy state persistence across saves.
 * </p>

 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public interface IShootingStrategy extends Serializable {
    /**
     * Determines and returns the next target coordinate to attack.
     *
     * @return Target {@link Coordinate} for the upcoming shot.
     */
    Coordinate selectTarget();
    /**
     * Registers the outcome of a fired shot to update the internal targeting state or strategy.
     *
     * @param coordinate The target coordinate that was attacked.
     * @param result     The resulting {@link CellState} (e.g., WATER, HIT, SUNK).
     */
    void registerResult(Coordinate coordinate, CellState result);
    /**
     * Resets the strategy state, clearing shot history and targeted memory.
     */
    void reset();
}
