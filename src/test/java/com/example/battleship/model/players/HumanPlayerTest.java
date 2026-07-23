package com.example.battleship.model.players;

import com.example.battleship.model.Coordinate;
import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.enums.ShipType;
import com.example.battleship.model.exceptions.ShipOverLapException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HumanPlayerTest {

    @Test
    void newHumanPlayerShouldHaveNoShipsAndIncompleteFleet() {
        HumanPlayer human = new HumanPlayer();

        assertEquals(0, human.getPlacedShipsCount());
        assertFalse(human.isFleetComplete());
    }

    @Test
    void placeShipShouldAddOneShipToTheFleet() throws ShipOverLapException {
        HumanPlayer human = new HumanPlayer();

        human.placeShip(new Coordinate(0, 0), Orientation.HORIZONTAL, ShipType.FRIGATE);

        assertEquals(1, human.getPlacedShipsCount());
    }

    @Test
    void placeShipOnAnOccupiedCellShouldThrow() throws ShipOverLapException {
        HumanPlayer human = new HumanPlayer();
        human.placeShip(new Coordinate(0, 0), Orientation.HORIZONTAL, ShipType.FRIGATE);

        assertThrows(ShipOverLapException.class,
                () -> human.placeShip(new Coordinate(0, 0), Orientation.HORIZONTAL, ShipType.FRIGATE));
    }

    @Test
    void isFleetCompleteShouldBeFalseWithPartialPlacement() throws ShipOverLapException {
        HumanPlayer human = new HumanPlayer();
        human.placeShip(new Coordinate(0, 0), Orientation.HORIZONTAL, ShipType.FRIGATE);
        human.placeShip(new Coordinate(1, 0), Orientation.HORIZONTAL, ShipType.FRIGATE);

        assertFalse(human.isFleetComplete());
    }

    @Test
    void placeFleetShouldAutoFillAllTenShipsAndCompleteTheFleet() {
        HumanPlayer human = new HumanPlayer();

        human.placeFleet();

        assertEquals(10, human.getPlacedShipsCount());
        assertTrue(human.isFleetComplete());
    }

    @Test
    void chooseShotTargetCurrentlyAlwaysReturnsNull() {
        // Documents current behavior: HumanPlayer.chooseShotTarget() is not
        // implemented since the human's shots are driven by mouse clicks in
        // GameController, not by this method. If that ever changes, this
        // test should be updated to reflect the new behavior.
        HumanPlayer human = new HumanPlayer();

        assertNull(human.chooseShotTarget());
    }
}