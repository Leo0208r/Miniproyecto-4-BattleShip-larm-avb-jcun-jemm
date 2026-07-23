package com.example.battleship.view.shapes.ships;

import com.example.battleship.view.shapes.util.Colors;
import com.example.battleship.view.shapes.util.Gradients;
import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
/**
 * Utility factory for constructing the graphical 2D JavaFX representation of a Frigate ship.
 * <p>
 * Assembles vector shapes including a compact hull, command bridge structure, naval artillery,
 * missile launcher tubes, radar systems, and ambient lighting effects.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public final class FrigateShape {
    /** Horizontal size multiplier corresponding to 1 grid cell. */
    private static final int SHIP_LENGTH_CELLS = 1;
    /** Vertical scaling factor relative to individual cell height. */
    private static final double SHIP_HEIGHT_FACTOR = 0.72;
    /**
     * Private constructor to prevent instantiation of static factory utility.
     */
    private FrigateShape() {
    }
    /**
     * Creates a composite JavaFX {@link Group} representing the Frigate vessel.
     *
     * @param cellSize The width and height in pixels of a single grid cell.
     * @return {@link Group} containing all vector elements of the rendered vessel.
     */
    public static Group create(double cellSize) {

        Group ship = new Group();

        buildShadow(ship, cellSize);
        buildHull(ship, cellSize);
        buildBridge(ship, cellSize);
        buildWeapons(ship, cellSize);
        buildDetails(ship, cellSize);
        buildHighlights(ship, cellSize);

        return ship;
    }
    /**
     * Constructs the primary hull polygon using gradient fills and border strokes.
     *
     * @param ship     Parent {@link Group} container.
     * @param cellSize Cell dimensions for layout calculations.
     */
    private static void buildHull(Group ship, double cellSize) {

        double w = SHIP_LENGTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2.0;

        Polygon hull = new Polygon(

                w * 0.05, y + h * 0.50,

                w * 0.28, y + h * 0.12,

                w * 0.82, y + h * 0.12,

                w * 0.95, y + h * 0.50,

                w * 0.82, y + h * 0.88,

                w * 0.28, y + h * 0.88

        );

        hull.setFill(Gradients.destroyerHull());
        hull.setStroke(Colors.BORDER);
        hull.setStrokeWidth(2);

        ship.getChildren().add(hull);

    }
    /**
     * Constructs the superstructure bridge, main mast, radar arrays, and communication antennas.
     *
     * @param ship     Parent {@link Group} container.
     * @param cellSize Cell dimensions for layout calculations.
     */
    private static void buildBridge(Group ship, double cellSize) {

        double w = SHIP_LENGTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2.0;

        //----------------------------------------
        // Superestructura
        //----------------------------------------

        Rectangle bridge = new Rectangle(
                w * 0.34,
                y + h * 0.24,
                w * 0.28,
                h * 0.32
        );

        bridge.setArcWidth(6);
        bridge.setArcHeight(6);

        bridge.setFill(Gradients.tower());
        bridge.setStroke(Colors.BORDER);

        //----------------------------------------
        // Cabina superior
        //----------------------------------------

        Rectangle cabin = new Rectangle(
                w * 0.40,
                y + h * 0.14,
                w * 0.14,
                h * 0.12
        );

        cabin.setArcWidth(4);
        cabin.setArcHeight(4);

        cabin.setFill(Color.web("#6B747B"));

        //----------------------------------------
        // Ventanas
        //----------------------------------------

        for (int i = 0; i < 2; i++) {

            Rectangle window = new Rectangle(
                    w * (0.42 + i * 0.05),
                    y + h * 0.17,
                    w * 0.025,
                    h * 0.03
            );

            window.setFill(Color.web("#BFDDF8"));

            ship.getChildren().add(window);
        }

        //----------------------------------------
        // Mástil
        //----------------------------------------

        Line mast = new Line(
                w * 0.48,
                y + h * 0.14,
                w * 0.48,
                y - h * 0.18
        );

        mast.setStroke(Color.GRAY);
        mast.setStrokeWidth(2);

        //----------------------------------------
        // Radar
        //----------------------------------------

        Line radar = new Line(
                w * 0.43,
                y - h * 0.08,
                w * 0.53,
                y - h * 0.08
        );

        radar.setStroke(Color.SILVER);
        radar.setStrokeWidth(1.5);

        //----------------------------------------
        // Antena
        //----------------------------------------

        Line antenna = new Line(
                w * 0.48,
                y - h * 0.18,
                w * 0.45,
                y - h * 0.30
        );

        antenna.setStroke(Color.DARKGRAY);

        ship.getChildren().addAll(
                bridge,
                cabin,
                mast,
                radar,
                antenna
        );
    }
    /**
     * Builds ship armaments including the forward naval gun turret, missile launcher box, and light CIWS defense system.
     *
     * @param ship     Parent {@link Group} container.
     * @param cellSize Cell dimensions for layout calculations.
     */    private static void buildWeapons(Group ship, double cellSize) {

        double w = SHIP_LENGTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2.0;

        //----------------------------------------
        // Cañón principal
        //----------------------------------------

        Circle turret = new Circle(
                w * 0.22,
                y + h * 0.50,
                cellSize * 0.07
        );

        turret.setFill(Color.web("#5B6369"));
        turret.setStroke(Colors.BORDER);

        Line barrel = new Line(
                w * 0.22,
                y + h * 0.50,
                w * 0.08,
                y + h * 0.50
        );

        barrel.setStroke(Color.BLACK);
        barrel.setStrokeWidth(2.5);

        //----------------------------------------
        // Lanzador de misiles
        //----------------------------------------

        Rectangle launcher = new Rectangle(
                w * 0.62,
                y + h * 0.36,
                w * 0.12,
                h * 0.16
        );

        launcher.setArcWidth(4);
        launcher.setArcHeight(4);

        launcher.setRotate(-15);

        launcher.setFill(Color.web("#6A7278"));
        launcher.setStroke(Colors.BORDER);

        //----------------------------------------
        // Tubos lanzamisiles
        //----------------------------------------

        for (int i = 0; i < 2; i++) {

            Rectangle tube = new Rectangle(
                    w * 0.645,
                    y + h * (0.39 + i * 0.06),
                    w * 0.06,
                    h * 0.025
            );

            tube.setFill(Color.web("#40464A"));

            ship.getChildren().add(tube);
        }

        //----------------------------------------
        // CIWS ligero
        //----------------------------------------

        Circle ciws = new Circle(
                w * 0.76,
                y + h * 0.58,
                cellSize * 0.035
        );

        ciws.setFill(Color.DARKGRAY);
        ciws.setStroke(Color.BLACK);

        ship.getChildren().addAll(
                turret,
                barrel,
                launcher,
                ciws
        );
    }
    /**
     * Adds hull panel seam lines, access hatches, deck ventilation grates, and structural rivets.
     *
     * @param ship     Parent {@link Group} container.
     * @param cellSize Cell dimensions for layout calculations.
     */
    private static void buildDetails(Group ship, double cellSize) {

        double w = SHIP_LENGTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2.0;

        //----------------------------------------
        // Paneles del casco
        //----------------------------------------

        for (int i = 1; i <= 2; i++) {

            Line panel = new Line(
                    w * (0.35 + i * 0.12),
                    y + h * 0.22,
                    w * (0.35 + i * 0.12),
                    y + h * 0.78
            );

            panel.setStroke(Color.rgb(255, 255, 255, 0.12));
            panel.setStrokeWidth(1);

            ship.getChildren().add(panel);
        }

        //----------------------------------------
        // Escotilla
        //----------------------------------------

        Circle hatch = new Circle(
                w * 0.48,
                y + h * 0.68,
                cellSize * 0.035
        );

        hatch.setFill(Color.web("#7A8288"));
        hatch.setStroke(Colors.BORDER);

        //----------------------------------------
        // Respiraderos
        //----------------------------------------

        for (int i = 0; i < 3; i++) {

            Rectangle vent = new Rectangle(
                    w * (0.40 + i * 0.05),
                    y + h * 0.31,
                    w * 0.025,
                    h * 0.05
            );

            vent.setArcWidth(3);
            vent.setArcHeight(3);

            vent.setFill(Color.SILVER);

            ship.getChildren().add(vent);
        }

        //----------------------------------------
        // Remaches superiores
        //----------------------------------------

        for (int i = 0; i < 6; i++) {

            Circle rivet = new Circle(
                    w * 0.22 + i * w * 0.09,
                    y + h * 0.18,
                    1.1
            );

            rivet.setFill(Color.rgb(255, 255, 255, 0.30));

            ship.getChildren().add(rivet);
        }

        //----------------------------------------
        // Remaches inferiores
        //----------------------------------------

        for (int i = 0; i < 6; i++) {

            Circle rivet = new Circle(
                    w * 0.22 + i * w * 0.09,
                    y + h * 0.82,
                    1.1
            );

            rivet.setFill(Color.rgb(0, 0, 0, 0.25));

            ship.getChildren().add(rivet);
        }

        //----------------------------------------
        // Línea superior
        //----------------------------------------

        Line top = new Line(
                w * 0.22,
                y + h * 0.28,
                w * 0.78,
                y + h * 0.28
        );

        top.setStroke(Color.rgb(255, 255, 255, 0.12));

        //----------------------------------------
        // Línea inferior
        //----------------------------------------

        Line bottom = new Line(
                w * 0.22,
                y + h * 0.72,
                w * 0.78,
                y + h * 0.72
        );

        bottom.setStroke(Color.rgb(0, 0, 0, 0.15));

        ship.getChildren().addAll(
                hatch,
                top,
                bottom
        );
    }
    /**
     * Renders specular highlights, soft edge shadows, central reflections, and bow/stern caps.
     *
     * @param ship     Parent {@link Group} container.
     * @param cellSize Cell dimensions for layout calculations.
     */
    private static void buildHighlights(Group ship, double cellSize) {

        double w = SHIP_LENGTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2.0;

        //----------------------------------------
        // Brillo superior
        //----------------------------------------

        Polyline highlight = new Polyline(

                w * 0.25, y + h * 0.22,

                w * 0.42, y + h * 0.18,

                w * 0.60, y + h * 0.18,

                w * 0.78, y + h * 0.22

        );

        highlight.setStroke(Color.rgb(255, 255, 255, 0.20));
        highlight.setStrokeWidth(2);
        highlight.setFill(null);

        //----------------------------------------
        // Sombra inferior
        //----------------------------------------

        Polyline shadow = new Polyline(

                w * 0.25, y + h * 0.78,

                w * 0.42, y + h * 0.82,

                w * 0.60, y + h * 0.82,

                w * 0.78, y + h * 0.78

        );

        shadow.setStroke(Color.rgb(0, 0, 0, 0.18));
        shadow.setStrokeWidth(2);
        shadow.setFill(null);

        //----------------------------------------
        // Reflejo del puente
        //----------------------------------------

        Line bridgeHighlight = new Line(

                w * 0.36,
                y + h * 0.24,

                w * 0.36,
                y + h * 0.52

        );

        bridgeHighlight.setStroke(Color.rgb(255, 255, 255, 0.25));
        bridgeHighlight.setStrokeWidth(1.2);

        //----------------------------------------
        // Reflejo del radar
        //----------------------------------------

        Line radarHighlight = new Line(

                w * 0.43,
                y - h * 0.08,

                w * 0.53,
                y - h * 0.08

        );

        radarHighlight.setStroke(Color.rgb(255, 255, 255, 0.35));

        //----------------------------------------
        // Brillo del cañón
        //----------------------------------------

        Line gunHighlight = new Line(

                w * 0.22,
                y + h * 0.48,

                w * 0.10,
                y + h * 0.48

        );

        gunHighlight.setStroke(Color.rgb(255, 255, 255, 0.30));

        //----------------------------------------
        // Brillo central
        //----------------------------------------

        Ellipse reflection = new Ellipse(

                w * 0.50,
                y + h * 0.40,

                w * 0.14,
                h * 0.07

        );

        reflection.setFill(Color.rgb(255, 255, 255, 0.08));

        //----------------------------------------
        // Remate de proa
        //----------------------------------------

        Circle bow = new Circle(

                w * 0.06,
                y + h * 0.50,
                cellSize * 0.010

        );

        bow.setFill(Color.rgb(255, 255, 255, 0.35));

        //----------------------------------------
        // Remate de popa
        //----------------------------------------

        Circle stern = new Circle(

                w * 0.94,
                y + h * 0.50,
                cellSize * 0.010

        );

        stern.setFill(Color.rgb(0, 0, 0, 0.25));

        //----------------------------------------

        ship.getChildren().addAll(
                highlight,
                shadow,
                bridgeHighlight,
                radarHighlight,
                gunHighlight,
                reflection,
                bow,
                stern
        );

    }
    /**
     * Constructs a soft, blurred shadow underneath the vessel hull.
     *
     * @param ship     Parent {@link Group} container.
     * @param cellSize Cell dimensions for layout calculations.
     */
    private static void buildShadow(Group ship, double cellSize) {

        double w = SHIP_LENGTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2.0;

        Ellipse shadow = new Ellipse(
                w / 2,
                y + h / 2 + cellSize * 0.06,
                w * 0.42,
                h * 0.34
        );

        shadow.setFill(Color.rgb(0, 0, 0, 0.18));

        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(6);

        shadow.setEffect(blur);

        ship.getChildren().add(shadow);
    }
}