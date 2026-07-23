package com.example.battleship.model;

import com.example.battleship.model.enums.ShipType;
import com.example.battleship.model.ships.Ship;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
/**
 * Manages the collection of ships belonging to a player's board in Battleship.
 * <p>
 * Enforces composition limits based on ship size according to official game rules
 * (1 Aircraft Carrier, 2 Submarines, 3 Destroyers, and 4 Frigates) for a total maximum fleet of 10 ships.
 * Provides utility methods to monitor sunk status and active/destroyed vessel counts.
 * Implements {@link Serializable} to support game persistence.
 * </p>

 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class Fleet implements Serializable {
    /** Serial version UID for serialization compatibility. */
    private static final long serialVersionUID = 1L;
    /** Active list of ships assigned to this fleet. */
    private final List<Ship> ships;
    /** Map defining the maximum allowable quota for each ship size (size -> maxCount). */
    private static final Map<Integer,Integer> sizeAndMax= Map.of(
            4,1,
            3,2,
            2,3,
            1,4
    );
    /**
     * Constructs a new empty {@code Fleet}.
     */
    public Fleet(){
        ships=new ArrayList<>();
    }
    /**
     * Gets the list of ships currently in this fleet.
     *
     * @return List of {@link Ship} instances.
     */
    public List<Ship> getShips(){return ships;}
    /**
     * Adds a ship to the fleet after validating total count and size limits.
     *
     * @param ship The {@link Ship} instance to add.
     * @throws IllegalArgumentException If total fleet capacity (10 ships) or size quota is exceeded.
     */
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
    /**
     * Checks if every ship in the fleet has been completely destroyed.
     *
     * @return {@code true} if all ships are sunk; {@code false} otherwise.
     */
    public boolean isFleetSunk(){
        for (Ship ship: ships){
            if (!(ship.isSunk())){
                return false;
            }
        }
        return true;
    }
    /**
     * Calculates the number of ships that remain afloat in the fleet.
     *
     * @return Count of non-sunk ships.
     */
    public int getRemainingShipsCount(){
        int count=0;
        for (Ship ship: ships){
            if (!(ship.isSunk())){
                count++;
            }
        }
        return count;
    }
    /**
     * Calculates the number of ships in the fleet that have been completely sunk.
     *
     * @return Count of destroyed ships.
     */
    public int getSunkShipsCount(){
        int count=0;
        for (Ship ship:ships){
            if (ship.isSunk()){
                count++;
            }
        }
        return count;
    }
    /**
     * Checks whether the fleet has quota available for an additional ship of the specified size.
     *
     * @param size Length of the ship in cells.
     * @return {@code true} if the current count for this size is less than the max quota; {@code false} otherwise.
     */
    private boolean hasSpaceForSize(int size){
        int count=0;
        for (Ship ship: ships){
            if (ship.getSize()==size){
                count++;
            }
        }
        return count<sizeAndMax.get(size);
    }
    /**
     * Generates a standard list of 10 ship types conforming to the game's default fleet composition.
     *
     * @return List of {@link ShipType} representing the 10 standard ships.
     */
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
