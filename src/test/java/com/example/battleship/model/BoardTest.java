package com.example.battleship.model;

import com.example.battleship.model.enums.CellState;
import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.enums.ShipType;
import com.example.battleship.model.exceptions.InvalidCoordinateException;
import com.example.battleship.model.exceptions.RepeatedShotException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void shouldCreateBoardWithOneHundredEmptyCells() {
        Board board = new Board();

        assertEquals(100, board.getBoard().size());
        for (Cell cell : board.getBoard().values()) {
            assertEquals(CellState.EMPTY, cell.getCellState());
        }
    }

    @Test
    void getBoardShouldReturnAnUnmodifiableMap() {
        Board board = new Board();
        Coordinate coordinate = new Coordinate(0, 0);

        assertThrows(UnsupportedOperationException.class,
                () -> board.getBoard().put(coordinate, new Cell(coordinate)));
    }

    @Test
    void getCellShouldThrowForNullCoordinate() {
        Board board = new Board();

        assertThrows(IllegalArgumentException.class, () -> board.getCell(null));
    }

    @Test
    void getCellShouldReturnTheRequestedCell() {
        Board board = new Board();
        Coordinate coordinate = new Coordinate(3, 4);

        Cell cell = board.getCell(coordinate);

        assertEquals(coordinate, cell.getCoordinate());
    }

    @Test
    void shootingAnUnshotCellShouldReturnWater() throws RepeatedShotException {
        Board board = new Board();
        Coordinate coordinate = new Coordinate(0, 0);

        CellState result = board.shoot(coordinate);

        assertEquals(CellState.WATER, result);
    }

    @Test
    void shootingTheSameCellTwiceShouldThrow() throws RepeatedShotException {
        Board board = new Board();
        Coordinate coordinate = new Coordinate(0, 0);
        board.shoot(coordinate);

        assertThrows(RepeatedShotException.class, () -> board.shoot(coordinate));
    }

    @Test
    void placingShipOutOfBoundsShouldThrow() {
        Board board = new Board();

        // Column 8 + size 4 (AIRCRAFTCARRIER) horizontally goes past column 9
        assertThrows(InvalidCoordinateException.class,
                () -> board.placeShip(new Coordinate(0, 8), Orientation.HORIZONTAL, ShipType.AIRCRAFTCARRIER));
    }

    @Test
    void newBoardWithNoShipsIsConsideredDefeated() {
        // Documents current behavior: with an empty underlying Fleet,
        // isFleetDefeated() returns true even though no ships were placed.
        Board board = new Board();

        assertTrue(board.isFleetDefeated());
    }

    @Test
    void placingAShipDoesNotCurrentlyMarkItsCellsAsOccupied() {
        // KNOWN BUG: Board.placeShip() builds the Ship and adds it to the
        // internal Fleet, but never calls Cell.assignShip() on the cells
        // it occupies. As a result, the board's cells never reflect the
        // ship's presence and shooting on top of a placed ship is
        // currently indistinguishable from shooting an empty cell.
        //
        // This test documents the CURRENT (buggy) behavior on purpose.
        // Once placeShip() is fixed to link cells to their ship, this
        // test should be rewritten to assert hasShip() == true instead.
        Board board = new Board();
        Coordinate coordinate = new Coordinate(0, 0);

        board.placeShip(coordinate, Orientation.HORIZONTAL, ShipType.FRIGATE);

        assertFalse(board.getCell(coordinate).hasShip(),
                "Expected current buggy behavior: cell is not linked to its ship yet");
    }
}
