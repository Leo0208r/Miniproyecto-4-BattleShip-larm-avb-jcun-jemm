package com.example.battleship.model;

import com.example.battleship.model.enums.CellState;
import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.exceptions.RepeatedShotException;
import com.example.battleship.model.ships.Destroyer;
import com.example.battleship.model.ships.Frigate;
import com.example.battleship.model.ships.Ship;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    void shouldThrowWhenCoordinateIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Cell(null));
    }

    @Test
    void shouldStartEmptyWithoutShip() {
        Cell cell = new Cell(new Coordinate(0, 0));
        assertEquals(CellState.EMPTY, cell.getCellState());
        assertFalse(cell.hasShip());
        assertNull(cell.getShip());
    }

    @Test
    void shouldThrowWhenAssigningNullShip() {
        Cell cell = new Cell(new Coordinate(0, 0));
        assertThrows(IllegalArgumentException.class, () -> cell.assignShip(null));
    }

    @Test
    void shouldAssignShipAndBecomeOccupied() {
        Cell cell = new Cell(new Coordinate(0, 0));
        Ship ship = new Frigate(Orientation.HORIZONTAL, List.of(cell));

        cell.assignShip(ship);

        assertTrue(cell.hasShip());
        assertEquals(ship, cell.getShip());
        assertEquals(CellState.OCCUPIED, cell.getCellState());
    }

    @Test
    void shouldThrowWhenAssigningShipTwice() {
        Cell cell = new Cell(new Coordinate(0, 0));
        Ship ship = new Frigate(Orientation.HORIZONTAL, List.of(cell));
        cell.assignShip(ship);

        assertThrows(IllegalStateException.class, () -> cell.assignShip(ship));
    }

    @Test
    void shouldThrowWhenSettingNullState() {
        Cell cell = new Cell(new Coordinate(0, 0));
        assertThrows(IllegalArgumentException.class, () -> cell.setCellState(null));
    }

    @Test
    void shootingEmptyCellShouldTurnItToWater() throws RepeatedShotException {
        Cell cell = new Cell(new Coordinate(0, 0));

        CellState result = cell.receiveShot();

        assertEquals(CellState.WATER, result);
        assertEquals(CellState.WATER, cell.getCellState());
    }

    @Test
    void shootingSingleCellShipShouldSinkItImmediately() throws RepeatedShotException {
        Cell cell = new Cell(new Coordinate(0, 0));
        Ship frigate = new Frigate(Orientation.HORIZONTAL, List.of(cell));
        cell.assignShip(frigate);

        CellState result = cell.receiveShot();

        assertEquals(CellState.SUNK, result);
        assertTrue(frigate.isSunk());
    }

    @Test
    void shootingMultiCellShipShouldOnlyHitUntilAllCellsAreShot() throws RepeatedShotException {
        Cell cellA = new Cell(new Coordinate(0, 0));
        Cell cellB = new Cell(new Coordinate(0, 1));
        Ship destroyer = new Destroyer(Orientation.HORIZONTAL, List.of(cellA, cellB));
        cellA.assignShip(destroyer);
        cellB.assignShip(destroyer);

        CellState firstResult = cellA.receiveShot();
        assertEquals(CellState.HIT, firstResult);
        assertFalse(destroyer.isSunk());

        CellState secondResult = cellB.receiveShot();
        assertEquals(CellState.SUNK, secondResult);
        assertTrue(destroyer.isSunk());
        assertEquals(CellState.SUNK, cellA.getCellState());
    }

    @Test
    void shootingAlreadyShotWaterCellShouldThrow() throws RepeatedShotException {
        Cell cell = new Cell(new Coordinate(0, 0));
        cell.receiveShot();

        assertThrows(RepeatedShotException.class, cell::receiveShot);
    }

    @Test
    void shootingAlreadySunkCellShouldThrow() throws RepeatedShotException {
        Cell cell = new Cell(new Coordinate(0, 0));
        Ship frigate = new Frigate(Orientation.HORIZONTAL, List.of(cell));
        cell.assignShip(frigate);
        cell.receiveShot();

        assertThrows(RepeatedShotException.class, cell::receiveShot);
    }
}
