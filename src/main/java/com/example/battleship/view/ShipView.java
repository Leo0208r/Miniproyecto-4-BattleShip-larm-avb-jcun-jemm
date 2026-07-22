package com.example.battleship.view;

import com.example.battleship.model.Coordinate;
import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.enums.ShipType;
import com.example.battleship.model.ships.AircraftCarrier;
import com.example.battleship.model.ships.Destroyer;
import com.example.battleship.model.ships.Frigate;
import com.example.battleship.model.ships.Ship;
import com.example.battleship.model.ships.Submarine;
import com.example.battleship.view.shapes.ShipShapeFactory;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;


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


    private final Group shape;

    /** Construye la vista del barco usando las implementaciones detalladas en view.shapes */
    public ShipView(Ship ship) {
        ShipType type = detectType(ship);
        this.shape = ShipShapeFactory.create(type, ship.getOrientation(), CELL_SIZE);
        getChildren().add(shape);
        getStyleClass().add("ship-view");
        // Por defecto, darle un viewOrder neutro: las marcas usan valores negativos
        // para pintarse por encima; las celdas deben estar detrás.
        this.setViewOrder(0.0);
    }

    // Los detalles de la forma están en los paquetes view.shapes; no necesitamos construir
    // manualmente rectángulos aquí. Mantenemos MARGIN por compatibilidad futura.

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
        // Enviar el ShipView al fondo (en caso necesario) y mantener viewOrder neutro
        this.toBack();
        this.setViewOrder(0.0);
        this.setMouseTransparent(true);
    }

    /**
      /** Construye el ShipView correspondiente a un Ship del modelo,
       * usando la forma detallada según su tipo y orientación.
       */
      public static ShipView from(Ship ship) {
          return new ShipView(ship);
      }

      private static ShipType detectType(Ship ship) {
          if (ship instanceof AircraftCarrier) return ShipType.AIRCRAFTCARRIER;
          if (ship instanceof Submarine) return ShipType.SUBMARINE;
          if (ship instanceof Destroyer) return ShipType.DESTROYER;
          if (ship instanceof Frigate) return ShipType.FRIGATE;
          // fallback by size
          switch (ship.getSize()) {
              case 4: return ShipType.AIRCRAFTCARRIER;
              case 3: return ShipType.SUBMARINE;
              case 2: return ShipType.DESTROYER;
              default: return ShipType.FRIGATE;
          }
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
