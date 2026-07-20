package com.example.battleship.model;

import com.example.battleship.model.exceptions.InvalidCoordinateException;

public class Coordinate {
    private final int row;
    private final int col;
    public Coordinate(int row, int col) throws InvalidCoordinateException{
        if(validate(row,col)){
            this.row=row;
            this.col=col;
        }else{
            throw new InvalidCoordinateException("Row or Col outside the table", row ,col);
        }
    }
    public int getRow(){return row;}
    public int getCol(){return col;}
    private boolean validate(int row, int col){
        return row>=0 && row<=9 && col>=0 && col<=9;
    }
    @Override
    public String toString(){
        char columnLetter=(char)('A'+col);
        String stringColumnLetter=String.valueOf(columnLetter);
        return stringColumnLetter+(row+1);
    }
    @Override
    public boolean equals(Object obj) {
        if (this==obj){
            return true;
        }
        if(!(obj instanceof Coordinate other)){return false;}
        return row==other.row && col==other.col;
    }
    @Override
    public int hashCode(){
        return 31*row+col;
    }

}
