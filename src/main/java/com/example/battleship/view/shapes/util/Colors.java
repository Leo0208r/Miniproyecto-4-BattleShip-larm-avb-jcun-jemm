package com.example.battleship.view.shapes.util;

import javafx.scene.paint.Color;
/**
 * Utility color palette defining color constants for ship 2D vector rendering in JavaFX.
 * <p>
 * Contains structured color palettes for ship hulls, superstructures, decks, flight decks,
 * navigation windows, and auxiliary details across the game view.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public final class Colors {
    /**
     * Private constructor to prevent instantiation of static color palette utility.
     */
    private Colors() {}

    // =========================
    // Hull
    // =========================
    /** Highlighting color for top-facing hull boundaries and light reflections. */
    public static final Color HULL_LIGHT = Color.web("#A9B4BC");
    /** Base mid-tone color for main ship hull surfaces. */
    public static final Color HULL = Color.web("#7F8B93");
    /** Shading color for lower hull boundaries and recessed structural areas. */
    public static final Color HULL_DARK = Color.web("#4E5961");
    /** Deep shadow tone for bottom hull contours and occlusion effects. */
    public static final Color HULL_SHADOW = Color.web("#2F3940");

    // =========================
    // Deck
    // =========================
    /** Bright surface tone for elevated deck elements and raised walkways. */
    public static final Color DECK_LIGHT = Color.web("#A2A9AF");
    /** Standard color tone for main weather decks and horizontal surfaces. */
    public static final Color DECK = Color.web("#7A8288");
    /** Shading tone for recessed deck areas and lower walkways. */
    public static final Color DECK_DARK = Color.web("#5A6268");

    // =========================
    // Tower
    // =========================
    /** Standard color for bridge towers and command structures. */
    public static final Color TOWER = Color.web("#59636A");
    /** Highlighting tone for upper bridge decks and illuminated superstructure edges. */
    public static final Color TOWER_LIGHT = Color.web("#7C878E");
    /** Dark shadow color for shaded tower sides and lower bridge recesses. */
    public static final Color TOWER_DARK = Color.web("#3E474D");

    // =========================
    // Windows
    // =========================
    /** Primary bright blue tone for illuminated navigation and bridge windows. */
    public static final Color WINDOW = Color.web("#8FD8FF");
    /** Darker blue-tinted tone for shaded side windows and glass reflections. */
    public static final Color WINDOW_DARK = Color.web("#4FA8D5");

    // =========================
    // Runway
    // =========================
    /** Dark asphalt color for aircraft carrier flight decks and landing strips. */
    public static final Color RUNWAY = Color.web("#656D73");
    /** Bright white paint color for runway centerlines and touchdown markers. */
    public static final Color RUNWAY_LINE = Color.WHITE;
    /** High-visibility yellow paint color for taxiway guidelines and hazard markings. */
    public static final Color TAXI_LINE = Color.web("#F5D442");

    // =========================
    // Details
    // =========================
    /** Metallic light color for rotating radar dishes and sensor domes. */
    public static final Color RADAR = Color.web("#D9E0E4");
    /** Dark metallic tone for radio masts, whip antennas, and rigging. */
    public static final Color ANTENNA = Color.web("#20262B");
    /** Dark outline color for component stroke borders and structural separation lines. */
    public static final Color BORDER = Color.web("#253036");

}