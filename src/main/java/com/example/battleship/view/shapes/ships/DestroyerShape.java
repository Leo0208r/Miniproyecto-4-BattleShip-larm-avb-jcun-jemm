package com.example.battleship.view.shapes.ships;

import com.example.battleship.view.shapes.util.Colors;
import com.example.battleship.view.shapes.util.Gradients;
import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public final class DestroyerShape {

    private static final int SHIP_LENGTH_CELLS = 2;
    private static final double SHIP_HEIGHT_FACTOR = 0.72;

    private DestroyerShape(){}

    public static Group create(double cellSize){

        Group ship = new Group();

        buildShadow(ship, cellSize);
        buildHull(ship, cellSize);
        buildBridge(ship, cellSize);
        buildWeapons(ship, cellSize);
        buildDetails(ship, cellSize);
        buildHighlights(ship, cellSize);

        return ship;
    }

    private static void buildBridge(Group ship, double cellSize){

        double w = SHIP_LENGTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2.0;

        //----------------------------------------
        // Superestructura principal
        //----------------------------------------

        Rectangle bridge = new Rectangle(
                w * 0.34,
                y + h * 0.22,
                w * 0.28,
                h * 0.42
        );

        bridge.setArcWidth(8);
        bridge.setArcHeight(8);

        bridge.setFill(Gradients.tower());
        bridge.setStroke(Colors.BORDER);

        //----------------------------------------
        // Puente superior
        //----------------------------------------

        Rectangle bridgeTop = new Rectangle(
                w * 0.39,
                y + h * 0.12,
                w * 0.18,
                h * 0.16
        );

        bridgeTop.setArcWidth(6);
        bridgeTop.setArcHeight(6);

        bridgeTop.setFill(Color.web("#5E666D"));

        //----------------------------------------
        // Ventanas
        //----------------------------------------

        for(int i = 0; i < 3; i++){

            Rectangle window = new Rectangle(
                    w * (0.40 + i * 0.05),
                    y + h * 0.18,
                    w * 0.03,
                    h * 0.04
            );

            window.setFill(Color.web("#BFD9F2"));

            ship.getChildren().add(window);
        }

        //----------------------------------------
        // Mástil
        //----------------------------------------

        Line mast = new Line(
                w * 0.48,
                y + h * 0.12,
                w * 0.48,
                y - h * 0.18
        );

        mast.setStroke(Color.DARKGRAY);
        mast.setStrokeWidth(2);

        //----------------------------------------
        // Radar horizontal
        //----------------------------------------

        Line radar = new Line(
                w * 0.42,
                y - h * 0.08,
                w * 0.54,
                y - h * 0.08
        );

        radar.setStroke(Color.SILVER);
        radar.setStrokeWidth(2);

        //----------------------------------------
        // Antenas
        //----------------------------------------

        Line antennaLeft = new Line(
                w * 0.46,
                y - h * 0.18,
                w * 0.43,
                y - h * 0.30
        );

        antennaLeft.setStroke(Color.GRAY);

        Line antennaRight = new Line(
                w * 0.50,
                y - h * 0.18,
                w * 0.53,
                y - h * 0.30
        );

        antennaRight.setStroke(Color.GRAY);

        ship.getChildren().addAll(
                bridge,
                bridgeTop,
                mast,
                radar,
                antennaLeft,
                antennaRight
        );

    }
    private static void buildWeapons(Group ship, double cellSize){

        double w = SHIP_LENGTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2.0;

        //----------------------------------------
        // Cañón principal
        //----------------------------------------

        Circle turret = new Circle(
                w * 0.18,
                y + h * 0.50,
                cellSize * 0.08
        );

        turret.setFill(Color.web("#555D63"));
        turret.setStroke(Colors.BORDER);

        Line barrel = new Line(
                w * 0.18,
                y + h * 0.50,
                w * 0.05,
                y + h * 0.50
        );

        barrel.setStroke(Color.BLACK);
        barrel.setStrokeWidth(3);

        //----------------------------------------
        // Sistema VLS
        //----------------------------------------

        for(int row = 0; row < 2; row++){

            for(int col = 0; col < 4; col++){

                Rectangle cell = new Rectangle(
                        w * (0.30 + col * 0.055),
                        y + h * (0.36 + row * 0.09),
                        w * 0.035,
                        h * 0.05
                );

                cell.setFill(Color.web("#3F454A"));
                cell.setStroke(Color.BLACK);

                ship.getChildren().add(cell);
            }

        }

        //----------------------------------------
        // CIWS
        //----------------------------------------

        Circle ciws = new Circle(
                w * 0.78,
                y + h * 0.50,
                cellSize * 0.05
        );

        ciws.setFill(Color.DARKGRAY);
        ciws.setStroke(Color.BLACK);

        Line ciwsGun = new Line(
                w * 0.78,
                y + h * 0.50,
                w * 0.86,
                y + h * 0.50
        );

        ciwsGun.setStroke(Color.BLACK);
        ciwsGun.setStrokeWidth(2);

        //----------------------------------------
        // Lanzadores de misiles
        //----------------------------------------

        Rectangle launcherLeft = new Rectangle(
                w * 0.58,
                y + h * 0.28,
                w * 0.08,
                h * 0.08
        );

        launcherLeft.setRotate(-18);

        launcherLeft.setFill(Color.web("#676F75"));

        Rectangle launcherRight = new Rectangle(
                w * 0.58,
                y + h * 0.64,
                w * 0.08,
                h * 0.08
        );

        launcherRight.setRotate(18);

        launcherRight.setFill(Color.web("#676F75"));

        ship.getChildren().addAll(
                turret,
                barrel,
                ciws,
                ciwsGun,
                launcherLeft,
                launcherRight
        );

    }
    private static void buildDetails(Group ship, double cellSize){

        double w = SHIP_LENGTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2.0;

        //----------------------------------------
        // Paneles del casco
        //----------------------------------------

        for(int i = 1; i <= 4; i++){

            Line panel = new Line(
                    w * (0.20 + i * 0.12),
                    y + h * 0.18,
                    w * (0.20 + i * 0.12),
                    y + h * 0.82
            );

            panel.setStroke(Color.rgb(255,255,255,0.10));
            panel.setStrokeWidth(1);

            ship.getChildren().add(panel);
        }

        //----------------------------------------
        // Escotillas
        //----------------------------------------

        for(int i = 0; i < 2; i++){

            Circle hatch = new Circle(
                    w * (0.38 + i * 0.16),
                    y + h * 0.68,
                    cellSize * 0.04
            );

            hatch.setFill(Color.web("#747C82"));
            hatch.setStroke(Color.BLACK);

            ship.getChildren().add(hatch);
        }

        //----------------------------------------
        // Remaches superiores
        //----------------------------------------

        for(int i = 0; i < 10; i++){

            Circle rivet = new Circle(
                    w * 0.15 + i * w * 0.07,
                    y + h * 0.16,
                    1.2
            );

            rivet.setFill(Color.rgb(255,255,255,0.30));

            ship.getChildren().add(rivet);
        }

        //----------------------------------------
        // Remaches inferiores
        //----------------------------------------

        for(int i = 0; i < 10; i++){

            Circle rivet = new Circle(
                    w * 0.15 + i * w * 0.07,
                    y + h * 0.84,
                    1.2
            );

            rivet.setFill(Color.rgb(0,0,0,0.25));

            ship.getChildren().add(rivet);
        }

        //----------------------------------------
        // Ventilaciones
        //----------------------------------------

        for(int i = 0; i < 3; i++){

            Rectangle vent = new Rectangle(
                    w * (0.34 + i * 0.10),
                    y + h * 0.28,
                    w * 0.03,
                    h * 0.06
            );

            vent.setArcWidth(3);
            vent.setArcHeight(3);

            vent.setFill(Color.SILVER);

            ship.getChildren().add(vent);
        }

        //----------------------------------------
        // Líneas de mantenimiento
        //----------------------------------------

        Line top = new Line(
                w * 0.18,
                y + h * 0.30,
                w * 0.82,
                y + h * 0.30
        );

        top.setStroke(Color.rgb(255,255,255,0.12));

        Line bottom = new Line(
                w * 0.18,
                y + h * 0.70,
                w * 0.82,
                y + h * 0.70
        );

        bottom.setStroke(Color.rgb(0,0,0,0.15));

        ship.getChildren().addAll(
                top,
                bottom
        );

    }
    private static void buildHighlights(Group ship, double cellSize){

        double w = SHIP_LENGTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2.0;

        //----------------------------------------
        // Brillo superior del casco
        //----------------------------------------

        Polyline highlight = new Polyline(

                w * 0.18, y + h * 0.22,

                w * 0.35, y + h * 0.18,

                w * 0.65, y + h * 0.18,

                w * 0.82, y + h * 0.22

        );

        highlight.setStroke(Color.rgb(255,255,255,0.18));
        highlight.setStrokeWidth(2);
        highlight.setFill(null);

        //----------------------------------------
        // Sombra inferior
        //----------------------------------------

        Polyline shadow = new Polyline(

                w * 0.18, y + h * 0.78,

                w * 0.35, y + h * 0.82,

                w * 0.65, y + h * 0.82,

                w * 0.82, y + h * 0.78

        );

        shadow.setStroke(Color.rgb(0,0,0,0.18));
        shadow.setStrokeWidth(2);
        shadow.setFill(null);

        //----------------------------------------
        // Reflejo del puente
        //----------------------------------------

        Line bridgeHighlight = new Line(

                w * 0.36,
                y + h * 0.26,

                w * 0.36,
                y + h * 0.60

        );

        bridgeHighlight.setStroke(Color.rgb(255,255,255,0.20));
        bridgeHighlight.setStrokeWidth(1.5);

        //----------------------------------------
        // Reflejo del radar
        //----------------------------------------

        Line radarHighlight = new Line(

                w * 0.43,
                y - h * 0.08,

                w * 0.53,
                y - h * 0.08

        );

        radarHighlight.setStroke(Color.rgb(255,255,255,0.35));

        //----------------------------------------
        // Brillo del cañón
        //----------------------------------------

        Line gunHighlight = new Line(

                w * 0.18,
                y + h * 0.48,

                w * 0.07,
                y + h * 0.48

        );

        gunHighlight.setStroke(Color.rgb(255,255,255,0.30));

        //----------------------------------------
        // Remate de proa
        //----------------------------------------

        Circle bow = new Circle(

                w * 0.02,
                y + h * 0.50,
                cellSize * 0.012

        );

        bow.setFill(Color.rgb(255,255,255,0.35));

        //----------------------------------------
        // Remate de popa
        //----------------------------------------

        Circle stern = new Circle(

                w * 0.98,
                y + h * 0.50,
                cellSize * 0.012

        );

        stern.setFill(Color.rgb(0,0,0,0.25));

        //----------------------------------------

        ship.getChildren().addAll(
                highlight,
                shadow,
                bridgeHighlight,
                radarHighlight,
                gunHighlight,
                bow,
                stern
        );

    }
    private static void buildShadow(Group ship, double cellSize){

        double w = SHIP_LENGTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2.0;

        Ellipse shadow = new Ellipse(
                w / 2,
                y + h / 2 + cellSize * 0.08,
                w * 0.46,
                h * 0.40
        );

        shadow.setFill(Color.rgb(0,0,0,0.18));
        shadow.setEffect(new GaussianBlur(8));

        ship.getChildren().add(shadow);
    }
    private static void buildHull(Group ship, double cellSize){

        double w = SHIP_LENGTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2.0;

        Polygon hull = new Polygon(

                0.0, y + h * 0.50,

                w * 0.18, y + h * 0.12,

                w * 0.82, y + h * 0.12,

                w, y + h * 0.50,

                w * 0.82, y + h * 0.88,

                w * 0.18, y + h * 0.88

        );

        hull.setFill(Gradients.destroyerHull());
        hull.setStroke(Colors.BORDER);
        hull.setStrokeWidth(2);

        ship.getChildren().add(hull);
    }
}