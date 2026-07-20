package com.example.battleship.model.exceptions;

public class InvalidCoordinateException extends RuntimeException {
    private final int row;
    private final int col;

    public InvalidCoordinateException(String message, int row, int col){
        super(message);
        this.row=row;
        this.col=col;
    }
    public int getRow(){return row;}
    public int getCol(){return col;}
}
