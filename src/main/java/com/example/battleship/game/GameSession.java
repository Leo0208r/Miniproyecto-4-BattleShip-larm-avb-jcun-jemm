package com.example.battleship.game;

import com.example.battleship.model.players.HumanPlayer;
import com.example.battleship.persistence.GameSaver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * Singleton class managing global game sessions and persistence operations in Battleship.
 * <p>
 * Responsible for creating new game instances, loading and saving active match states,
 * managing persistent file paths, and ensuring a single active game session across scenes.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public final class GameSession {
    /** Path to the binary saved game file. */
    private static final Path SAVE_FILE = Paths.get("saves", "game.dat");
    /** Path to the text summary save file. */
    private static final Path SUMMARY_FILE = Paths.get("saves", "summary.txt");
    /** Single instance of {@code GameSession}. */
    private static GameSession instance;
    /** Currently active {@link GameManager} instance. */
    private GameManager currentGameManager;
    /**
     * Private constructor to enforce Singleton design pattern.
     */
    private GameSession() {
    }
    /**
     * Retrieves the thread-safe singleton instance of {@code GameSession}.
     *
     * @return The global {@code GameSession} instance.
     */
    public static synchronized GameSession getInstance() {
        if (instance == null) {
            instance = new GameSession();
        }
        return instance;
    }
    /**
     * Gets the current active game manager instance.
     *
     * @return The active {@link GameManager}, or {@code null} if no session is active.
     */
    public GameManager getCurrentGameManager() {
        return currentGameManager;
    }
    /**
     * Gets the file path location for binary match saves.
     *
     * @return The {@link Path} to the main save file.
     */
    public Path getSaveFile() {
        return SAVE_FILE;
    }
    /**
     * Gets the file path location for summary match text files.
     *
     * @return The {@link Path} to the summary save file.
     */
    public Path getSummaryFile() {
        return SUMMARY_FILE;
    }
    /**
     * Checks if a valid binary save file exists on disk.
     *
     * @return {@code true} if the save file exists; {@code false} otherwise.
     */
    public boolean hasSavedGame() {
        return Files.exists(SAVE_FILE);
    }
    /**
     * Initializes a new match session with the given nickname.
     * <p>
     * Defaults player nickname to "Player1" if blank or null, instantiates a new
     * {@link GameManager}, and automatically triggers fleet placement for the AI.
     * </p>
     *
     * @param nickname Player's preferred display nickname.
     */
    public void startNewGame(String nickname) {
        String effectiveNickname = (nickname == null || nickname.isBlank()) ? "Player1" : nickname.trim();
        currentGameManager = new GameManager(effectiveNickname);
        currentGameManager.getMachine().placeFleet();
    }
    /**
     * Attempts to deserialize and load the latest save file from disk.
     *
     * @return {@code true} if match data was loaded successfully; {@code false} otherwise.
     */
    public boolean loadLatestSave() {
        if (!hasSavedGame()) {
            currentGameManager = null;
            return false;
        }

        try {
            currentGameManager = GameSaver.load(SAVE_FILE);
            return currentGameManager != null;
        } catch (Exception e) {
            currentGameManager = null;
            System.err.println("No se pudo cargar la partida guardada: " + e.getMessage());
            return false;
        }
    }
    /**
     * Persists the current game session state and summary to disk.
     */
    public void saveCurrentGame() {
        if (currentGameManager == null) {
            return;
        }

        try {
            GameSaver.save(currentGameManager, SAVE_FILE);
            GameSaver.saveSummary(currentGameManager, SUMMARY_FILE);
        } catch (IOException e) {
            System.err.println("No se pudo guardar la partida: " + e.getMessage());
        }
    }
    /**
     * Deletes existing save and summary files from disk upon game completion or reset.
     */
    public void clearSavedGame() {
        try {
            Files.deleteIfExists(SAVE_FILE);
            Files.deleteIfExists(SUMMARY_FILE);
        } catch (IOException e) {
            System.err.println("No se pudo borrar la partida guardada: " + e.getMessage());
        }
    }
    /**
     * Gets the current human player instance from active game manager.
     *
     * @return The {@link HumanPlayer} reference, or {@code null} if no manager is active.
     */
    public HumanPlayer getHumanPlayer() {
        return currentGameManager == null ? null : currentGameManager.getHuman();
    }
    /**
     * Ensures an active game manager instance exists, creating a new default one if necessary.
     *
     * @return A non-null {@link GameManager} instance.
     */
    public GameManager requireGameManager() {
        if (currentGameManager == null) {
            startNewGame("Player1");
        }
        return currentGameManager;
    }
}
