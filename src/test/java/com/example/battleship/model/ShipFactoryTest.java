package com.example.battleship.model;

import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.enums.ShipType;
import com.example.battleship.model.exceptions.InvalidShipSizeException;
import com.example.battleship.model.ships.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipFactoryTest {

    private List<Cell> cellsFor(int size) {
        List<Cell> cells = new ArrayList<>();
        for (int col = 0; col < size; col++) {
            cells.add(new Cell(new Coordinate(0, col)));
        }
        return cells;
    }

    @Test
    void shouldCreateAircraftCarrierOfCorrectSize() {
        Ship ship = ShipFactory.createShip(ShipType.AIRCRAFTCARRIER, Orientation.HORIZONTAL, cellsFor(4));

        assertInstanceOf(AircraftCarrier.class, ship);
        assertEquals(4, ship.getSize());
    }

    @Test
    void shouldCreateSubmarineOfCorrectSize() {
        Ship ship = ShipFactory.createShip(ShipType.SUBMARINE, Orientation.HORIZONTAL, cellsFor(3));

        assertInstanceOf(Submarine.class, ship);
        assertEquals(3, ship.getSize());
    }

    @Test
    void shouldCreateDestroyerOfCorrectSize() {
        Ship ship = ShipFactory.createShip(ShipType.DESTROYER, Orientation.HORIZONTAL, cellsFor(2));

        assertInstanceOf(Destroyer.class, ship);
        assertEquals(2, ship.getSize());
    }

    @Test
    void shouldCreateFrigateOfCorrectSize() {
        Ship ship = ShipFactory.createShip(ShipType.FRIGATE, Orientation.HORIZONTAL, cellsFor(1));

        assertInstanceOf(Frigate.class, ship);
        assertEquals(1, ship.getSize());
    }

    @Test
    void shouldThrowWhenCellCountDoesNotMatchRequestedType() {
        assertThrows(InvalidShipSizeException.class,
                () -> ShipFactory.createShip(ShipType.FRIGATE, Orientation.HORIZONTAL, cellsFor(2)));
    }
}
