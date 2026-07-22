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


public class HuntTargetStrategy implements IShootingStrategy, Serializable {

    private static final int BOARD_SIZE = 10;

    private final Set<Coordinate> shotCoordinates;
    private final Set<Coordinate> hitCoordinates;
    private final Deque<Coordinate> targetCoordinates;
    private transient Random random;
    private ShootingMode mode;

    public HuntTargetStrategy() {
        this.shotCoordinates = new HashSet<>();
        this.hitCoordinates = new HashSet<>();
        this.targetCoordinates = new ArrayDeque<>();
        this.random = new Random();
        this.mode = ShootingMode.HUNT;
    }

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

    @Override
    public void reset() {
        shotCoordinates.clear();
        targetCoordinates.clear();
        hitCoordinates.clear();
        mode = ShootingMode.HUNT;
    }


    private Coordinate nextRandomUnshotCoordinate() {
        Coordinate coordinate;
        do {
            int row = random.nextInt(BOARD_SIZE);
            int col = random.nextInt(BOARD_SIZE);
            coordinate = new Coordinate(row, col);
        } while (shotCoordinates.contains(coordinate));
        return coordinate;
    }


    private Coordinate nextValidTargetCandidate() {
        while (!targetCoordinates.isEmpty()) {
            Coordinate candidate = targetCoordinates.poll();
            if (!shotCoordinates.contains(candidate)) {
                return candidate;
            }
        }
        return null;
    }


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
    @Serial
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {

        in.defaultReadObject();

        random = new Random();
    }
}