package com.example.battleship.model;

import com.example.battleship.model.enums.CellState;

/**
 * Listener interface for Board changes. Implementations (UI) should register to
 * receive notifications when a cell changes state or when ships are placed.
 */
public interface BoardListener {
    void onCellUpdated(Coordinate coordinate, CellState newState);
}

