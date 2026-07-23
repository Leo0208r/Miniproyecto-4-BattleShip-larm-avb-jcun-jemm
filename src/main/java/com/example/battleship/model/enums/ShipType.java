package com.example.battleship.model.enums;
/**
 * Defines the specific types of ships available in the Battleship game.
 * <p>
 * Each ship type has an associated size representing the number of grid cells it occupies.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public enum ShipType {
    /** Aircraft Carrier, which occupies 4 grid cells. */
    AIRCRAFTCARRIER(4),
    /** Destroyer, which occupies 2 grid cells. */
    DESTROYER(2),
    /** Frigate, which occupies 1 grid cell. */
    FRIGATE(1),
    /** Submarine, which occupies 3 grid cells. */
    SUBMARINE(3);
    /** The length of the ship in grid cells. */
    private final int size;
    /**
     * Constructs a {@code ShipType} with the specified cell size.
     *
     * @param size Number of grid cells occupied by the ship type.
     */
    ShipType(int size){
        this.size=size;
    }
    /**
     * Gets the length of the ship in grid cells.
     *
     * @return Size of the ship.
     */
    public int getSize(){return size;}
}
