package com.example.battleship.persistence;

import com.example.battleship.game.GameManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
/**
 * Utility class for managing game persistence operations in Battleship.
 * <p>
 * Handles serialization and deserialization of {@link GameManager} state instances
 * to and from disk, and exports lightweight plain text match summaries.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class GameSaver {
    /**
     * Serializes and saves the complete game state to a specified file.
     *
     * @param state The current {@link GameManager} instance to serialize.
     * @param file  The destination {@link Path} where the save file will be written.
     * @throws IOException If an I/O error occurs during directory creation or file writing.
     */
    public static void save(GameManager state, Path file) throws IOException{
        Files.createDirectories(file.getParent());
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(file)))){
            out.writeObject(state);
        }
    }
    /**
     * Deserializes and loads a previously saved game match state from a file.
     * <p>
     * Re-initializes transient fields by invoking {@link GameManager#afterLoad()} after loading.
     * </p>

     * @param file The {@link Path} pointing to the saved game file.
     * @return The restored {@link GameManager} instance.
     * @throws GamePersistenceException If the save file cannot be read or class casting fails.
     */
    public static GameManager load(Path file) throws GamePersistenceException {
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(file)))){
            Object o = in.readObject();
            GameManager manager = (GameManager) o;
            manager.afterLoad();
            return manager;
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new GamePersistenceException("No se pudo leer la partida guardada", e);
        }
    }
    /**
     * Saves a small plain text summary of the current match state.
     * <p>
     * Writes player nickname, human sunk ships count, and machine sunk ships count in CSV format.
     * </p>
     *
     * @param state The active {@link GameManager} match state.
     * @param file  The target {@link Path} file for saving the text summary.
     * @throws IOException If an I/O error occurs during file writing.
     */
    public static void saveSummary(GameManager state, Path file) throws IOException{
        Files.createDirectories(file.getParent());
        String line = String.format("%s,%d,%d", state.getNickname(), state.getHuman().getBoard().getFleet().getSunkShipsCount(), state.getMachine().getBoard().getFleet().getSunkShipsCount());
        Files.write(file, List.of(line), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}



