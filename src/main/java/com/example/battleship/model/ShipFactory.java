package com.example.battleship.model;

import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.enums.ShipType;
import com.example.battleship.model.ships.*;

import java.util.List;
/**
 * Factory class responsible for instantiating specific {@link Ship} implementations.
 * <p>
 * Encapsulates ship creation logic based on the provided {@link ShipType}, orientation,
 * and list of assigned grid cells.
 * </p>

 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class ShipFactory {

    /**
     * Creates and returns a concrete {@link Ship} instance corresponding to the specified type.
     *
     * @param type        The {@link ShipType} enumeration value indicating ship classification.
     * @param orientation Alignment direction (HORIZONTAL or VERTICAL) of the ship.
     * @param cells       List of {@link Cell} objects assigned to occupy the ship.
     * @return Concrete subclass instance of {@link Ship}.
     * @throws IllegalArgumentException If an unknown or unhandled ship type is provided.
     */
    public static Ship createShip(ShipType type, Orientation orientation, List<Cell> cells){
        switch (type){
            case AIRCRAFTCARRIER ->{return new AircraftCarrier(orientation, cells);}
            case FRIGATE ->{return new Frigate(orientation, cells);}
            case DESTROYER ->{ return new Destroyer(orientation, cells);}
            case SUBMARINE ->{return new Submarine(orientation, cells);}
            default ->throw new IllegalArgumentException("Unknown ship type: " + type);
        }
    }

}
