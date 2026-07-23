package com.example.battleship.view;

import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * Visual shot marker component drawn over an individual board cell using JavaFX {@link Group} shapes.
 * <p>
 * Displays specific visual representations for water misses (a splash dot), hits (a red X),
 * and sunk status markers (a ring with crosshairs). These markers render independently of {@link ShipView}
 * and sit above ship vectors in the z-order hierarchy.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class ShotMarkView extends Group {
    /**
     * Private constructor to instantiate a base shot mark group with the CSS style class applied.
     */
    private ShotMarkView() {
        getStyleClass().add("shot-mark");
    }
    /**
     * Creates a visual marker for a missed shot landing in water (a blue circle splash).
     *
     * @return A configured {@code ShotMarkView} for a water miss.
     */
    public static ShotMarkView water() {
        ShotMarkView mark = new ShotMarkView();
        // Marcas deben tener viewOrder mucho menor para dibujarse por encima de los barcos
        mark.setViewOrder(-100.0);
        Circle splash = new Circle(ShipView.CELL_SIZE / 6.0);
        splash.setCenterX(ShipView.CELL_SIZE / 2.0);
        splash.setCenterY(ShipView.CELL_SIZE / 2.0);
        splash.setFill(javafx.scene.paint.Color.web("#38bdf8"));
        splash.getStyleClass().add("mark-water");
        mark.getChildren().add(splash);
        return mark;
    }
    /**
     * Creates a visual marker for a successful shot hit on a vessel cell (a red diagonal X).
     *
     * @return A configured {@code ShotMarkView} for a successful hit.
     */
    public static ShotMarkView hit() {
        ShotMarkView mark = new ShotMarkView();
        // Marcas deben tener viewOrder mucho menor para dibujarse por encima de los barcos
        mark.setViewOrder(-100.0);
        double margin = 8.0;
        double size = ShipView.CELL_SIZE;

        Line diagonal1 = new Line(margin, margin, size - margin, size - margin);
        Line diagonal2 = new Line(size - margin, margin, margin, size - margin);
        diagonal1.setStroke(javafx.scene.paint.Color.web("#ef4444"));
        diagonal2.setStroke(javafx.scene.paint.Color.web("#ef4444"));
        diagonal1.setStrokeWidth(3.0);
        diagonal2.setStrokeWidth(3.0);
        diagonal1.getStyleClass().add("mark-hit");
        diagonal2.getStyleClass().add("mark-hit");

        mark.getChildren().addAll(diagonal1, diagonal2);
        return mark;
    }
    /**
     * Creates an intensified visual marker for a cell belonging to a completely destroyed/sunk vessel
     * (a dark red target ring combined with a red diagonal X).
     *
     * @return A configured {@code ShotMarkView} for a sunk vessel cell.
     */
    public static ShotMarkView sunk() {
        ShotMarkView mark = new ShotMarkView();
        // Marcas deben tener viewOrder mucho menor para dibujarse por encima de los barcos
        mark.setViewOrder(-100.0);
        Circle ring = new Circle(ShipView.CELL_SIZE / 3.8);
        ring.setCenterX(ShipView.CELL_SIZE / 2.0);
        ring.setCenterY(ShipView.CELL_SIZE / 2.0);
        ring.setFill(javafx.scene.paint.Color.rgb(239, 68, 68, 0.12));
        ring.setStroke(javafx.scene.paint.Color.web("#b91c1c"));
        ring.setStrokeWidth(2.0);
        ring.getStyleClass().add("mark-sunk");

        Line diagonal1 = new Line(7.0, 7.0, ShipView.CELL_SIZE - 7.0, ShipView.CELL_SIZE - 7.0);
        Line diagonal2 = new Line(ShipView.CELL_SIZE - 7.0, 7.0, 7.0, ShipView.CELL_SIZE - 7.0);
        diagonal1.setStroke(javafx.scene.paint.Color.web("#b91c1c"));
        diagonal2.setStroke(javafx.scene.paint.Color.web("#b91c1c"));
        diagonal1.setStrokeWidth(3.0);
        diagonal2.setStrokeWidth(3.0);
        diagonal1.getStyleClass().add("mark-sunk");
        diagonal2.getStyleClass().add("mark-sunk");

        mark.getChildren().addAll(ring, diagonal1, diagonal2);
        return mark;
    }
}
