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
 * JavaFX {@link Group} component representing the visual display of a vessel on the game board grid.
 * <p>
 * Constructs 2D vector shapes using {@link ShipShapeFactory}, applies hit/sunk visual state CSS classes,
 * and handles layout positioning spans inside JavaFX {@link GridPane} instances.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */

public class ShipView extends Group {
    /** Standard grid cell dimension in pixels used for shape scaling. */
    public static final double CELL_SIZE = 32.0;

    /** Graphical vector group representing the ship shape. */
    private final Group shape;
    /**
     * Constructs a {@code ShipView} instance for a given model {@link Ship}.
     *
     * @param ship The model {@link Ship} instance to visualize.
     */
    public ShipView(Ship ship) {
        ShipType type = detectType(ship);
        this.shape = ShipShapeFactory.create(type, ship.getOrientation(), CELL_SIZE);
        getChildren().add(shape);
        getStyleClass().add("ship-view");

        this.setViewOrder(0.0);
    }


    /**
     * Applies the hit visual style class to indicate damage on this vessel.
     */
    public void markHit() {
        if (!getStyleClass().contains("ship-hit")) {
            getStyleClass().add("ship-hit");
        }
    }
    /**
     * Applies the sunk visual style class, sends the view layer back, and disables mouse transparency.
     */
    public void markSunk() {
        getStyleClass().remove("ship-hit");
        if (!getStyleClass().contains("ship-sunk")) {
            getStyleClass().add("ship-sunk");
        }
        this.toBack();
        this.setViewOrder(0.0);
        this.setMouseTransparent(true);
    }

    /**
     * Static factory method to instantiate a new {@link ShipView} for a given {@link Ship}.
     *
     * @param ship The model {@link Ship} instance.
     * @return A new {@link ShipView} instance.
     */
      public static ShipView from(Ship ship) {
          return new ShipView(ship);
      }
    /**
     * Identifies the specific {@link ShipType} of a vessel based on class instance or unit length.
     *
     * @param ship The target {@link Ship} instance.
     * @return The corresponding {@link ShipType} enum.
     */
      private static ShipType detectType(Ship ship) {
          if (ship instanceof AircraftCarrier) return ShipType.AIRCRAFTCARRIER;
          if (ship instanceof Submarine) return ShipType.SUBMARINE;
          if (ship instanceof Destroyer) return ShipType.DESTROYER;
          if (ship instanceof Frigate) return ShipType.FRIGATE;
          switch (ship.getSize()) {
              case 4: return ShipType.AIRCRAFTCARRIER;
              case 3: return ShipType.SUBMARINE;
              case 2: return ShipType.DESTROYER;
              default: return ShipType.FRIGATE;
          }
      }

    /**
     * Creates a {@link ShipView} and adds it to the target {@link GridPane} occupying the appropriate coordinate span.
     *
     * @param grid The target {@link GridPane} board container.
     * @param ship The model {@link Ship} to be placed.
     * @return The created and placed {@link ShipView} instance.
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
