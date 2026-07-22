package com.example.battleship.view.shapes;

import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.enums.ShipType;
import com.example.battleship.view.shapes.ships.AircraftCarrierShape;
import com.example.battleship.view.shapes.ships.DestroyerShape;
import com.example.battleship.view.shapes.ships.FrigateShape;
import com.example.battleship.view.shapes.ships.SubmarineShape;
import javafx.scene.Group;

public class ShipShapeFactory {


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