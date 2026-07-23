package com.example.battleship.model.enums;
/**
 * Represents the AI player's active shooting strategy mode in Battleship.
 * <p>
 * Controls whether the AI is searching randomly across the grid (Hunt mode)
 * or systematically targeting surrounding cells after landing a successful hit (Target mode).
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public enum ShootingMode {
    /** Search mode where the AI picks random or strategic candidate coordinates. */
    HUNT("Hunt"),
    /** Target mode activated upon hitting a ship, focusing shots on adjacent cells. */
    TARGET("Target");
    /** Descriptive string symbol representing the shooting mode. */
    private final  String symbol;
    /**
     * Constructs a {@code ShootingMode} with its associated text symbol.
     *
     * @param symbol Descriptive text string for the shooting mode.
     */
    ShootingMode(String symbol) {
        this.symbol = symbol;
    }
    /**
     * Gets the text symbol representation of the shooting mode.
     *
     * @return String description of the shooting mode.
     */
    public String getSymbol() {
        return symbol;
    }
}
