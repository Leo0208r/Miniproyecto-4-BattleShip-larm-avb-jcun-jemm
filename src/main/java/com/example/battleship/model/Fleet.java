package com.example.battleship.model;

import com.example.battleship.model.enums.ShipType;
import com.example.battleship.model.ships.Ship;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Fleet implements Serializable {
    private static final long serialVersionUID = 1L;
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
    public static List<ShipType> getStandardComposition(){
        List<ShipType> shipsTypes=new ArrayList<>(10);
        for (int i=0; i<sizeAndMax.get(4);i++){
            shipsTypes.add(ShipType.AIRCRAFTCARRIER);
        }
        for (int i=0; i<sizeAndMax.get(3);i++){
            shipsTypes.add(ShipType.SUBMARINE);
        }
        for (int i=0; i<sizeAndMax.get(2);i++){
            shipsTypes.add(ShipType.DESTROYER);
        }
        for (int i=0; i<sizeAndMax.get(1);i++){
            shipsTypes.add(ShipType.FRIGATE);
        }
        return shipsTypes;
    }
}
