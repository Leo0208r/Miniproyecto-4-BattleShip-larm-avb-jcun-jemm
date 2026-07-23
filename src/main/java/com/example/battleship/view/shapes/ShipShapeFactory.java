package com.example.battleship.view.shapes;

import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.enums.ShipType;
import com.example.battleship.view.shapes.ships.AircraftCarrierShape;
import com.example.battleship.view.shapes.ships.DestroyerShape;
import com.example.battleship.view.shapes.ships.FrigateShape;
import com.example.battleship.view.shapes.ships.SubmarineShape;
import javafx.scene.Group;
/**
 * Factory class responsible for instantiating JavaFX {@link Group} vector graphics
 * corresponding to specific ship types and applying spatial orientation transformations.
 * <p>
 * Delegates actual shape creation to individual ship shape builder utility classes
 * and handles grid orientation alignment (horizontal vs. vertical).
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class ShipShapeFactory {

    /**
     * Creates and returns a transformed JavaFX {@link Group} representing the specified vessel.
     *
     * @param type        The {@link ShipType} enum identifying which vessel to render.
     * @param orientation The {@link Orientation} enum indicating horizontal or vertical grid alignment.
     * @param cellSize    The pixel dimension of a single board cell used to scale the shape.
     * @return {@link Group} containing all vector components of the constructed ship shape.
     */
    public static Group create(
            ShipType type,
            Orientation orientation,
            double cellSize
    ){

        Group ship = switch(type){

            case AIRCRAFTCARRIER ->
                    AircraftCarrierShape.create(cellSize);

            case SUBMARINE ->
                    SubmarineShape.create(cellSize);

            case DESTROYER ->
                    DestroyerShape.create(cellSize);

            case FRIGATE ->
                    FrigateShape.create(cellSize);
        };


        if(orientation == Orientation.VERTICAL){
            ship.setRotate(90);
        }


        return ship;
    }
}