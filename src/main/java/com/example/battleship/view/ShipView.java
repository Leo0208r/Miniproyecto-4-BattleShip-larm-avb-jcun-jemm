package com.example.battleship.view;

import com.example.battleship.model.Coordinate;
import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.ships.Ship;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * 2D visual representation of a ship, drawn with JavaFX Shapes.
 * Each ship size (1-4) has its own distinct silhouette and details,
 * based on approved concept sketches: a frigate with a deck gun and
 * missile grid, a destroyer with an angled superstructure, a
 * submarine with a rounded hull and conning towers, and an aircraft
 * carrier with a flight deck, island and parked planes.
 * Ships are built in a "horizontal" local coordinate space
 * (x: 0..length, y: 0..thickness) and rotated 90 degrees for
 * vertical placement, so each ship only needs to be designed once.
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

        Group content = switch (size) {
            case 1 -> buildFrigate(length, thickness);
            case 2 -> buildDestroyer(length, thickness);
            case 3 -> buildSubmarine(length, thickness);
            case 4 -> buildCarrier(length, thickness);
            default -> throw new IllegalArgumentException("Unsupported ship size: " + size);
        };

        Group oriented = orient(content, thickness, orientation);
        oriented.setLayoutX(MARGIN);
        oriented.setLayoutY(MARGIN);

        getChildren().add(oriented);
        getStyleClass().add("ship-view");
        if (size == 3) {
            getStyleClass().add("submarine");
        }
    }

    /**
     * Rotates the horizontally-built content 90 degrees for vertical
     * ships, so every ship design only has to be authored once.
     */
    private Group orient(Group content, double thickness, Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL) {
            return content;
        }
        Group wrapper = new Group(content);
        wrapper.getTransforms().addAll(new Rotate(90, 0, 0), new Translate(thickness, 0));
        return wrapper;
    }

    // ----------------------------------------------------------------
    // Frigate (size 1): pointed bow, deck gun near the tip, a wide
    // central deckhouse with radar mast, and a 2-cell missile grid
    // at the stern.
    // ----------------------------------------------------------------
    private Group buildFrigate(double length, double thickness) {
        Group group = new Group();

        Polygon hull = new Polygon(
                0.067 * length, 0.5 * thickness,
                0.267 * length, 0.1 * thickness,
                0.933 * length, 0.1 * thickness,
                0.933 * length, 0.9 * thickness,
                0.267 * length, 0.9 * thickness
        );
        hull.getStyleClass().add("ship-hull");

        Circle gun = new Circle(0.227 * length, 0.5 * thickness, 0.073 * length);
        gun.getStyleClass().add("ship-detail-dark");
        Circle gunBarrel = new Circle(0.227 * length, 0.5 * thickness, 0.027 * length);
        gunBarrel.getStyleClass().add("ship-detail-darker");

        Rectangle deckhouse = new Rectangle(0.44 * length, 0.32 * thickness, 0.307 * length, 0.36 * thickness);
        deckhouse.setArcWidth(6);
        deckhouse.setArcHeight(6);
        deckhouse.getStyleClass().add("ship-detail");

        Circle radar = new Circle(0.593 * length, 0.5 * thickness, 0.06 * length);
        radar.getStyleClass().add("ship-detail-light");

        Line mast = new Line(0.593 * length, 0.32 * thickness, 0.593 * length, 0.227 * thickness);
        mast.getStyleClass().add("ship-mast");

        Rectangle gridTop = new Rectangle(0.787 * length, 0.393 * thickness, 0.107 * length, 0.093 * thickness);
        Rectangle gridBottom = new Rectangle(0.787 * length, 0.513 * thickness, 0.107 * length, 0.093 * thickness);
        gridTop.getStyleClass().add("ship-detail-dark");
        gridBottom.getStyleClass().add("ship-detail-dark");

        group.getChildren().addAll(hull, gun, gunBarrel, deckhouse, radar, mast, gridTop, gridBottom);
        return group;
    }

    // ----------------------------------------------------------------
    // Destroyer (size 2): pointed bow, forward gun, a 2x2 missile
    // grid, a wide trapezoid superstructure (narrow side forward)
    // with a hexagon bridge window and two aligned sensor domes,
    // and a second trapezoid with a turret near the stern.
    // ----------------------------------------------------------------
    private Group buildDestroyer(double length, double thickness) {
        Group group = new Group();

        Polygon hull = buildTaperedHull(length, thickness, 0.183);

        Circle gun = new Circle(0.2 * length, 0.5 * thickness, 0.08 * thickness);
        gun.getStyleClass().add("ship-detail-dark");
        Circle gunBarrel = new Circle(0.2 * length, 0.5 * thickness, 0.03 * thickness);
        gunBarrel.getStyleClass().add("ship-detail-darker");

        Rectangle sq1 = new Rectangle(0.253 * length, 0.387 * thickness, 0.047 * length, 0.08 * thickness);
        Rectangle sq2 = new Rectangle(0.253 * length, 0.533 * thickness, 0.047 * length, 0.08 * thickness);
        Rectangle sq3 = new Rectangle(0.313 * length, 0.387 * thickness, 0.047 * length, 0.08 * thickness);
        Rectangle sq4 = new Rectangle(0.313 * length, 0.533 * thickness, 0.047 * length, 0.08 * thickness);
        for (Rectangle r : new Rectangle[]{sq1, sq2, sq3, sq4}) {
            r.getStyleClass().add("ship-detail-dark");
        }

        Polygon midTrapezoid = new Polygon(
                0.443 * length, 0.367 * thickness,
                0.683 * length, 0.2 * thickness,
                0.683 * length, 0.8 * thickness,
                0.443 * length, 0.633 * thickness
        );
        midTrapezoid.getStyleClass().add("ship-detail");

        Polygon window = new Polygon(
                0.497 * length, 0.427 * thickness,
                0.573 * length, 0.4 * thickness,
                0.587 * length, 0.5 * thickness,
                0.573 * length, 0.587 * thickness,
                0.497 * length, 0.573 * thickness,
                0.48 * length, 0.5 * thickness
        );
        window.getStyleClass().add("ship-window");

        Circle domeTop = new Circle(0.65 * length, 0.367 * thickness, 0.043 * thickness);
        Circle domeBottom = new Circle(0.65 * length, 0.633 * thickness, 0.043 * thickness);
        domeTop.getStyleClass().add("ship-detail-light");
        domeBottom.getStyleClass().add("ship-detail-light");

        Polygon aftTrapezoid = new Polygon(
                0.74 * length, 0.233 * thickness,
                0.907 * length, 0.3 * thickness,
                0.907 * length, 0.7 * thickness,
                0.74 * length, 0.767 * thickness
        );
        aftTrapezoid.getStyleClass().add("ship-detail");

        Circle turret = new Circle(0.823 * length, 0.5 * thickness, 0.05 * thickness);
        turret.getStyleClass().add("ship-detail-dark");

        Rectangle aftBlock = new Rectangle(0.923 * length, 0.38 * thickness, 0.047 * length, 0.24 * thickness);
        aftBlock.getStyleClass().add("ship-detail");

        group.getChildren().addAll(hull, gun, gunBarrel, sq1, sq2, sq3, sq4,
                midTrapezoid, window, domeTop, domeBottom, aftTrapezoid, turret, aftBlock);
        return group;
    }

    // ----------------------------------------------------------------
    // Submarine (size 3): rounded ellipse hull, a small elongated
    // tower followed by a larger tower with a periscope and masts,
    // and rivet dots grouped in 2x2 clusters fore and aft.
    // ----------------------------------------------------------------
    private Group buildSubmarine(double length, double thickness) {
        Group group = new Group();

        Ellipse hull = new Ellipse(0.5 * length, 0.5 * thickness, 0.444 * length, 0.28 * thickness);
        hull.getStyleClass().add("ship-hull");

        Rectangle smallTower = new Rectangle(0.422 * length, 0.4 * thickness, 0.111 * length, 0.2 * thickness);
        smallTower.setArcWidth(8);
        smallTower.setArcHeight(8);
        smallTower.getStyleClass().add("ship-detail");

        Rectangle bigTower = new Rectangle(0.556 * length, 0.267 * thickness, 0.156 * length, 0.467 * thickness);
        bigTower.setArcWidth(14);
        bigTower.setArcHeight(14);
        bigTower.getStyleClass().add("ship-detail");

        Circle periscopeOuter = new Circle(0.633 * length, 0.5 * thickness, 0.12 * thickness);
        periscopeOuter.getStyleClass().add("ship-detail-light");
        Circle periscopeInner = new Circle(0.633 * length, 0.5 * thickness, 0.047 * thickness);
        periscopeInner.getStyleClass().add("ship-window");

        Line mast1 = new Line(0.6 * length, 0.267 * thickness, 0.6 * length, 0.12 * thickness);
        Line mast2 = new Line(0.667 * length, 0.267 * thickness, 0.667 * length, 0.16 * thickness);
        mast1.getStyleClass().add("ship-mast");
        mast2.getStyleClass().add("ship-mast");

        group.getChildren().addAll(hull, smallTower, bigTower, periscopeOuter, periscopeInner, mast1, mast2);

        double[][] dotGroupsX = {{0.122, 0.162}, {0.218, 0.258}, {0.313, 0.353}, {0.8, 0.84}};
        for (double[] xs : dotGroupsX) {
            for (double fx : xs) {
                for (double fy : new double[]{0.433, 0.567}) {
                    Circle dot = new Circle(fx * length, fy * thickness, 0.027 * thickness);
                    dot.getStyleClass().add("ship-rivet");
                    group.getChildren().add(dot);
                }
            }
        }
        return group;
    }

    // ----------------------------------------------------------------
    // Aircraft carrier (size 4): flat angular flight deck, a dashed
    // centerline, three elevator platforms, an offset island with
    // radar mast, diagonal parking stripes, and parked/scattered
    // aircraft silhouettes.
    // ----------------------------------------------------------------
    private Group buildCarrier(double length, double thickness) {
        Group group = new Group();

        Polygon hull = buildTaperedHull(length, thickness, 0.083);
        hull.getStyleClass().add("ship-hull");

        Line centerline = new Line(0.717 * length, 0.5 * thickness, 0.03 * length, 0.5 * thickness);
        centerline.getStyleClass().add("ship-marking");

        Rectangle elev1 = new Rectangle(0.108 * length, 0.653 * thickness, 0.043 * length, 0.173 * thickness);
        Rectangle elev2 = new Rectangle(0.168 * length, 0.653 * thickness, 0.043 * length, 0.173 * thickness);
        Rectangle elev3 = new Rectangle(0.228 * length, 0.653 * thickness, 0.043 * length, 0.173 * thickness);
        for (Rectangle r : new Rectangle[]{elev1, elev2, elev3}) {
            r.setArcWidth(4);
            r.setArcHeight(4);
            r.getStyleClass().add("ship-detail");
        }

        Rectangle island = new Rectangle(0.783 * length, 0.08 * thickness, 0.1 * length, 0.6 * thickness);
        island.setArcWidth(6);
        island.setArcHeight(6);
        island.getStyleClass().add("ship-detail");

        Rectangle window1 = new Rectangle(0.8 * length, 0.147 * thickness, 0.027 * length, 0.067 * thickness);
        Rectangle window2 = new Rectangle(0.833 * length, 0.147 * thickness, 0.027 * length, 0.067 * thickness);
        window1.getStyleClass().add("ship-window");
        window2.getStyleClass().add("ship-window");

        Circle radar = new Circle(0.833 * length, 0.347 * thickness, 0.053 * thickness);
        radar.getStyleClass().add("ship-detail-light");
        Circle domeLeft = new Circle(0.803 * length, 0.467 * thickness, 0.027 * thickness);
        Circle domeRight = new Circle(0.863 * length, 0.467 * thickness, 0.027 * thickness);
        domeLeft.getStyleClass().add("ship-detail-light");
        domeRight.getStyleClass().add("ship-detail-light");

        Line mast = new Line(0.833 * length, 0.08 * thickness, 0.833 * length, -0.027 * thickness);
        mast.getStyleClass().add("ship-mast");

        group.getChildren().addAll(hull, centerline, elev1, elev2, elev3, island,
                window1, window2, radar, domeLeft, domeRight, mast);

        double[][] stripes = {
                {0.417, 0.833, 0.467, 0.5},
                {0.533, 0.833, 0.583, 0.5},
                {0.65, 0.833, 0.7, 0.5}
        };
        for (double[] s : stripes) {
            Line stripe = new Line(s[0] * length, s[1] * thickness, s[2] * length, s[3] * thickness);
            stripe.getStyleClass().add("ship-marking");
            group.getChildren().add(stripe);
        }

        double planeScale = thickness / 120.0;
        addPlane(group, 0.35 * length, 0.327 * thickness, 0, planeScale);
        addPlane(group, 0.5 * length, 0.36 * thickness, 0, planeScale);
        addPlane(group, 0.658 * length, 0.327 * thickness, 0, planeScale);
        addPlane(group, 0.345 * length, 0.633 * thickness, -59, planeScale);
        addPlane(group, 0.5 * length, 0.633 * thickness, -59, planeScale);
        addPlane(group, 0.617 * length, 0.633 * thickness, -59, planeScale);
        addPlane(group, 0.733 * length, 0.633 * thickness, -59, planeScale);
        return group;
    }

    // ----------------------------------------------------------------
    // Shared helpers
    // ----------------------------------------------------------------

    /** Generic pointed-bow hull, with a configurable bow taper fraction of the length. */
    private Polygon buildTaperedHull(double length, double thickness, double taperFraction) {
        double taper = taperFraction * length;
        Polygon polygon = new Polygon(
                0.0, thickness / 2,
                taper, 0.0,
                length, 0.0,
                length, thickness,
                taper, thickness
        );
        polygon.getStyleClass().add("ship-hull");
        return polygon;
    }

    /** Small plane silhouette, centered at (cx, cy) and rotated by the given angle in degrees. */
    /** Small plane silhouette, centered at (cx, cy), rotated and scaled to fit the ship's actual size. */
    private void addPlane(Group parent, double cx, double cy, double angleDegrees, double scale) {
        Polygon plane = new Polygon(
                -5 * scale, -4 * scale,
                11 * scale, -9 * scale,
                5 * scale, 0,
                11 * scale, 9 * scale,
                -5 * scale, 4 * scale,
                -17 * scale, 0
        );
        plane.getStyleClass().add("ship-plane");
        plane.setLayoutX(cx);
        plane.setLayoutY(cy);
        plane.setRotate(angleDegrees);
        parent.getChildren().add(plane);
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