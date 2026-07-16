package com.example.battleship.model;

import com.example.battleship.model.ships.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Fleet {
    private final List<Ship> ships;
    private static final Map<Integer,Integer> sizeAndMax= Map.of(
            4,1,
            3,2,
            2,3,
            1,4
    );
    public Fleet(){
        ships=new ArrayList<>();
    }
    public List<Ship> getShips(){return ships;}
    public void addShip(Ship ship){
       if (ships.size()+1<=10){
           if(hasSpaceForSize(ship.getSize())){
               ships.add(ship);
           }else{
               throw new IllegalArgumentException("it cannot put the ship in the list");
           }
       }else{
           throw new IllegalArgumentException("it cannot be more than 10 ships");
       }
    }
    public boolean isFleetSunk(){
        for (Ship ship: ships){
            if (!(ship.isSunk())){
                return false;
            }
        }
        return true;
    }
    public int getRemainingShipsCount(){
        int count=0;
        for (Ship ship: ships){
            if (!(ship.isSunk())){
                count++;
            }
        }
        return count;
    }
    public int getSunkShipsCount(){
        int count=0;
        for (Ship ship:ships){
            if (ship.isSunk()){
                count++;
            }
        }
        return count;
    }
    private boolean hasSpaceForSize(int size){
        int count=0;
        for (Ship ship: ships){
            if (ship.getSize()==size){
                count++;
            }
        }
        return count<sizeAndMax.get(size);
    }
}
