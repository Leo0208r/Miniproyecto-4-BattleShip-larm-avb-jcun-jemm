package com.example.battleship.game;

import com.example.battleship.model.players.HumanPlayer;
import com.example.battleship.model.players.MachinePlayer;
import com.example.battleship.model.enums.CellState;
import com.example.battleship.model.Coordinate;
import com.example.battleship.model.ships.Ship;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple game manager that holds the two players and the turn state.
 * Serializable so it can be saved/loaded by GameSaver.
 */
public class GameManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private final HumanPlayer human;
    private final MachinePlayer machine;
    private boolean humanTurn;
    private String nickname;

    /** Ships that the player has seen revealed as sunken; persisted with the game */
    private Set<Ship> revealedSunkenShips = new HashSet<>();

    public GameManager(String nickname){
        this.nickname = nickname;
        this.human = new HumanPlayer();
        this.machine = new MachinePlayer();
        this.humanTurn = true;
    }

    public HumanPlayer getHuman(){return human;}
    public MachinePlayer getMachine(){return machine;}
    public boolean isHumanTurn(){return humanTurn;}
    public void setHumanTurn(boolean t){this.humanTurn = t;}
    public String getNickname(){return nickname;}
    public void setNickname(String nickname){this.nickname = nickname;}

    public Set<Ship> getRevealedSunkenShips(){
        if (revealedSunkenShips == null) revealedSunkenShips = new HashSet<>();
        return revealedSunkenShips;
    }

    public void addRevealedSunkenShip(Ship ship){
        if (ship == null) return;
        getRevealedSunkenShips().add(ship);
    }

    /**
     * Apply a shot by the human to the machine board and return the result.
     */
    public com.example.battleship.model.enums.CellState humanShoot(Coordinate c) throws Exception{
        CellState result = machine.getBoard().shoot(c);
        // If water, switch turn
        if (result == CellState.WATER){
            humanTurn = false;
        }
        return result;
    }

    public com.example.battleship.model.enums.CellState machineShoot() throws Exception{
        Coordinate c = machine.chooseShotTarget();
        CellState result = human.getBoard().shoot(c);
        machine.registerShotResult(c, result);
        if (result == CellState.WATER){
            humanTurn = true;
        }
        return result;
    }

    public void afterLoad() {
        human.getBoard().restoreTransientState();
        machine.getBoard().restoreTransientState();
        if (revealedSunkenShips == null) revealedSunkenShips = new HashSet<>();
    }
}



