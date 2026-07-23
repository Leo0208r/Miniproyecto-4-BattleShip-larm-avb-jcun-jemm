package com.example.battleship.model;

import com.example.battleship.model.enums.CellState;
import com.example.battleship.model.enums.ShootingMode;
import com.example.battleship.model.exceptions.InvalidCoordinateException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
/**
 * Implements the Hunt-and-Target AI shooting strategy for Battleship.
 * <p>
 * Operates in two distinct states:
 * </p>
 * <ul>
 *   <li><b>HUNT Mode:</b> Randomly fires at valid, unshot grid cells looking for an enemy vessel.</li>
 *   <li><b>TARGET Mode:</b> Triggered when a shot hits a ship. Systematically enqueues adjacent neighboring
 *   coordinates (up, down, left, right) and aligns shots along identified axes until the ship is SUNK.</li>
 * </ul>
 * <p>
 * Implements {@link IShootingStrategy} and {@link Serializable} for AI state preservation across saves.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class HuntTargetStrategy implements IShootingStrategy, Serializable {
    /** Dimension of the square game board (10x10). */
    private static final int BOARD_SIZE = 10;
    /** Set tracking all coordinates targeted in previous turns. */
    private final Set<Coordinate> shotCoordinates;
    /** Set tracking coordinates that successfully scored a HIT on the current unsunk vessel. */
    private final Set<Coordinate> hitCoordinates;
    /** Queue of high-priority target coordinates queued during TARGET mode. */
    private final Deque<Coordinate> targetCoordinates;
    /** Random number generator for HUNT mode selection (re-initialized after deserialization). */
    private transient Random random;
    /** Current operational shooting state (HUNT or TARGET). */
    private ShootingMode mode;
    /**
     * Constructs a new {@code HuntTargetStrategy} starting in {@link ShootingMode#HUNT} mode.
     */
    public HuntTargetStrategy() {
        this.shotCoordinates = new HashSet<>();
        this.hitCoordinates = new HashSet<>();
        this.targetCoordinates = new ArrayDeque<>();
        this.random = new Random();
        this.mode = ShootingMode.HUNT;
    }
    /**
     * Selects the next coordinate to attack based on current state.
     * <p>
     * Returns a candidate from queued targets in TARGET mode if available;
     * otherwise falls back to HUNT mode selecting an unshot random coordinate.
     * </p>
     *
     * @return Target {@link Coordinate} to attack.
     */
    @Override
    public Coordinate selectTarget() {
        if (mode == ShootingMode.TARGET) {
            Coordinate candidate = nextValidTargetCandidate();
            if (candidate != null) {
                return candidate;
            }
            mode = ShootingMode.HUNT;
        }
        return nextRandomUnshotCoordinate();
    }
    /**
     * Registers shot outcomes to update strategy intelligence and state.
     *
     * @param coordinate The target coordinate attacked.
     * @param result     Resulting {@link CellState} (WATER, HIT, SUNK).
     */
    @Override
    public void registerResult(Coordinate coordinate, CellState result) {
        shotCoordinates.add(coordinate);
        switch (result) {
            case WATER -> {
                if (mode == ShootingMode.TARGET && targetCoordinates.isEmpty()) {
                    mode = ShootingMode.HUNT;
                }
            }
            case HIT -> {
                hitCoordinates.add(coordinate);
                mode = ShootingMode.TARGET;
                enqueueNeighbors(coordinate);
                enqueueAlignedTargets();
            }
            case SUNK -> {
                mode = ShootingMode.HUNT;
                targetCoordinates.clear();
                hitCoordinates.clear();
            }
            default -> throw new IllegalArgumentException("Unexpected shot result: " + result);
        }
    }
    /**
     * Resets strategy internal history and reverts to standard HUNT mode.
     */
    @Override
    public void reset() {
        shotCoordinates.clear();
        targetCoordinates.clear();
        hitCoordinates.clear();
        mode = ShootingMode.HUNT;
    }

    /**
     * Generates an unshot random coordinate on the 10x10 board.
     *
     * @return Next random {@link Coordinate}.
     */
    private Coordinate nextRandomUnshotCoordinate() {
        Coordinate coordinate;
        do {
            int row = random.nextInt(BOARD_SIZE);
            int col = random.nextInt(BOARD_SIZE);
            coordinate = new Coordinate(row, col);
        } while (shotCoordinates.contains(coordinate));
        return coordinate;
    }

    /**
     * Retrieves the next valid unshot candidate from the priority target queue.
     *
     * @return Valid target {@link Coordinate}, or {@code null} if queue is exhausted.
     */
    private Coordinate nextValidTargetCandidate() {
        while (!targetCoordinates.isEmpty()) {
            Coordinate candidate = targetCoordinates.poll();
            if (!shotCoordinates.contains(candidate)) {
                return candidate;
            }
        }
        return null;
    }

    /**
     * Adds non-diagonal adjacent neighbors of a hit coordinate to the target queue.
     *
     * @param coordinate Central hit coordinate.
     */
    private void enqueueNeighbors(Coordinate coordinate) {
        int row = coordinate.getRow();
        int col = coordinate.getCol();

        int[][] offsets = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };
        for (int[] offset : offsets) {
            int neighborRow = row + offset[0];
            int neighborCol = col + offset[1];
            try {
                Coordinate neighbor = new Coordinate(neighborRow, neighborCol);
                if (!shotCoordinates.contains(neighbor) && !targetCoordinates.contains(neighbor)) {
                    targetCoordinates.add(neighbor);
                }
            } catch (InvalidCoordinateException e) {
                // Neighbor falls outside the board; simply skip it.
            }
        }
    }
    /**
     * Identifies linear ship orientation when multiple hits exist and prioritizes endpoints.
     */
    private void enqueueAlignedTargets() {
        if (hitCoordinates.size() < 2) {
            return;
        }

        boolean sameRow = hitCoordinates.stream().map(Coordinate::getRow).distinct().count() == 1;
        boolean sameCol = hitCoordinates.stream().map(Coordinate::getCol).distinct().count() == 1;

        if (sameRow) {
            int row = hitCoordinates.iterator().next().getRow();
            int minCol = hitCoordinates.stream().mapToInt(Coordinate::getCol).min().orElse(0);
            int maxCol = hitCoordinates.stream().mapToInt(Coordinate::getCol).max().orElse(0);
            addTargetFront(row, minCol - 1);
            addTargetFront(row, maxCol + 1);
        } else if (sameCol) {
            int col = hitCoordinates.iterator().next().getCol();
            int minRow = hitCoordinates.stream().mapToInt(Coordinate::getRow).min().orElse(0);
            int maxRow = hitCoordinates.stream().mapToInt(Coordinate::getRow).max().orElse(0);
            addTargetFront(minRow - 1, col);
            addTargetFront(maxRow + 1, col);
        }
    }

    /**
     * Prepends high-priority coordinates at the front of the targeting deque.
     *
     * @param row Row index.
     * @param col Column index.
     */
    private void addTargetFront(int row, int col) {
        try {
            Coordinate candidate = new Coordinate(row, col);
            if (!shotCoordinates.contains(candidate) && !targetCoordinates.contains(candidate)) {
                targetCoordinates.addFirst(candidate);
            }
        } catch (InvalidCoordinateException e) {
            // outside the board, ignore
        }
    }
    /**
     * Custom deserialization logic to restore non-serializable fields (e.g. Random).
     *
     * @param in ObjectInputStream instance.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class cannot be located.
     */
    @Serial
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {

        in.defaultReadObject();

        random = new Random();
    }
}