package com.example.battleship.persistence;

import com.example.battleship.game.GameManager;
import com.example.battleship.model.Coordinate;
import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.enums.ShipType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameSaverTest {

    @Test
    void saveAndLoadShouldPreserveNicknameAndTurnState(@TempDir Path tempDir) throws Exception {
        GameManager original = new GameManager("Comandante Ana");
        original.setHumanTurn(false);
        Path file = tempDir.resolve("game.dat");

        GameSaver.save(original, file);
        GameManager loaded = GameSaver.load(file);

        assertEquals("Comandante Ana", loaded.getNickname());
        assertFalse(loaded.isHumanTurn());
    }

    @Test
    void saveAndLoadShouldPreserveFleetState() throws Exception {
        Path tempDir = Files.createTempDirectory("battleship-test");
        GameManager original = new GameManager("Ana");
        original.getMachine().getBoard().placeShip(new Coordinate(0, 0), Orientation.HORIZONTAL, ShipType.FRIGATE);
        Path file = tempDir.resolve("game.dat");

        GameSaver.save(original, file);
        GameManager loaded = GameSaver.load(file);

        assertEquals(1, loaded.getMachine().getBoard().getFleet().getShips().size());
    }

    @Test
    void loadedGameShouldStillBeUsableAfterLoad(@TempDir Path tempDir) throws Exception {
        GameManager original = new GameManager("Ana");
        Path file = tempDir.resolve("game.dat");
        GameSaver.save(original, file);

        GameManager loaded = GameSaver.load(file);

        assertDoesNotThrow(() -> loaded.humanShoot(new Coordinate(0, 0)));
    }

    @Test
    void saveSummaryShouldWriteNicknameAndSunkCounts(@TempDir Path tempDir) throws Exception {
        GameManager manager = new GameManager("Ana");
        manager.getMachine().getBoard().placeShip(new Coordinate(0, 0), Orientation.HORIZONTAL, ShipType.FRIGATE);
        manager.humanShoot(new Coordinate(0, 0));
        Path file = tempDir.resolve("summary.txt");

        GameSaver.saveSummary(manager, file);

        List<String> lines = Files.readAllLines(file);
        assertEquals(1, lines.size());
        assertEquals("Ana,0,1", lines.get(0));
    }

    @Test
    void loadingAMissingFileShouldThrowGamePersistenceException(@TempDir Path tempDir) {
        Path missingFile = tempDir.resolve("does-not-exist.dat");

        assertThrows(GamePersistenceException.class, () -> GameSaver.load(missingFile));
    }

    @Test
    void saveShouldCreateMissingParentDirectories(@TempDir Path tempDir) throws Exception {
        GameManager manager = new GameManager("Ana");
        Path nestedFile = tempDir.resolve("saves").resolve("nested").resolve("game.dat");

        GameSaver.save(manager, nestedFile);

        assertTrue(Files.exists(nestedFile));
    }
}