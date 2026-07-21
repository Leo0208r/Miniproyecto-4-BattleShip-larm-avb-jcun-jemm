package com.example.battleship.persistence;

import com.example.battleship.game.GameManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class GameSaver {

    public static void save(GameManager state, Path file) throws IOException{
        Files.createDirectories(file.getParent());
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(file)))){
            out.writeObject(state);
        }
    }

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
     * Save small plain text summary (nickname and sunk ships counts).
     */
    public static void saveSummary(GameManager state, Path file) throws IOException{
        Files.createDirectories(file.getParent());
        String line = String.format("%s,%d,%d", state.getNickname(), state.getHuman().getBoard().getFleet().getSunkShipsCount(), state.getMachine().getBoard().getFleet().getSunkShipsCount());
        Files.write(file, List.of(line), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}



