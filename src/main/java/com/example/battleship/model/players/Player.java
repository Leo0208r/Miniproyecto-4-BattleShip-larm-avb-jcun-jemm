package com.example.battleship.model.players;

public abstract class Player {
    protected  String name;
    protected int sunkenShips;
    public Player(String name, int sunkenShips){
        this.name=name;
        this.sunkenShips=sunkenShips;
    }
    public abstract void  shot();
    public String getName(){return name;}
    public int getSunkenShips(){return sunkenShips;}
}
