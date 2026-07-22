package com.example.battleship.view.shapes.util;

import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public final class Gradients {

    private Gradients(){}

    /**
     * Hull metallic gradient.
     */
    public static LinearGradient hull(){

        return new LinearGradient(
                0,
                0,
                0,
                1,
                true,
                CycleMethod.NO_CYCLE,

                new Stop[]{
                        new Stop(0.00, Colors.HULL_LIGHT),
                        new Stop(0.18, Colors.HULL),
                        new Stop(0.50, Colors.HULL_DARK),
                        new Stop(0.82, Colors.HULL),
                        new Stop(1.00, Colors.HULL_SHADOW)
                }
        );

    }

    /**
     * Deck metallic gradient.
     */
    public static LinearGradient deck(){

        return new LinearGradient(
                0,
                0,
                1,
                0,
                true,
                CycleMethod.NO_CYCLE,

                new Stop[]{
                        new Stop(0.0, Colors.DECK_LIGHT),
                        new Stop(0.5, Colors.DECK),
                        new Stop(1.0, Colors.DECK_DARK)
                }
        );

    }

    /**
     * Tower gradient.
     */
    public static LinearGradient tower(){

        return new LinearGradient(
                0,
                0,
                0,
                1,
                true,
                CycleMethod.NO_CYCLE,

                new Stop[]{
                        new Stop(0.0, Colors.TOWER_LIGHT),
                        new Stop(0.5, Colors.TOWER),
                        new Stop(1.0, Colors.TOWER_DARK)
                }
        );

    }
    public static LinearGradient submarineHull() {

        return new LinearGradient(
                0,
                0,
                0,
                1,
                true,
                CycleMethod.NO_CYCLE,

                new Stop[]{
                        new Stop(0.0, Colors.HULL_LIGHT),
                        new Stop(0.35, Colors.HULL),
                        new Stop(0.70, Colors.HULL_DARK),
                        new Stop(1.0, Colors.BORDER)
                }
        );

    }
    public static LinearGradient destroyerHull(){

        return new LinearGradient(
                0,
                0,
                0,
                1,
                true,
                CycleMethod.NO_CYCLE,

                new Stop[]{
                        new Stop(0.0, Colors.HULL_LIGHT),
                        new Stop(0.5, Colors.HULL),
                        new Stop(1.0, Colors.HULL_DARK)
                }

        );

    }

}