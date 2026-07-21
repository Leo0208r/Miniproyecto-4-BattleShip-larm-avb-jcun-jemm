package com.example.battleship.view;

import com.example.battleship.model.Coordinate;
import com.example.battleship.model.enums.Orientation;
import javafx.scene.shape.Circle;
import com.example.battleship.model.ships.Ship;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/**
 * 2D visual representation of a ship, drawn with JavaFX Shapes:
 * a Polygon hull with a pointed bow, an optional superstructure
 * (for ships of size 2+), a deck line, and a cyan neon glow.
 * Does not depend on the game engine: only needs size and
 * orientation to be drawn, so it can be built and tested in
 * isolation before turn logic exists.
 */

public class ShipView extends Group {

    public static final double CELL_SIZE = 32.0;
    private static final double MARGIN = 3.0;

    private final Orientation orientation;

    public ShipView(int size, Orientation orientation) {
        this.orientation = orientation;
        double length = size * CELL_SIZE - MARGIN * 2;
        double thickness = CELL_SIZE - MARGIN * 2;

        getChildren().add(buildHull(length, thickness));
        if (size >= 2) {
            getChildren().add(buildSuperstructure(length, thickness, size));
        }

        setEffect(new DropShadow(8, Color.web("#00ffff", 0.65)));
        getStyleClass().add("ship-view");
    }

    private Polygon buildHull(double length, double thickness) {
        double taper = Math.min(thickness, length * 0.35);
        Polygon polygon = new Polygon();
        if (orientation == Orientation.HORIZONTAL) {
            polygon.getPoints().addAll(
                    0.0, thickness / 2,
                    taper, 0.0,
                    length, 0.0,
                    length, thickness,
                    taper, thickness
            );
        } else {
            polygon.getPoints().addAll(
                    thickness / 2, 0.0,
                    0.0, taper,
                    0.0, length,
                    thickness, length,
                    thickness, taper
            );
        }
        polygon.setLayoutX(MARGIN);
        polygon.setLayoutY(MARGIN);
        polygon.getStyleClass().add("ship-hull");
        return polygon;
    }

    private Rectangle buildSuperstructure(double length, double thickness, int size) {
        double towerLength = length * (size >= 4 ? 0.32 : 0.22);
        double towerThickness = thickness * 0.5;
        Rectangle tower = new Rectangle();
        if (orientation == Orientation.HORIZONTAL) {
            tower.setWidth(towerLength);
            tower.setHeight(towerThickness);
            tower.setLayoutX(MARGIN + length * 0.42);
            tower.setLayoutY(MARGIN + thickness * 0.25);
        } else {
            tower.setWidth(towerThickness);
            tower.setHeight(towerLength);
            tower.setLayoutX(MARGIN + thickness * 0.25);
            tower.setLayoutY(MARGIN + length * 0.42);
        }
        tower.setArcWidth(4);
        tower.setArcHeight(4);
        tower.getStyleClass().add("ship-superstructure");
        return tower;
    }

    private Rectangle buildDeckLine(double length, double thickness) {
        Rectangle line = new Rectangle();
        double lineThickness = 1.4;
        if (orientation == Orientation.HORIZONTAL) {
            line.setWidth(length * 0.8);
            line.setHeight(lineThickness);
            line.setLayoutX(MARGIN + length * 0.1);
            line.setLayoutY(MARGIN + thickness / 2 - lineThickness / 2);
        } else {
            line.setWidth(lineThickness);
            line.setHeight(length * 0.8);
            line.setLayoutX(MARGIN + thickness / 2 - lineThickness / 2);
            line.setLayoutY(MARGIN + length * 0.1);
        }
        line.getStyleClass().add("ship-deck-line");
        return line;
    }

    public void markHit() {
        if (!getStyleClass().contains("ship-hit")) {
            getStyleClass().add("ship-hit");
        }
    }

    public void markSunk() {
        getStyleClass().remove("ship-hit");
        if (!getStyleClass().contains("ship-sunk")) {
            getStyleClass().add("ship-sunk");
        }
        setEffect(new DropShadow(6, Color.web("#ff3333", 0.5)));
    }

    public static ShipView from(Ship ship) {
        return new ShipView(ship.getSize(), ship.getOrientation());
    }

    public static ShipView placeOnGrid(GridPane grid, Ship ship) {
        ShipView view = from(ship);
        Coordinate start = ship.getCells().get(0).getCoordinate();

        int colSpan = ship.getOrientation() == Orientation.HORIZONTAL ? ship.getSize() : 1;
        int rowSpan = ship.getOrientation() == Orientation.VERTICAL ? ship.getSize() : 1;

        grid.add(view, start.getCol(), start.getRow(), colSpan, rowSpan);
        return view;
    }
}