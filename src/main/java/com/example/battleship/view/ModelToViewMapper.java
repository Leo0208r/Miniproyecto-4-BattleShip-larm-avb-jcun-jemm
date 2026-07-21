package com.example.battleship.view;

import com.example.battleship.model.Board;
import com.example.battleship.model.BoardListener;
import com.example.battleship.model.Coordinate;
import com.example.battleship.model.enums.CellState;
import com.example.battleship.model.ships.Ship;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Simple mapper that links a model Board with a GridPane created by the
 * controller. It listens to board events and updates the GridPane nodes.
 */
public class ModelToViewMapper {

    public static void bindBoardToGrid(Board board, GridPane grid, boolean showShips) {
        // place existing ships if requested
        if (showShips) {
            for (Ship ship : board.getFleet().getShips()) {
                // place ship visually using ShipView
                ShipView.placeOnGrid(grid, ship);
            }
        }

        // register listener to update cells when they change
        board.addListener((Coordinate coordinate, CellState newState) -> {
            Platform.runLater(() -> {
                String id = "cell_" + coordinate.getRow() + "_" + coordinate.getCol();
                Node node = grid.lookup("#" + id);
                if (node instanceof Pane pane) {
                    pane.getChildren().clear();
                    if (newState == CellState.WATER) {
                        pane.getChildren().add(ShotMarkView.water());
                    } else if (newState == CellState.HIT) {
                        pane.getChildren().add(ShotMarkView.hit());
                    } else if (newState == CellState.SUNK) {
                        // optionally show sunk mark (use hit mark for now)
                        pane.getChildren().add(ShotMarkView.hit());
                    }
                }
            });
        });
    }
}

