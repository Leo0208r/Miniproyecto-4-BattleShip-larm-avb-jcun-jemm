package com.example.battleship.model;

import com.example.battleship.model.enums.CellState;
import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.ships.AircraftCarrier;
import com.example.battleship.model.ships.Frigate;
import com.example.battleship.model.ships.Ship;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FleetTest {

    private List<Cell> cellsFor(int size, int row) {
        List<Cell> cells = new ArrayList<>();
        for (int col = 0; col < size; col++) {
            cells.add(new Cell(new Coordinate(row, col)));
        }
        return cells;
    }

    @Test
    void shouldStartEmpty() {
        Fleet fleet = new Fleet();

        assertTrue(fleet.getShips().isEmpty());
        assertEquals(0, fleet.getSunkShipsCount());
        assertEquals(0, fleet.getRemainingShipsCount());
    }

    @Test
    void shouldAddShipWithinLimits() {
        Fleet fleet = new Fleet();
        Ship carrier = new AircraftCarrier(Orientation.HORIZONTAL, cellsFor(4, 0));

        fleet.addShip(carrier);

        assertEquals(1, fleet.getShips().size());
        assertEquals(1, fleet.getRemainingShipsCount());
    }

    @Test
    void shouldThrowWhenExceedingMaxShipsForASize() {
        // The rules only allow 1 aircraft carrier (size 4)
        Fleet fleet = new Fleet();
        fleet.addShip(new AircraftCarrier(Orientation.HORIZONTAL, cellsFor(4, 0)));

        assertThrows(IllegalArgumentException.class,
                () -> fleet.addShip(new AircraftCarrier(Orientation.HORIZONTAL, cellsFor(4, 1))));
    }

    @Test
    void shouldAllowUpToFourFrigatesButNoMore() {
        Fleet fleet = new Fleet();
        for (int row = 0; row < 4; row++) {
            fleet.addShip(new Frigate(Orientation.HORIZONTAL, cellsFor(1, row)));
        }

        assertEquals(4, fleet.getShips().size());
        assertThrows(IllegalArgumentException.class,
                () -> fleet.addShip(new Frigate(Orientation.HORIZONTAL, cellsFor(1, 4))));
    }

    @Test
    void shouldReportFleetSunkOnlyWhenAllShipsAreSunk() {
        Fleet fleet = new Fleet();
        Cell cellA = new Cell(new Coordinate(0, 0));
        Cell cellB = new Cell(new Coordinate(1, 0));
        Ship frigateA = new Frigate(Orientation.HORIZONTAL, List.of(cellA));
        Ship frigateB = new Frigate(Orientation.HORIZONTAL, List.of(cellB));
        cellA.assignShip(frigateA);
        cellB.assignShip(frigateB);
        fleet.addShip(frigateA);
        fleet.addShip(frigateB);

        assertFalse(fleet.isFleetSunk());

        cellA.setCellState(CellState.HIT);
        frigateA.registerHit();
        assertFalse(fleet.isFleetSunk());
        assertEquals(1, fleet.getSunkShipsCount());
        assertEquals(1, fleet.getRemainingShipsCount());

        cellB.setCellState(CellState.HIT);
        frigateB.registerHit();
        assertTrue(fleet.isFleetSunk());
        assertEquals(2, fleet.getSunkShipsCount());
        assertEquals(0, fleet.getRemainingShipsCount());
    }

    @Test
    void newFleetWithNoShipsIsConsideredSunk() {
        // Documents current behavior: isFleetSunk() loops over an empty
        // ship list and finds nothing that ISN'T sunk, so it returns true
        // even though no ships were ever placed. Worth discussing whether
        // this is the intended behavior once the game engine is built.
        Fleet fleet = new Fleet();

        assertTrue(fleet.isFleetSunk());
    }
}
