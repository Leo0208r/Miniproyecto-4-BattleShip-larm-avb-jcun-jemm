package com.example.battleship.model.enums;

public enum ShootingMode {
    HUNT("Hunt"),
    TARGET("Target");
    private final  String symbol;
    ShootingMode(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
