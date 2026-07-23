package com.example.battleship.model;

import com.example.battleship.model.enums.CellState;
import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.enums.ShipType;
import com.example.battleship.model.exceptions.InvalidCoordinateException;
import com.example.battleship.model.exceptions.RepeatedShotException;
import com.example.battleship.model.exceptions.ShipOverLapException;
import org.junit.jupiter.api.Test;
import com.example.battleship.model.BoardListener;
import java.util.ArrayList;
import java.util.List;

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
    void placingAShipShouldMarkItsCellsAsOccupied() throws ShipOverLapException {
        Board board = new Board();
        Coordinate coordinate = new Coordinate(0, 0);

        board.placeShip(coordinate, Orientation.HORIZONTAL, ShipType.FRIGATE);

        assertTrue(board.getCell(coordinate).hasShip());
        assertEquals(CellState.OCCUPIED, board.getCell(coordinate).getCellState());
    }

    @Test
    void placingAShipOnAnOccupiedCellShouldThrowShipOverLapException() throws ShipOverLapException {
        Board board = new Board();
        board.placeShip(new Coordinate(0, 0), Orientation.HORIZONTAL, ShipType.FRIGATE);

        assertThrows(ShipOverLapException.class,
                () -> board.placeShip(new Coordinate(0, 0), Orientation.HORIZONTAL, ShipType.FRIGATE));
    }

    @Test
    void shootingAPlacedShipShouldSinkIt() throws RepeatedShotException, ShipOverLapException {
        Board board = new Board();
        Coordinate coordinate = new Coordinate(0, 0);
        board.placeShip(coordinate, Orientation.HORIZONTAL, ShipType.FRIGATE);

        CellState result = board.shoot(coordinate);

        assertEquals(CellState.SUNK, result);
        assertTrue(board.isFleetDefeated());
    }

    @Test
    void shootingShouldNotifyListenerWithCorrectCoordinateAndState() throws RepeatedShotException {
        Board board = new Board();
        Coordinate coordinate = new Coordinate(0, 0);

        List<Coordinate> notifiedCoordinates = new ArrayList<>();
        List<CellState> notifiedStates = new ArrayList<>();
        board.addListener((coord, state) -> {
            notifiedCoordinates.add(coord);
            notifiedStates.add(state);
        });

        board.shoot(coordinate);

        assertEquals(List.of(coordinate), notifiedCoordinates);
        assertEquals(List.of(CellState.WATER), notifiedStates);
    }

    @Test
    void placingAShipShouldNotifyListenerOncePerCell() throws ShipOverLapException {
        Board board = new Board();
        List<CellState> notifiedStates = new ArrayList<>();
        board.addListener((coord, state) -> notifiedStates.add(state));

        board.placeShip(new Coordinate(0, 0), Orientation.HORIZONTAL, ShipType.DESTROYER);

        assertEquals(2, notifiedStates.size());
        assertEquals(CellState.OCCUPIED, notifiedStates.get(0));
        assertEquals(CellState.OCCUPIED, notifiedStates.get(1));
    }

    @Test
    void multipleListenersShouldAllBeNotified() throws RepeatedShotException {
        Board board = new Board();
        List<CellState> firstListenerStates = new ArrayList<>();
        List<CellState> secondListenerStates = new ArrayList<>();
        board.addListener((coord, state) -> firstListenerStates.add(state));
        board.addListener((coord, state) -> secondListenerStates.add(state));

        board.shoot(new Coordinate(0, 0));

        assertEquals(List.of(CellState.WATER), firstListenerStates);
        assertEquals(List.of(CellState.WATER), secondListenerStates);
    }

    @Test
    void removedListenerShouldNotBeNotified() throws RepeatedShotException {
        Board board = new Board();
        List<CellState> notifiedStates = new ArrayList<>();
        BoardListener listener = (coord, state) -> notifiedStates.add(state);
        board.addListener(listener);
        board.removeListener(listener);

        board.shoot(new Coordinate(0, 0));

        assertTrue(notifiedStates.isEmpty());
    }

    @Test
    void addingNullListenerShouldNotThrow() {
        Board board = new Board();

        assertDoesNotThrow(() -> board.addListener(null));
    }
}
