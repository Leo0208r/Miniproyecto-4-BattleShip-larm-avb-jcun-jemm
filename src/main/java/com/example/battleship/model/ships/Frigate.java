package com.example.battleship.model.ships;

import com.example.battleship.model.Cell;
import com.example.battleship.model.enums.Orientation;

import java.util.List;
/**
 * Represents a Frigate ship in Battleship.
 * <p>
 * The Frigate is the smallest ship class in the standard fleet, occupying a single grid cell.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class Frigate extends Ship {
    /** Standard size of a Frigate in grid cells. */
    public static final int SIZE = 1;
    /**
     * Constructs a new {@code Frigate} with the specified orientation and grid cells.
     *
     * @param orientation The layout orientation (HORIZONTAL or VERTICAL) of the ship.
     * @param cells       The list containing the single grid {@link Cell} occupied by the ship.
     */
    public Frigate( Orientation orientation, List<Cell> cells) {
        super(SIZE, orientation, cells);
    }
}
