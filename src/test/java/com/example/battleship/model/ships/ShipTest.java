package com.example.battleship.model.ships;

import com.example.battleship.model.Cell;
import com.example.battleship.model.Coordinate;
import com.example.battleship.model.enums.CellState;
import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.exceptions.InvalidShipSizeException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    private Cell cellAt(int row, int col) {
        return new Cell(new Coordinate(row, col));
    }

    @Test
    void shouldCreateShipWithMatchingCellCount() {
        List<Cell> cells = List.of(cellAt(0, 0), cellAt(0, 1));

        Destroyer destroyer = new Destroyer(Orientation.HORIZONTAL, cells);

        assertEquals(2, destroyer.getSize());
        assertEquals(Orientation.HORIZONTAL, destroyer.getOrientation());
        assertEquals(cells, destroyer.getCells());
    }

    @Test
    void shouldThrowWhenCellCountDoesNotMatchSize() {
        List<Cell> cells = List.of(cellAt(0, 0));

        assertThrows(InvalidShipSizeException.class,
                () -> new Destroyer(Orientation.HORIZONTAL, cells));
    }

    @Test
    void shouldNotBeSunkWhenNoCellsAreHit() {
        Cell cell = cellAt(0, 0);
        Frigate frigate = new Frigate(Orientation.HORIZONTAL, List.of(cell));

        assertFalse(frigate.isSunk());
    }

    @Test
    void shouldNotSinkWhileSomeCellsAreStillNotHit() {
        Cell cellA = cellAt(0, 0);
        Cell cellB = cellAt(0, 1);
        Destroyer destroyer = new Destroyer(Orientation.HORIZONTAL, List.of(cellA, cellB));
        cellA.assignShip(destroyer);
        cellB.assignShip(destroyer);

        cellA.setCellState(CellState.HIT);

        assertFalse(destroyer.registerHit());
        assertFalse(destroyer.isSunk());
    }

    @Test
    void shouldSinkOnceAllCellsAreHit() {
        Cell cellA = cellAt(0, 0);
        Cell cellB = cellAt(0, 1);
        Destroyer destroyer = new Destroyer(Orientation.HORIZONTAL, List.of(cellA, cellB));
        cellA.assignShip(destroyer);
        cellB.assignShip(destroyer);
        cellA.setCellState(CellState.HIT);
        cellB.setCellState(CellState.HIT);

        assertTrue(destroyer.registerHit());

        assertTrue(destroyer.isSunk());
        assertEquals(CellState.SUNK, cellA.getCellState());
        assertEquals(CellState.SUNK, cellB.getCellState());
    }

    @Test
    void cellsListShouldBeUnmodifiable() {
        Cell cell = cellAt(0, 0);
        Frigate frigate = new Frigate(Orientation.HORIZONTAL, List.of(cell));

        assertThrows(UnsupportedOperationException.class,
                () -> frigate.getCells().add(cellAt(1, 1)));
    }
}
