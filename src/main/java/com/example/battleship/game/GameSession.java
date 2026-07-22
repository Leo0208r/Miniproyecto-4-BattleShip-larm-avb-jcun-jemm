package com.example.battleship.game;

import com.example.battleship.model.players.HumanPlayer;
import com.example.battleship.persistence.GameSaver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class GameSession {
    private static final Path SAVE_FILE = Paths.get("saves", "game.dat");
    private static final Path SUMMARY_FILE = Paths.get("saves", "summary.txt");

    private static GameSession instance;
    private GameManager currentGameManager;

    private GameSession() {
    }

    public static synchronized GameSession getInstance() {
        if (instance == null) {
            instance = new GameSession();
        }
        return instance;
    }

    public GameManager getCurrentGameManager() {
        return currentGameManager;
    }

    public Path getSaveFile() {
        return SAVE_FILE;
    }

    public Path getSummaryFile() {
        return SUMMARY_FILE;
    }

    public boolean hasSavedGame() {
        return Files.exists(SAVE_FILE);
    }

    public void startNewGame(String nickname) {
        String effectiveNickname = (nickname == null || nickname.isBlank()) ? "Player1" : nickname.trim();
        currentGameManager = new GameManager(effectiveNickname);
        currentGameManager.getMachine().placeFleet();
    }

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

    public void clearSavedGame() {
        try {
            Files.deleteIfExists(SAVE_FILE);
            Files.deleteIfExists(SUMMARY_FILE);
        } catch (IOException e) {
            System.err.println("No se pudo borrar la partida guardada: " + e.getMessage());
        }
    }

    public HumanPlayer getHumanPlayer() {
        return currentGameManager == null ? null : currentGameManager.getHuman();
    }

    public GameManager requireGameManager() {
        if (currentGameManager == null) {
            startNewGame("Player1");
        }
        return currentGameManager;
    }
}
