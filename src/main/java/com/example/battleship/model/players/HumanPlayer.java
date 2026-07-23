package com.example.battleship.model.players;

import com.example.battleship.model.Board;
import com.example.battleship.model.Coordinate;
import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.enums.ShipType;
import com.example.battleship.model.exceptions.InvalidCoordinateException;
import com.example.battleship.model.exceptions.ShipOverLapException;
import com.example.battleship.model.Fleet;
/**
 * Represents the human player in the Battleship game.
 * <p>
 * Provides functionality for manually placing ships onto the player's board,
 * checking fleet completion, and automatically filling remaining fleet positions
 * using a random placement helper algorithm.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class HumanPlayer extends Player {
    /**
     * Places a specific ship onto the human player's board at the specified coordinate and orientation.
     *
     * @param coordinate  The starting coordinate for placing the ship.
     * @param orientation The orientation (HORIZONTAL or VERTICAL) of the ship.
     * @param shipType    The type of ship to place.
     * @throws ShipOverLapException If the placement overlaps with an existing ship.
     */
    public void placeShip(Coordinate coordinate, Orientation orientation, ShipType shipType) throws ShipOverLapException {
        board.placeShip(coordinate, orientation, shipType);
    }
    /**
     * Gets the total count of ships currently placed on the player's board.
     *
     * @return Total number of placed ships.
     */
    public int getPlacedShipsCount() {
        return board.getFleet().getShips().size();
    }
    /**
     * Checks whether the human player has placed the complete standard fleet.
     *
     * @return {@code true} if all standard ships have been placed; {@code false} otherwise.
     */
    public boolean isFleetComplete() {
        return getPlacedShipsCount() >= Fleet.getStandardComposition().size();
    }
    /**
     * Automatically places all standard fleet ships onto the player's board in random valid positions.
     * <p>
     * Used primarily by the UI as an auto-fill helper feature.
     * </p>
     */
    @Override
    public void placeFleet() {
        // Helper used by the UI as an optional auto-fill action.
        java.util.Random random = new java.util.Random();
        java.util.List<ShipType> composition = Fleet.getStandardComposition();
        java.util.List<Orientation> orientations = java.util.List.of(Orientation.HORIZONTAL, Orientation.VERTICAL);
        for (ShipType type : composition) {
            boolean placed = false;
            while (!placed) {
                int row = random.nextInt(10);
                int col = random.nextInt(10);
                int orientationRandom = random.nextInt(2);
                try {
                    Coordinate coordinate = new Coordinate(row, col);
                    this.board.placeShip(coordinate, orientations.get(orientationRandom), type);
                    placed = true;
                } catch (ShipOverLapException | InvalidCoordinateException e) {
                    // try again
                }
            }
        }
    }
    /**
     * Chooses a target coordinate for firing a shot.
     * <p>
     * For human players, target selection is driven interactively through the GUI,
     * so this method returns {@code null}.
     * </p>
     * @return Always {@code null} for human players.
     */
    @Override
    public Coordinate chooseShotTarget() {
        return null;
    }
}
