package com.example.battleship.model.ships;

import com.example.battleship.model.Cell;
import com.example.battleship.model.enums.Orientation;

import java.util.List;
/**
 * Represents an Aircraft Carrier ship in Battleship.
 * <p>
 * The Aircraft Carrier is the largest ship class in the standard fleet, occupying 4 grid cells.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class AircraftCarrier extends Ship {
    /** Standard size of an Aircraft Carrier in grid cells. */
    public static final int SIZE = 4;
    /**
     * Constructs a new {@code AircraftCarrier} with the specified orientation and grid cells.
     *
     * @param orientation The layout orientation (HORIZONTAL or VERTICAL) of the ship.
     * @param cells       The list of grid {@link Cell}s occupied by the ship.
     */
    public AircraftCarrier(Orientation orientation, List<Cell> cells) {
        super(SIZE, orientation, cells);
    }
}
