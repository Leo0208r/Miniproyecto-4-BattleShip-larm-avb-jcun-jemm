package com.example.battleship.model;

import com.example.battleship.model.enums.CellState;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HuntTargetStrategyTest {

    @Test
    void huntModeShouldCoverTheWholeBoardWithoutRepeatingCoordinates() {
        HuntTargetStrategy strategy = new HuntTargetStrategy();
        Set<Coordinate> visited = new HashSet<>();

        for (int i = 0; i < 100; i++) {
            Coordinate target = strategy.selectTarget();
            assertTrue(visited.add(target), "Coordinate " + target + " was selected more than once");
            strategy.registerResult(target, CellState.WATER);
        }

        assertEquals(100, visited.size());
    }

    @Test
    void aHitShouldSwitchToTargetModeAndReturnAnOrthogonalNeighbor() {
        HuntTargetStrategy strategy = new HuntTargetStrategy();
        Coordinate hit = strategy.selectTarget();
        strategy.registerResult(hit, CellState.HIT);

        Coordinate next = strategy.selectTarget();

        assertTrue(isOrthogonalNeighbor(hit, next),
                "Expected " + next + " to be an orthogonal neighbor of " + hit);
    }

    @Test
    void chainedHitsShouldKeepTargetingNearbyUnshotCells() {
        HuntTargetStrategy strategy = new HuntTargetStrategy();
        Coordinate firstHit = new Coordinate(5, 5);
        Coordinate secondHit = new Coordinate(5, 6);

        strategy.registerResult(firstHit, CellState.HIT);
        strategy.registerResult(secondHit, CellState.HIT);

        Coordinate next = strategy.selectTarget();

        assertNotEquals(firstHit, next);
        assertNotEquals(secondHit, next);
        assertTrue(isOrthogonalNeighbor(firstHit, next) || isOrthogonalNeighbor(secondHit, next),
                "Expected " + next + " to be adjacent to one of the hit cells");
    }

    @Test
    void sinkingAShipShouldAllowHuntingToContinueOnTheRestOfTheBoard() {
        HuntTargetStrategy strategy = new HuntTargetStrategy();
        Coordinate hit = new Coordinate(0, 0);
        strategy.registerResult(hit, CellState.HIT);
        strategy.registerResult(hit, CellState.SUNK);

        Set<Coordinate> visited = new HashSet<>();
        visited.add(hit);
        for (int i = 0; i < 20; i++) {
            Coordinate target = strategy.selectTarget();
            assertTrue(visited.add(target), "Coordinate " + target + " was selected more than once");
            strategy.registerResult(target, CellState.WATER);
        }

        assertEquals(21, visited.size());
    }

    @Test
    void resetShouldAllowShotCoordinatesToBeSelectedAgain() {
        HuntTargetStrategy strategy = new HuntTargetStrategy();
        Coordinate first = strategy.selectTarget();
        strategy.registerResult(first, CellState.HIT);

        strategy.reset();

        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                Coordinate target = strategy.selectTarget();
                strategy.registerResult(target, CellState.WATER);
            }
        });
    }

    private boolean isOrthogonalNeighbor(Coordinate a, Coordinate b) {
        int rowDiff = Math.abs(a.getRow() - b.getRow());
        int colDiff = Math.abs(a.getCol() - b.getCol());
        return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1);
    }
}