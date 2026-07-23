package com.example.battleship.model.ships;

import com.example.battleship.model.Cell;
import com.example.battleship.model.enums.Orientation;

import java.util.List;
/**
 * Represents a Submarine ship in Battleship.
 * <p>
 * The Submarine is a medium-sized ship class occupying 3 grid cells.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class Submarine extends Ship {
    /** Standard size of a Submarine in grid cells. */
    public static final int SIZE = 3;
    /**
     * Constructs a new {@code Submarine} with the specified orientation and grid cells.
     *
     * @param orientation The layout orientation (HORIZONTAL or VERTICAL) of the ship.
     * @param cells       The list of grid {@link Cell}s occupied by the ship.
     */
    public Submarine(Orientation orientation, List<Cell> cells) {
        super(SIZE, orientation, cells);
    }
}
