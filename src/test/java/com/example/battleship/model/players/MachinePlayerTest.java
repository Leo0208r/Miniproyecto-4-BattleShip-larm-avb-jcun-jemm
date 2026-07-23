package com.example.battleship.model.players;

import com.example.battleship.model.Coordinate;
import com.example.battleship.model.ships.Ship;
import com.example.battleship.model.enums.CellState;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MachinePlayerTest {

    @Test
    void boardShouldStartWithoutShipsBeforePlacingFleet() {
        MachinePlayer machine = new MachinePlayer();

        assertTrue(machine.getBoard().getFleet().getShips().isEmpty());
    }

    @Test
    void placeFleetShouldPlaceExactlyTenShipsWithCorrectDistribution() {
        MachinePlayer machine = new MachinePlayer();

        machine.placeFleet();

        var ships = machine.getBoard().getFleet().getShips();
        assertEquals(10, ships.size());

        long carriers = ships.stream().filter(s -> s.getSize() == 4).count();
        long submarines = ships.stream().filter(s -> s.getSize() == 3).count();
        long destroyers = ships.stream().filter(s -> s.getSize() == 2).count();
        long frigates = ships.stream().filter(s -> s.getSize() == 1).count();

        assertEquals(1, carriers);
        assertEquals(2, submarines);
        assertEquals(3, destroyers);
        assertEquals(4, frigates);
    }

    @Test
    void placeFleetShouldNotOverlapShips() {
        MachinePlayer machine = new MachinePlayer();

        machine.placeFleet();

        int occupiedCells = 0;
        for (var cell : machine.getBoard().getBoard().values()) {
            if (cell.hasShip()) {
                occupiedCells++;
            }
        }

        int expectedOccupiedCells = machine.getBoard().getFleet().getShips().stream()
                .mapToInt(Ship::getSize)
                .sum();

        assertEquals(expectedOccupiedCells, occupiedCells);
    }

    @Test
    void chooseShotTargetShouldReturnDifferentCoordinatesAsShotsAreRegistered() {
        MachinePlayer machine = new MachinePlayer();
        Set<Coordinate> visited = new HashSet<>();

        for (int i = 0; i < 20; i++) {
            Coordinate target = machine.chooseShotTarget();
            assertTrue(visited.add(target), "Coordinate " + target + " was selected more than once");
            machine.registerShotResult(target, CellState.WATER);
        }

        assertEquals(20, visited.size());
    }

    @Test
    void registerShotResultWithHitShouldMakeNextTargetAnOrthogonalNeighbor() {
        MachinePlayer machine = new MachinePlayer();
        Coordinate hit = machine.chooseShotTarget();

        machine.registerShotResult(hit, CellState.HIT);
        Coordinate next = machine.chooseShotTarget();

        int rowDiff = Math.abs(hit.getRow() - next.getRow());
        int colDiff = Math.abs(hit.getCol() - next.getCol());
        boolean isNeighbor = (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1);

        assertTrue(isNeighbor, "Expected " + next + " to be an orthogonal neighbor of " + hit);
    }
}