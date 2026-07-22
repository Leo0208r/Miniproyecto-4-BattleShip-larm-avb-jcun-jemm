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

        // render all existing cell states (important for loaded games)
        for (var cellEntry : board.getBoard().entrySet()) {
            Coordinate coord = cellEntry.getKey();
            CellState state = cellEntry.getValue().getCellState();
            String id = "cell_" + coord.getRow() + "_" + coord.getCol();
            Node node = grid.lookup("#" + id);
            if (node instanceof Pane pane && (state == CellState.WATER || state == CellState.HIT || state == CellState.SUNK)) {
                // Remove any existing mark for this cell (we'll add it directly to the grid so it can
                // render above ShipView without the cell background covering it).
                String markId = "mark_" + coord.getRow() + "_" + coord.getCol();
                Node existing = grid.lookup("#" + markId);
                if (existing != null) grid.getChildren().remove(existing);

                ShotMarkView mark;
                if (state == CellState.WATER) {
                    mark = ShotMarkView.water();
                } else if (state == CellState.HIT) {
                    mark = ShotMarkView.hit();
                } else {
                    mark = ShotMarkView.sunk();
                }
                mark.setId(markId);
                mark.setMouseTransparent(true);
                grid.add(mark, coord.getCol(), coord.getRow());
                mark.toFront();
            }
        }

        // register listener to update cells when they change
        board.addListener((Coordinate coordinate, CellState newState) -> {
            Platform.runLater(() -> {
                String id = "cell_" + coordinate.getRow() + "_" + coordinate.getCol();
                Node node = grid.lookup("#" + id);
                if (node instanceof Pane pane) {
                    String markId = "mark_" + coordinate.getRow() + "_" + coordinate.getCol();
                    Node existing = grid.lookup("#" + markId);
                    if (existing != null) grid.getChildren().remove(existing);

                    if (newState == CellState.WATER) {
                        ShotMarkView m = ShotMarkView.water();
                        m.setId(markId);
                        m.setMouseTransparent(true);
                        grid.add(m, coordinate.getCol(), coordinate.getRow());
                        m.toFront();
                    } else if (newState == CellState.HIT) {
                        ShotMarkView m = ShotMarkView.hit();
                        m.setId(markId);
                        m.setMouseTransparent(true);
                        grid.add(m, coordinate.getCol(), coordinate.getRow());
                        m.toFront();
                    } else if (newState == CellState.SUNK) {
                        ShotMarkView m = ShotMarkView.sunk();
                        m.setId(markId);
                        m.setMouseTransparent(true);
                        grid.add(m, coordinate.getCol(), coordinate.getRow());
                        m.toFront();
                    }
                }
            });
        });
    }
}




