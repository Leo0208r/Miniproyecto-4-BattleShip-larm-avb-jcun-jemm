package com.example.battleship.model.ships;

import com.example.battleship.model.Cell;
import com.example.battleship.model.enums.Orientation;

import java.util.List;
/**
 * Represents a Destroyer ship in Battleship.
 * <p>
 * The Destroyer is a medium-small ship class occupying 2 grid cells.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class Destroyer extends Ship {
    /** Standard size of a Destroyer in grid cells. */
    public static final int SIZE = 2;
    /**
     * Constructs a new {@code Destroyer} with the specified orientation and grid cells.
     *
     * @param orientation The layout orientation (HORIZONTAL or VERTICAL) of the ship.
     * @param cells       The list of grid {@link Cell}s occupied by the ship.
     */
    public Destroyer(Orientation orientation, List<Cell> cells) {
        super(SIZE, orientation, cells);
    }
}
