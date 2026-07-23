package com.example.battleship.view.shapes.util;

import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
/**
 * Utility factory for constructing pre-configured JavaFX {@link LinearGradient} paints.
 * <p>
 * Provides vertical and horizontal color gradients used for rendering metallic vessel hulls,
 * decks, superstructure towers, submarines, and light combatants with depth and lighting effects.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public final class Gradients {
    /**
     * Private constructor to prevent instantiation of static gradient utility.
     */
    private Gradients(){}
    /**
     * Creates a vertical metallic linear gradient for standard vessel hulls.
     * <p>
     * Features a multi-stop color progression transition from top highlights down to lower edge shadows.
     * </p>
     *
     * @return {@link LinearGradient} configured for primary ship hulls.
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
     * Creates a horizontal metallic linear gradient for deck surfaces.
     * <p>
     * Transitions horizontally from lighter deck tones to darker shaded deck areas.
     * </p>
     *
     * @return {@link LinearGradient} configured for horizontal deck overlays.
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
     * Creates a vertical linear gradient for superstructure towers and command bridges.
     * <p>
     * Simulates downward light decay from roof edges to tower foundations.
     * </p>
     *
     * @return {@link LinearGradient} configured for bridge towers.
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
    /**
     * Creates a vertical linear gradient optimized for rounded submarine hulls.
     * <p>
     * Fades smoothly from upper hull specular tones into deep aquatic border shadows.
     * </p>
     *
     * @return {@link LinearGradient} configured for submarine bodies.
     */
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
    /**
     * Creates a simplified 3-stop vertical linear gradient for compact vessel hulls such as destroyers and frigates.
     *
     * @return {@link LinearGradient} configured for light combatant hulls.
     */
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