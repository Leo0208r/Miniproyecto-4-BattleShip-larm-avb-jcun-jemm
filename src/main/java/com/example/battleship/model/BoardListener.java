package com.example.battleship.model;

import com.example.battleship.model.enums.CellState;

/**
 * Listener interface for board state changes.
 * <p>
 * Implementations (such as UI components) register with a {@link Board} instance
 * to receive real-time notifications when individual grid cell states are updated
 * during ship placement or combat actions.
 * </p>

 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public interface BoardListener {
    /**
     * Invoked whenever a cell on the observed board changes its state.
     *
     * @param coordinate The coordinate of the updated cell.
     * @param newState   The new {@link CellState} of the updated cell.
     */
    void onCellUpdated(Coordinate coordinate, CellState newState);
}

