package com.example.battleship.view;

import com.example.battleship.model.Coordinate;
import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.ships.Ship;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * Representación visual 2D de un barco, dibujada con JavaFX Shapes
 * (un Rectangle con esquinas redondeadas + dos Circle en las puntas,
 * dando una forma de "cápsula" tipo submarino/nave).
 *
 * No depende del motor de juego: solo necesita el tamaño y la
 * orientación del barco para poder dibujarse, por lo que puede
 * construirse y probarse de forma aislada, antes de que exista
 * la lógica de turnos.
 */
public class ShipView extends Group {

    /** Debe coincidir con el tamaño de celda usado en GameController (32x32). */
    public static final double CELL_SIZE = 32.0;
    private static final double MARGIN = 3.0;

    private final Rectangle body;
    private final Orientation orientation;

    public ShipView(int size, Orientation orientation) {
        this.orientation = orientation;
        this.body = buildBody(size);
        getChildren().add(body);
        addBowAndStern();
        getStyleClass().add("ship-view");
    }

    private Rectangle buildBody(int size) {
        double length = size * CELL_SIZE - MARGIN * 2;
        double thickness = CELL_SIZE - MARGIN * 2;

        Rectangle rect = new Rectangle();
        if (orientation == Orientation.HORIZONTAL) {
            rect.setWidth(length);
            rect.setHeight(thickness);
        } else {
            rect.setWidth(thickness);
            rect.setHeight(length);
        }
        rect.setX(MARGIN);
        rect.setY(MARGIN);
        rect.setArcWidth(thickness * 0.6);
        rect.setArcHeight(thickness * 0.6);
        rect.getStyleClass().add("ship-body");
        return rect;
    }

    private void addBowAndStern() {
        double radius = (CELL_SIZE - MARGIN * 2) / 2.0;

        Circle bow = new Circle(radius);
        Circle stern = new Circle(radius);
        bow.getStyleClass().add("ship-tip");
        stern.getStyleClass().add("ship-tip");

        if (orientation == Orientation.HORIZONTAL) {
            bow.setCenterX(MARGIN + radius);
            bow.setCenterY(MARGIN + radius);
            stern.setCenterX(MARGIN + body.getWidth() - radius);
            stern.setCenterY(MARGIN + radius);
        } else {
            bow.setCenterX(MARGIN + radius);
            bow.setCenterY(MARGIN + radius);
            stern.setCenterX(MARGIN + radius);
            stern.setCenterY(MARGIN + body.getHeight() - radius);
        }
        getChildren().addAll(bow, stern);
    }

    /** Aplica el estilo visual de "tocado" (aún no hundido). */
    public void markHit() {
        if (!getStyleClass().contains("ship-hit")) {
            getStyleClass().add("ship-hit");
        }
    }

    /** Aplica el estilo visual de "hundido". */
    public void markSunk() {
        getStyleClass().remove("ship-hit");
        if (!getStyleClass().contains("ship-sunk")) {
            getStyleClass().add("ship-sunk");
        }
    }

    /**
     * Construye el ShipView correspondiente a un Ship del modelo,
     * usando su tamaño y orientación.
     */
    public static ShipView from(Ship ship) {
        return new ShipView(ship.getSize(), ship.getOrientation());
    }

    /**
     * Crea el ShipView de un barco y lo agrega al GridPane, alineado
     * sobre las celdas que ocupa (usando colSpan/rowSpan según su
     * tamaño y orientación). Asume que ship.getCells() está ordenado
     * de proa a popa, tal como lo arma Board.calculateShipCoordinates().
     */
    public static ShipView placeOnGrid(GridPane grid, Ship ship) {
        ShipView view = from(ship);
        Coordinate start = ship.getCells().get(0).getCoordinate();

        int colSpan = ship.getOrientation() == Orientation.HORIZONTAL ? ship.getSize() : 1;
        int rowSpan = ship.getOrientation() == Orientation.VERTICAL ? ship.getSize() : 1;

        grid.add(view, start.getCol(), start.getRow(), colSpan, rowSpan);
        return view;
    }
}
