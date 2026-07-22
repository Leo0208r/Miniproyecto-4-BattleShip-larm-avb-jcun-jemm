package com.example.battleship.view.shapes.ships;

import com.example.battleship.view.shapes.util.Colors;
import com.example.battleship.view.shapes.util.Gradients;
import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public final class SubmarineShape {

    private static final int SHIP_LENGTH_CELLS = 3;
    private static final double SHIP_HEIGHT_FACTOR = 0.70;

    private SubmarineShape() {
    }

    public static Group create(double cellSize) {

        Group ship = new Group();

        buildShadow(ship, cellSize);
        buildHull(ship, cellSize);
        buildDeck(ship, cellSize);
        buildTower(ship, cellSize);
        buildPropeller(ship, cellSize);
        buildDetails(ship, cellSize);
        buildHighlights(ship, cellSize);

        return ship;
    }

    private static void buildHull(Group ship, double cellSize) {

        double w = SHIP_LENGTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2;

        Ellipse hull = new Ellipse(
                w / 2,
                y + h / 2,
                w * 0.48,
                h * 0.46
        );

        hull.setFill(Gradients.submarineHull());
        hull.setStroke(Colors.BORDER);
        hull.setStrokeWidth(2);

        ship.getChildren().add(hull);
    }

    private static void buildDeck(Group ship, double cellSize) {

        double w = SHIP_LENGTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2;

        Ellipse deck = new Ellipse(
                w / 2,
                y + h / 2,
                w * 0.40,
                h * 0.30
        );

        deck.setFill(Color.rgb(255, 255, 255, 0.05));
        deck.setStroke(Color.rgb(255, 255, 255, 0.10));

        ship.getChildren().add(deck);

        Line center = new Line(
                w * 0.15,
                y + h / 2,
                w * 0.85,
                y + h / 2
        );

        center.setStroke(Color.rgb(255, 255, 255, 0.15));

        ship.getChildren().add(center);
    }

    private static void buildTower(Group ship, double cellSize) {

        double w = SHIP_LENGTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2;

        //----------------------------------------
        // Base de la torre
        //----------------------------------------

        Rectangle tower = new Rectangle(
                w * 0.43,
                y - h * 0.18,
                w * 0.14,
                h * 0.48
        );

        tower.setArcWidth(12);
        tower.setArcHeight(12);

        tower.setFill(Gradients.tower());
        tower.setStroke(Colors.BORDER);
        tower.setStrokeWidth(1.5);

        //----------------------------------------
        // Parte superior de la torre
        //----------------------------------------

        Rectangle bridge = new Rectangle(
                w * 0.455,
                y - h * 0.28,
                w * 0.09,
                h * 0.15
        );

        bridge.setArcWidth(8);
        bridge.setArcHeight(8);

        bridge.setFill(Color.web("#5D676E"));
        bridge.setStroke(Color.rgb(255, 255, 255, 0.12));

        //----------------------------------------
        // Periscopio principal
        //----------------------------------------

        Line periscope = new Line(
                w * 0.50,
                y - h * 0.28,
                w * 0.50,
                y - h * 0.52
        );

        periscope.setStroke(Color.web("#BFC7CD"));
        periscope.setStrokeWidth(2);

        //----------------------------------------
        // Cabeza del periscopio
        //----------------------------------------

        Line head = new Line(
                w * 0.50,
                y - h * 0.52,
                w * 0.55,
                y - h * 0.52
        );

        head.setStroke(Color.web("#BFC7CD"));
        head.setStrokeWidth(2);

        //----------------------------------------
        // Antena izquierda
        //----------------------------------------

        Line antennaLeft = new Line(
                w * 0.47,
                y - h * 0.24,
                w * 0.44,
                y - h * 0.42
        );

        antennaLeft.setStroke(Color.web("#90979D"));
        antennaLeft.setStrokeWidth(1.2);

        //----------------------------------------
        // Antena derecha
        //----------------------------------------

        Line antennaRight = new Line(
                w * 0.53,
                y - h * 0.24,
                w * 0.56,
                y - h * 0.40
        );

        antennaRight.setStroke(Color.web("#90979D"));
        antennaRight.setStrokeWidth(1.2);

        //----------------------------------------
        // Ventanas
        //----------------------------------------

        for (int i = 0; i < 3; i++) {

            Circle window = new Circle(
                    w * 0.50,
                    y - h * 0.10 + i * h * 0.12,
                    1.8
            );

            window.setFill(Color.web("#D8E7F2"));

            ship.getChildren().add(window);
        }

        ship.getChildren().addAll(
                tower,
                bridge,
                periscope,
                head,
                antennaLeft,
                antennaRight
        );
    }

    private static void buildPropeller(Group ship, double cellSize) {

        double w = SHIP_LENGTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2;

        //----------------------------------------
        // Eje de la hélice
        //----------------------------------------

        Line shaft = new Line(
                w * 0.95,
                y + h * 0.50,
                w * 1.01,
                y + h * 0.50
        );

        shaft.setStroke(Color.DARKGRAY);
        shaft.setStrokeWidth(2);

        //----------------------------------------
        // Centro de la hélice
        //----------------------------------------

        Circle hub = new Circle(
                w * 1.02,
                y + h * 0.50,
                cellSize * 0.025
        );

        hub.setFill(Color.GOLD);
        hub.setStroke(Color.DARKGOLDENROD);

        //----------------------------------------
        // Aspa superior
        //----------------------------------------

        Polygon bladeTop = new Polygon(
                w * 1.02, y + h * 0.47,
                w * 1.06, y + h * 0.42,
                w * 1.04, y + h * 0.49
        );

        bladeTop.setFill(Color.GOLDENROD);

        //----------------------------------------
        // Aspa inferior
        //----------------------------------------

        Polygon bladeBottom = new Polygon(
                w * 1.02, y + h * 0.53,
                w * 1.06, y + h * 0.58,
                w * 1.04, y + h * 0.51
        );

        bladeBottom.setFill(Color.GOLDENROD);

        //----------------------------------------
        // Aspa izquierda
        //----------------------------------------

        Polygon bladeLeft = new Polygon(
                w * 0.99, y + h * 0.50,
                w * 0.95, y + h * 0.46,
                w * 0.97, y + h * 0.50
        );

        bladeLeft.setFill(Color.GOLDENROD);

        //----------------------------------------
        // Timón superior
        //----------------------------------------

        Polygon rudderTop = new Polygon(
                w * 0.92, y + h * 0.32,
                w * 0.98, y + h * 0.40,
                w * 0.92, y + h * 0.44
        );

        rudderTop.setFill(Color.web("#555D63"));

        //----------------------------------------
        // Timón inferior
        //----------------------------------------

        Polygon rudderBottom = new Polygon(
                w * 0.92, y + h * 0.68,
                w * 0.98, y + h * 0.60,
                w * 0.92, y + h * 0.56
        );

        rudderBottom.setFill(Color.web("#555D63"));

        ship.getChildren().addAll(
                shaft,
                hub,
                bladeTop,
                bladeBottom,
                bladeLeft,
                rudderTop,
                rudderBottom
        );
    }
    private static void buildDetails(Group ship, double cellSize) {

        double w = SHIP_LENGTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2;

        //----------------------------------------
        // Paneles del casco
        //----------------------------------------

        for (int i = 1; i <= 5; i++) {

            Line panel = new Line(
                    w * (0.18 + i * 0.11),
                    y + h * 0.24,
                    w * (0.18 + i * 0.11),
                    y + h * 0.76
            );

            panel.setStroke(Color.rgb(255,255,255,0.10));
            panel.setStrokeWidth(1);

            ship.getChildren().add(panel);
        }

        //----------------------------------------
        // Escotillas
        //----------------------------------------

        for (int i = 0; i < 3; i++) {

            Circle hatch = new Circle(
                    w * (0.34 + i * 0.14),
                    y + h * 0.50,
                    cellSize * 0.04
            );

            hatch.setFill(Color.web("#7A8288"));
            hatch.setStroke(Color.web("#4A4F54"));
            hatch.setStrokeWidth(1);

            ship.getChildren().add(hatch);
        }

        //----------------------------------------
        // Remaches superiores
        //----------------------------------------

        for (int i = 0; i < 12; i++) {

            Circle rivet = new Circle(
                    w * 0.15 + i * w * 0.06,
                    y + h * 0.22,
                    1.2
            );

            rivet.setFill(Color.rgb(255,255,255,0.30));

            ship.getChildren().add(rivet);
        }

        //----------------------------------------
        // Remaches inferiores
        //----------------------------------------

        for (int i = 0; i < 12; i++) {

            Circle rivet = new Circle(
                    w * 0.15 + i * w * 0.06,
                    y + h * 0.78,
                    1.2
            );

            rivet.setFill(Color.rgb(0,0,0,0.25));

            ship.getChildren().add(rivet);
        }

        //----------------------------------------
        // Respiraderos
        //----------------------------------------

        for (int i = 0; i < 4; i++) {

            Rectangle vent = new Rectangle(
                    w * (0.28 + i * 0.12),
                    y + h * 0.34,
                    w * 0.025,
                    h * 0.08
            );

            vent.setArcWidth(3);
            vent.setArcHeight(3);

            vent.setFill(Color.web("#A4ADB3"));

            ship.getChildren().add(vent);
        }

        //----------------------------------------
        // Sonar frontal
        //----------------------------------------

        Circle sonar = new Circle(
                w * 0.08,
                y + h * 0.50,
                cellSize * 0.035
        );

        sonar.setFill(Color.web("#5A6166"));
        sonar.setStroke(Color.BLACK);

        //----------------------------------------
        // Puerto trasero
        //----------------------------------------

        Rectangle rearAccess = new Rectangle(
                w * 0.82,
                y + h * 0.43,
                w * 0.05,
                h * 0.14
        );

        rearAccess.setArcWidth(4);
        rearAccess.setArcHeight(4);

        rearAccess.setFill(Color.web("#6B737A"));
        rearAccess.setStroke(Color.BLACK);

        //----------------------------------------
        // Líneas de mantenimiento
        //----------------------------------------

        Line top = new Line(
                w * 0.18,
                y + h * 0.30,
                w * 0.82,
                y + h * 0.30
        );

        top.setStroke(Color.rgb(255,255,255,0.10));

        Line bottom = new Line(
                w * 0.18,
                y + h * 0.70,
                w * 0.82,
                y + h * 0.70
        );

        bottom.setStroke(Color.rgb(0,0,0,0.15));

        //----------------------------------------

        ship.getChildren().addAll(
                sonar,
                rearAccess,
                top,
                bottom
        );
    }
    private static void buildHighlights(Group ship, double cellSize) {

        double w = SHIP_LENGTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2;

        //----------------------------------------
        // Brillo principal
        //----------------------------------------

        Arc highlight = new Arc(
                w / 2,
                y + h / 2,
                w * 0.42,
                h * 0.34,
                25,
                130
        );

        highlight.setType(ArcType.OPEN);
        highlight.setFill(null);
        highlight.setStroke(Color.rgb(255,255,255,0.18));
        highlight.setStrokeWidth(2);

        //----------------------------------------
        // Sombra inferior
        //----------------------------------------

        Arc shadow = new Arc(
                w / 2,
                y + h / 2,
                w * 0.42,
                h * 0.34,
                205,
                130
        );

        shadow.setType(ArcType.OPEN);
        shadow.setFill(null);
        shadow.setStroke(Color.rgb(0,0,0,0.18));
        shadow.setStrokeWidth(2);

        //----------------------------------------
        // Brillo de la torre
        //----------------------------------------

        Line towerHighlight = new Line(
                w * 0.47,
                y - h * 0.15,
                w * 0.47,
                y + h * 0.18
        );

        towerHighlight.setStroke(Color.rgb(255,255,255,0.20));
        towerHighlight.setStrokeWidth(1.2);

        //----------------------------------------
        // Brillo del puente
        //----------------------------------------

        Line bridgeHighlight = new Line(
                w * 0.46,
                y - h * 0.24,
                w * 0.54,
                y - h * 0.24
        );

        bridgeHighlight.setStroke(Color.rgb(255,255,255,0.25));
        bridgeHighlight.setStrokeWidth(1);

        //----------------------------------------
        // Reflejo sobre el casco
        //----------------------------------------

        Ellipse reflection = new Ellipse(
                w * 0.42,
                y + h * 0.36,
                w * 0.16,
                h * 0.08
        );

        reflection.setFill(Color.rgb(255,255,255,0.05));

        //----------------------------------------
        // Remate de la proa
        //----------------------------------------

        Circle nose = new Circle(
                w * 0.03,
                y + h * 0.50,
                cellSize * 0.012
        );

        nose.setFill(Color.rgb(255,255,255,0.35));

        //----------------------------------------
        // Remate de la popa
        //----------------------------------------

        Circle tail = new Circle(
                w * 0.97,
                y + h * 0.50,
                cellSize * 0.012
        );

        tail.setFill(Color.rgb(0,0,0,0.25));

        //----------------------------------------

        ship.getChildren().addAll(
                reflection,
                highlight,
                shadow,
                towerHighlight,
                bridgeHighlight,
                nose,
                tail
        );

    }
    private static void buildShadow(Group ship, double cellSize) {

        double w = SHIP_LENGTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2.0;

        Ellipse shadow = new Ellipse(
                w / 2,
                y + h / 2 + cellSize * 0.08,
                w * 0.48,
                h * 0.42
        );

        shadow.setFill(Color.rgb(0, 0, 0, 0.18));

        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(8);

        shadow.setEffect(blur);

        ship.getChildren().add(shadow);
    }
}