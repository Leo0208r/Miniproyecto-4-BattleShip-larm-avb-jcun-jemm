package com.example.battleship.view.shapes.ships;

import com.example.battleship.view.shapes.util.Colors;
import com.example.battleship.view.shapes.util.Gradients;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public final class AircraftCarrierShape {
    private static final double SHIP_WIDTH_CELLS = 4.0;
    private static final double SHIP_HEIGHT_FACTOR = 0.95;


    private AircraftCarrierShape() {
    }

    public static Group create(double cellSize) {

        Group ship = new Group();

        buildHull(ship, cellSize);
        buildDeck(ship, cellSize);
        buildIsland(ship, cellSize);
        buildRunway(ship, cellSize);
        buildDetails(ship, cellSize);

        ship.setEffect(new DropShadow(5, Color.gray(0, 0.45)));

        return ship;
    }

    private static void buildHull(Group ship, double cellSize) {
        double w = SHIP_WIDTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;

        double y = (cellSize - h) / 2.0;

        //----------------------------------------
        // Casco exterior
        //----------------------------------------

        Path outerHull = new Path(

                new MoveTo(0, y + h * 0.22),

                new LineTo(w * 0.18, y),

                new LineTo(w * 0.82, y),

                new QuadCurveTo(
                        w * 0.96,
                        y + h * 0.12,
                        w,
                        y + h * 0.50
                ),

                new QuadCurveTo(
                        w * 0.96,
                        y + h * 0.88,
                        w * 0.82,
                        y + h
                ),

                new LineTo(w * 0.18, y + h),

                new LineTo(0, y + h * 0.78),

                new ClosePath()

        );

        outerHull.setFill(Gradients.hull());

        outerHull.setStroke(Colors.BORDER);

        outerHull.setStrokeWidth(2);

        //----------------------------------------
        // Casco interior
        //----------------------------------------

        Path innerHull = new Path(

                new MoveTo(w * 0.05, y + h * 0.26),

                new LineTo(w * 0.22, y + h * 0.07),

                new LineTo(w * 0.78, y + h * 0.07),

                new QuadCurveTo(
                        w * 0.90,
                        y + h * 0.18,
                        w * 0.94,
                        y + h * 0.50
                ),

                new QuadCurveTo(
                        w * 0.90,
                        y + h * 0.82,
                        w * 0.78,
                        y + h * 0.93
                ),

                new LineTo(w * 0.22, y + h * 0.93),

                new LineTo(w * 0.05, y + h * 0.74),

                new ClosePath()

        );

        innerHull.setFill(Color.rgb(255,255,255,0.08));

        innerHull.setStroke(Color.rgb(255,255,255,0.18));

        //----------------------------------------
        // Refuerzo de proa
        //----------------------------------------

        Polygon bow = new Polygon(

                w * 0.80, y + h * 0.12,

                w * 0.98, y + h * 0.50,

                w * 0.80, y + h * 0.88,

                w * 0.73, y + h * 0.50

        );

        bow.setFill(Color.rgb(255,255,255,0.15));

        //----------------------------------------
        // Línea superior
        //----------------------------------------

        Line topEdge = new Line(

                w * 0.20,
                y + h * 0.03,

                w * 0.79,
                y + h * 0.03

        );

        topEdge.setStroke(Color.rgb(255,255,255,0.35));

        topEdge.setStrokeWidth(1.5);

        //----------------------------------------
        // Línea inferior
        //----------------------------------------

        Line bottomEdge = new Line(

                w * 0.20,
                y + h * 0.97,

                w * 0.79,
                y + h * 0.97

        );

        bottomEdge.setStroke(Color.rgb(0,0,0,0.25));

        bottomEdge.setStrokeWidth(2);

        //----------------------------------------
        // Sombra interior
        //----------------------------------------

        InnerShadow shadow = new InnerShadow();

        shadow.setRadius(8);

        shadow.setColor(Color.rgb(0,0,0,0.35));

        outerHull.setEffect(shadow);

        //----------------------------------------
        // Agregar al barco
        //----------------------------------------

        ship.getChildren().addAll(
                outerHull,
                innerHull,
                bow,
                topEdge,
                bottomEdge
        );
    }

    private static void buildDeck(Group ship, double cellSize) {
        double w = SHIP_WIDTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2.0;

        //----------------------------------------
        // Cubierta principal
        //----------------------------------------

        Path deck = new Path(

                new MoveTo(w * 0.10, y + h * 0.18),

                new LineTo(w * 0.24, y + h * 0.08),

                new LineTo(w * 0.77, y + h * 0.08),

                new QuadCurveTo(
                        w * 0.90,
                        y + h * 0.18,
                        w * 0.93,
                        y + h * 0.50
                ),

                new QuadCurveTo(
                        w * 0.90,
                        y + h * 0.82,
                        w * 0.77,
                        y + h * 0.92
                ),

                new LineTo(w * 0.24, y + h * 0.92),

                new LineTo(w * 0.10, y + h * 0.82),

                new ClosePath()

        );

        deck.setFill(Gradients.deck());
        deck.setStroke(Color.rgb(255,255,255,0.15));
        deck.setStrokeWidth(1.2);

        //----------------------------------------
        // Segunda cubierta
        //----------------------------------------

        Rectangle centerDeck = new Rectangle(
                w * 0.20,
                y + h * 0.23,
                w * 0.55,
                h * 0.54
        );

        centerDeck.setArcWidth(12);
        centerDeck.setArcHeight(12);

        centerDeck.setFill(Color.rgb(255,255,255,0.08));

        //----------------------------------------
        // Zona de despegue
        //----------------------------------------

        Rectangle runway = new Rectangle(
                w * 0.08,
                y + h * 0.43,
                w * 0.80,
                h * 0.14
        );

        runway.setArcWidth(6);
        runway.setArcHeight(6);

        runway.setFill(Colors.RUNWAY);

        //----------------------------------------
        // Líneas blancas centrales
        //----------------------------------------

        for(int i=0;i<8;i++){

            Rectangle stripe = new Rectangle(

                    w*0.15 + i*w*0.08,

                    y+h*0.485,

                    w*0.04,

                    h*0.018

            );

            stripe.setFill(Colors.RUNWAY_LINE);

            ship.getChildren().add(stripe);

        }

        //----------------------------------------
        // Línea amarilla
        //----------------------------------------

        Polyline taxi = new Polyline(

                w*0.20, y+h*0.30,

                w*0.33, y+h*0.30,

                w*0.44, y+h*0.50,

                w*0.72, y+h*0.50

        );

        taxi.setStroke(Colors.TAXI_LINE);
        taxi.setStrokeWidth(2.5);
        taxi.setFill(null);

        //----------------------------------------
        // Ascensor delantero
        //----------------------------------------

        Rectangle elevator1 = new Rectangle(

                w*0.28,

                y+h*0.10,

                w*0.10,

                h*0.12

        );

        elevator1.setFill(Color.rgb(210,210,210,0.35));

        elevator1.setStroke(Colors.BORDER);

        //----------------------------------------
        // Ascensor trasero
        //----------------------------------------

        Rectangle elevator2 = new Rectangle(

                w*0.58,

                y+h*0.78,

                w*0.10,

                h*0.12

        );

        elevator2.setFill(Color.rgb(210,210,210,0.35));

        elevator2.setStroke(Colors.BORDER);

        //----------------------------------------

        ship.getChildren().addAll(
                deck,
                centerDeck,
                runway,
                taxi,
                elevator1,
                elevator2
        );
    }

    private static void buildIsland(Group ship, double cellSize) {
        double w = SHIP_WIDTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2.0;

        //----------------------------------------
        // Base de la isla
        //----------------------------------------

        Rectangle base = new Rectangle(
                w * 0.62,
                y + h * 0.20,
                w * 0.13,
                h * 0.45
        );

        base.setArcWidth(6);
        base.setArcHeight(6);
        base.setFill(Gradients.tower());
        base.setStroke(Colors.BORDER);

        //----------------------------------------
        // Segundo nivel
        //----------------------------------------

        Rectangle level2 = new Rectangle(
                w * 0.60,
                y + h * 0.14,
                w * 0.10,
                h * 0.14
        );

        level2.setArcWidth(5);
        level2.setArcHeight(5);
        level2.setFill(Colors.TOWER_LIGHT);
        level2.setStroke(Colors.BORDER);

        //----------------------------------------
        // Puente de mando
        //----------------------------------------

        Rectangle bridge = new Rectangle(
                w * 0.58,
                y + h * 0.08,
                w * 0.12,
                h * 0.08
        );

        bridge.setArcWidth(5);
        bridge.setArcHeight(5);
        bridge.setFill(Color.web("#90989E"));
        bridge.setStroke(Colors.BORDER);

        //----------------------------------------
        // Chimenea
        //----------------------------------------

        Rectangle chimney = new Rectangle(
                w * 0.69,
                y + h * 0.16,
                w * 0.035,
                h * 0.18
        );

        chimney.setFill(Colors.TOWER_DARK);

        //----------------------------------------
        // Mástil principal
        //----------------------------------------

        Line mast = new Line(
                w * 0.65,
                y + h * 0.05,
                w * 0.65,
                y - h * 0.12
        );

        mast.setStroke(Colors.ANTENNA);
        mast.setStrokeWidth(2);

        //----------------------------------------
        // Radar horizontal
        //----------------------------------------

        Line radar = new Line(
                w * 0.61,
                y - h * 0.07,
                w * 0.69,
                y - h * 0.07
        );

        radar.setStroke(Colors.RADAR);
        radar.setStrokeWidth(2);

        //----------------------------------------
        // Antenas
        //----------------------------------------

        Line antenna1 = new Line(
                w * 0.65,
                y - h * 0.07,
                w * 0.61,
                y - h * 0.11
        );

        antenna1.setStroke(Colors.ANTENNA);

        Line antenna2 = new Line(
                w * 0.65,
                y - h * 0.07,
                w * 0.69,
                y - h * 0.11
        );

        antenna2.setStroke(Colors.ANTENNA);

        //----------------------------------------
        // Ventanas del puente
        //----------------------------------------

        for (int i = 0; i < 5; i++) {

            Rectangle window = new Rectangle(
                    w * 0.595 + i * w * 0.018,
                    y + h * 0.105,
                    w * 0.010,
                    h * 0.020
            );

            window.setFill(Colors.WINDOW);

            ship.getChildren().add(window);
        }

        //----------------------------------------
        // Plataforma radar
        //----------------------------------------

        Rectangle platform = new Rectangle(
                w * 0.635,
                y - h * 0.01,
                w * 0.03,
                h * 0.02
        );

        platform.setFill(Color.LIGHTGRAY);

        //----------------------------------------

        ship.getChildren().addAll(
                base,
                level2,
                bridge,
                chimney,
                platform,
                mast,
                radar,
                antenna1,
                antenna2
        );
    }

    private static void buildRunway(Group ship, double cellSize) {
        double w = SHIP_WIDTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2.0;

        //----------------------------------------
        // Línea central discontinua
        //----------------------------------------

        for (int i = 0; i < 10; i++) {

            Rectangle stripe = new Rectangle(
                    w * 0.12 + i * w * 0.07,
                    y + h * 0.485,
                    w * 0.035,
                    h * 0.015
            );

            stripe.setFill(Color.WHITE);

            ship.getChildren().add(stripe);
        }

        //----------------------------------------
        // Línea amarilla de rodaje
        //----------------------------------------

        Polyline taxiLine = new Polyline(

                w * 0.18, y + h * 0.32,

                w * 0.35, y + h * 0.32,

                w * 0.48, y + h * 0.50,

                w * 0.72, y + h * 0.50

        );

        taxiLine.setStroke(Color.GOLD);
        taxiLine.setStrokeWidth(2.5);
        taxiLine.setFill(null);

        //----------------------------------------
        // Marcas de aterrizaje
        //----------------------------------------

        for (int i = 0; i < 5; i++) {

            Rectangle markLeft = new Rectangle(
                    w * 0.25,
                    y + h * (0.28 + i * 0.11),
                    w * 0.05,
                    h * 0.01
            );

            Rectangle markRight = new Rectangle(
                    w * 0.60,
                    y + h * (0.28 + i * 0.11),
                    w * 0.05,
                    h * 0.01
            );

            markLeft.setFill(Color.WHITE);
            markRight.setFill(Color.WHITE);

            ship.getChildren().addAll(markLeft, markRight);
        }

        //----------------------------------------
        // Flecha de despegue
        //----------------------------------------

        Polygon arrow = new Polygon(

                w * 0.82, y + h * 0.50,

                w * 0.76, y + h * 0.46,

                w * 0.76, y + h * 0.48,

                w * 0.68, y + h * 0.48,

                w * 0.68, y + h * 0.52,

                w * 0.76, y + h * 0.52,

                w * 0.76, y + h * 0.54

        );

        arrow.setFill(Color.WHITE);

        //----------------------------------------
        // Zona de apontaje
        //----------------------------------------

        Rectangle landingZone = new Rectangle(

                w * 0.14,

                y + h * 0.39,

                w * 0.48,

                h * 0.22

        );

        landingZone.setFill(Color.rgb(255,255,255,0.04));
        landingZone.setStroke(Color.rgb(255,255,255,0.10));

        //----------------------------------------
        // Luces de pista
        //----------------------------------------

        for(int i=0;i<12;i++){

            Circle lightLeft = new Circle(

                    w*0.10,

                    y+h*0.18+i*h*0.055,

                    1.8

            );

            Circle lightRight = new Circle(

                    w*0.88,

                    y+h*0.18+i*h*0.055,

                    1.8

            );

            lightLeft.setFill(Color.LIGHTGREEN);
            lightRight.setFill(Color.LIGHTGREEN);

            ship.getChildren().addAll(lightLeft, lightRight);

        }

        //----------------------------------------

        ship.getChildren().addAll(
                landingZone,
                taxiLine,
                arrow
        );
    }

    private static void buildDetails(Group ship, double cellSize) {
        double w = SHIP_WIDTH_CELLS * cellSize;
        double h = SHIP_HEIGHT_FACTOR * cellSize;
        double y = (cellSize - h) / 2.0;

        //----------------------------------------
        // Paneles de la cubierta
        //----------------------------------------

        for (int i = 0; i < 7; i++) {

            Line panel = new Line(
                    w * (0.18 + i * 0.08),
                    y + h * 0.18,
                    w * (0.18 + i * 0.08),
                    y + h * 0.82
            );

            panel.setStroke(Color.rgb(255,255,255,0.08));
            panel.setStrokeWidth(1);

            ship.getChildren().add(panel);
        }

        //----------------------------------------
        // Barandillas superiores
        //----------------------------------------

        Line railTop = new Line(
                w * 0.14,
                y + h * 0.16,
                w * 0.82,
                y + h * 0.16
        );

        railTop.setStroke(Color.LIGHTGRAY);
        railTop.setStrokeWidth(1);

        Line railBottom = new Line(
                w * 0.14,
                y + h * 0.84,
                w * 0.82,
                y + h * 0.84
        );

        railBottom.setStroke(Color.LIGHTGRAY);
        railBottom.setStrokeWidth(1);

        //----------------------------------------
        // CIWS delantero
        //----------------------------------------

        Circle ciwsFront = new Circle(
                w * 0.20,
                y + h * 0.28,
                cellSize * 0.05
        );

        ciwsFront.setFill(Color.DARKGRAY);
        ciwsFront.setStroke(Color.BLACK);

        Line gun1 = new Line(
                w * 0.20,
                y + h * 0.28,
                w * 0.24,
                y + h * 0.28
        );

        gun1.setStroke(Color.BLACK);

        //----------------------------------------
        // CIWS trasero
        //----------------------------------------

        Circle ciwsRear = new Circle(
                w * 0.74,
                y + h * 0.72,
                cellSize * 0.05
        );

        ciwsRear.setFill(Color.DARKGRAY);
        ciwsRear.setStroke(Color.BLACK);

        Line gun2 = new Line(
                w * 0.74,
                y + h * 0.72,
                w * 0.78,
                y + h * 0.72
        );

        gun2.setStroke(Color.BLACK);

        //----------------------------------------
        // Escotillas
        //----------------------------------------

        for (int i = 0; i < 4; i++) {

            Rectangle hatch = new Rectangle(
                    w * (0.30 + i * 0.10),
                    y + h * 0.62,
                    w * 0.04,
                    h * 0.05
            );

            hatch.setFill(Color.GRAY);
            hatch.setStroke(Color.DARKGRAY);

            ship.getChildren().add(hatch);
        }

        //----------------------------------------
        // Remaches simulados
        //----------------------------------------

        for (int i = 0; i < 18; i++) {

            Circle rivetTop = new Circle(
                    w * 0.16 + i * w * 0.035,
                    y + h * 0.14,
                    0.8
            );

            Circle rivetBottom = new Circle(
                    w * 0.16 + i * w * 0.035,
                    y + h * 0.86,
                    0.8
            );

            rivetTop.setFill(Color.rgb(255,255,255,0.30));
            rivetBottom.setFill(Color.rgb(0,0,0,0.25));

            ship.getChildren().addAll(rivetTop, rivetBottom);
        }

        //----------------------------------------
        // Ventilaciones
        //----------------------------------------

        for (int i = 0; i < 5; i++) {

            Rectangle vent = new Rectangle(
                    w * (0.22 + i * 0.09),
                    y + h * 0.36,
                    w * 0.025,
                    h * 0.05
            );

            vent.setArcWidth(3);
            vent.setArcHeight(3);

            vent.setFill(Color.SILVER);

            ship.getChildren().add(vent);
        }

        //----------------------------------------
        // Brillos metálicos
        //----------------------------------------

        Line shine1 = new Line(
                w * 0.16,
                y + h * 0.22,
                w * 0.78,
                y + h * 0.22
        );

        shine1.setStroke(Color.rgb(255,255,255,0.18));

        Line shine2 = new Line(
                w * 0.16,
                y + h * 0.78,
                w * 0.78,
                y + h * 0.78
        );

        shine2.setStroke(Color.rgb(255,255,255,0.12));

        //----------------------------------------

        ship.getChildren().addAll(
                railTop,
                railBottom,
                ciwsFront,
                gun1,
                ciwsRear,
                gun2,
                shine1,
                shine2
        );
    }
    private static double shipWidth(double cellSize) {
        return SHIP_WIDTH_CELLS * cellSize;
    }

    private static double shipHeight(double cellSize) {
        return SHIP_HEIGHT_FACTOR * cellSize;
    }

}