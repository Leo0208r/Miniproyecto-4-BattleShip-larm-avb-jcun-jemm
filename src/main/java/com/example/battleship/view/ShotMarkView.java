package com.example.battleship.view;

import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * Marca visual de un disparo sobre una celda individual del tablero,
 * dibujada con JavaFX Shapes. Se usa para "agua" (un punto/splash) y
 * "tocado" (una X), separado de ShipView porque estas marcas viven
 * sobre celdas sueltas, no sobre el barco completo.
 *
 * El estado "hundido" se refleja llamando a ShipView.markSunk() en el
 * barco correspondiente, no aquí.
 */
public class ShotMarkView extends Group {

    private ShotMarkView() {
        getStyleClass().add("shot-mark");
    }

    /** Punto pequeño centrado en la celda, para un disparo al agua. */
    public static ShotMarkView water() {
        ShotMarkView mark = new ShotMarkView();
        Circle splash = new Circle(ShipView.CELL_SIZE / 6.0);
        splash.setCenterX(ShipView.CELL_SIZE / 2.0);
        splash.setCenterY(ShipView.CELL_SIZE / 2.0);
        splash.getStyleClass().add("mark-water");
        mark.getChildren().add(splash);
        return mark;
    }

    /** X centrada en la celda, para un disparo que tocó un barco. */
    public static ShotMarkView hit() {
        ShotMarkView mark = new ShotMarkView();
        double margin = 8.0;
        double size = ShipView.CELL_SIZE;

        Line diagonal1 = new Line(margin, margin, size - margin, size - margin);
        Line diagonal2 = new Line(size - margin, margin, margin, size - margin);
        diagonal1.getStyleClass().add("mark-hit");
        diagonal2.getStyleClass().add("mark-hit");

        mark.getChildren().addAll(diagonal1, diagonal2);
        return mark;
    }
}
