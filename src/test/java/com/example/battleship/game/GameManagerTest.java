package com.example.battleship.game;

import com.example.battleship.model.Coordinate;
import com.example.battleship.model.enums.CellState;
import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.enums.ShipType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    @Test
    void newGameManagerShouldStartWithHumanTurnAndGivenNickname() {
        GameManager manager = new GameManager("Ana");

        assertTrue(manager.isHumanTurn());
        assertEquals("Ana", manager.getNickname());
        assertNotNull(manager.getHuman());
        assertNotNull(manager.getMachine());
    }

    @Test
    void setNicknameShouldUpdateNickname() {
        GameManager manager = new GameManager("Ana");

        manager.setNickname("Comandante");

        assertEquals("Comandante", manager.getNickname());
    }

    @Test
    void humanShootOnEmptyMachineBoardShouldReturnWaterAndSwitchTurnToMachine() throws Exception {
        GameManager manager = new GameManager("Ana");

        CellState result = manager.humanShoot(new Coordinate(0, 0));

        assertEquals(CellState.WATER, result);
        assertFalse(manager.isHumanTurn());
    }

    @Test
    void humanShootHittingAShipShouldKeepHumanTurn() throws Exception {
        GameManager manager = new GameManager("Ana");
        manager.getMachine().getBoard().placeShip(new Coordinate(0, 0), Orientation.HORIZONTAL, ShipType.FRIGATE);

        CellState result = manager.humanShoot(new Coordinate(0, 0));

        assertEquals(CellState.SUNK, result);
        assertTrue(manager.isHumanTurn());
    }

    @Test
    void machineShootOnEmptyHumanBoardShouldReturnWaterAndSwitchTurnToHuman() throws Exception {
        GameManager manager = new GameManager("Ana");
        manager.setHumanTurn(false);

        CellState result = manager.machineShoot();

        assertEquals(CellState.WATER, result);
        assertTrue(manager.isHumanTurn());
    }

    @Test
    void machineShootHittingAShipShouldKeepMachineTurn() throws Exception {
        GameManager manager = new GameManager("Ana");
        manager.getHuman().placeFleet();

        boolean hitObserved = false;
        for (int i = 0; i < 100 && !hitObserved; i++) {
            manager.setHumanTurn(false);
            CellState result = manager.machineShoot();
            if (result == CellState.HIT || result == CellState.SUNK) {
                hitObserved = true;
                assertFalse(manager.isHumanTurn());
            }
        }

        assertTrue(hitObserved, "Expected at least one hit while sweeping the whole board");
    }

    @Test
    void afterLoadShouldNotBreakSubsequentGameplay() throws Exception {
        GameManager manager = new GameManager("Ana");

        assertDoesNotThrow(manager::afterLoad);

        CellState result = manager.humanShoot(new Coordinate(0, 0));
        assertEquals(CellState.WATER, result);
    }
}