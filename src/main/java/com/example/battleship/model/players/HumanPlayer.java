package com.example.battleship.model.players;

import com.example.battleship.model.Board;
import com.example.battleship.model.Coordinate;
import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.enums.ShipType;
import com.example.battleship.model.exceptions.InvalidCoordinateException;
import com.example.battleship.model.exceptions.ShipOverLapException;
import com.example.battleship.model.Fleet;

public class HumanPlayer extends Player {

    public void placeShip(Coordinate coordinate, Orientation orientation, ShipType shipType) throws ShipOverLapException {
        board.placeShip(coordinate, orientation, shipType);
    }

    public int getPlacedShipsCount() {
        return board.getFleet().getShips().size();
    }

    public boolean isFleetComplete() {
        return getPlacedShipsCount() >= Fleet.getStandardComposition().size();
    }

    @Override
    public void placeFleet() {
        // Helper used by the UI as an optional auto-fill action.
        java.util.Random random = new java.util.Random();
        java.util.List<ShipType> composition = Fleet.getStandardComposition();
        java.util.List<Orientation> orientations = java.util.List.of(Orientation.HORIZONTAL, Orientation.VERTICAL);
        for (ShipType type : composition) {
            boolean placed = false;
            while (!placed) {
                int row = random.nextInt(10);
                int col = random.nextInt(10);
                int orientationRandom = random.nextInt(2);
                try {
                    Coordinate coordinate = new Coordinate(row, col);
                    this.board.placeShip(coordinate, orientations.get(orientationRandom), type);
                    placed = true;
                } catch (ShipOverLapException | InvalidCoordinateException e) {
                    // try again
                }
            }
        }
    }

    @Override
    public Coordinate chooseShotTarget() {
        return null;
    }
}
