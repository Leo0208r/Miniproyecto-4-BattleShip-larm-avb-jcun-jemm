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
 * Manages the state and turn-based flow of a Battleship match.
 * <p>
 * Controls human and AI players, tracks turns, manages revealed sunken ships,
 * handles firing actions, and restores transient board state upon reloading.
 * Implements {@link Serializable} to support saving and restoring match progress.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class GameManager implements Serializable {

    /** Serial version UID for serialization compatibility. */
    private static final long serialVersionUID = 1L;
    /** Human player instance. */
    private final HumanPlayer human;
    /** Machine (AI) player instance. */
    private final MachinePlayer machine;
    /** Indicates whether it is currently the human player's turn. */
    private boolean humanTurn;
    /** Player's nickname. */
    private String nickname;
    /** Set of enemy ships that have been revealed as sunken on the view board. */
    private Set<Ship> revealedSunkenShips = new HashSet<>();
    /**
     * Constructs a new {@code GameManager} instance for a player with the given nickname.
     *
     * @param nickname The human player's nickname.
     */
    public GameManager(String nickname){
        this.nickname = nickname;
        this.human = new HumanPlayer();
        this.machine = new MachinePlayer();
        this.humanTurn = true;
    }
    /**
     * Gets the human player instance.
     *
     * @return The {@link HumanPlayer} object.
     */
    public HumanPlayer getHuman(){return human;}
    /**
     * Gets the machine (AI) player instance.
     *
     * @return The {@link MachinePlayer} object.
     */
    public MachinePlayer getMachine(){return machine;}
    /**
     * Checks if it is currently the human player's turn.
     *
     * @return {@code true} if it is the human player's turn; {@code false} otherwise.
     */
    public boolean isHumanTurn(){return humanTurn;}
    /**
     * Sets turn ownership.
     *
     * @param t {@code true} to grant turn to human, {@code false} for machine.
     */
    public void setHumanTurn(boolean t){this.humanTurn = t;}
    /**
     * Gets the player's nickname.
     *
     * @return Player nickname string.
     */
    public String getNickname(){return nickname;}
    /**
     * Updates the player's nickname.
     *
     * @param nickname New nickname to set.
     */
    public void setNickname(String nickname){this.nickname = nickname;}
    /**
     * Gets the set of enemy ships that have already been visually revealed as sunk.
     *
     * @return Set containing revealed sunken {@link Ship} instances.
     */
    public Set<Ship> getRevealedSunkenShips(){
        if (revealedSunkenShips == null) revealedSunkenShips = new HashSet<>();
        return revealedSunkenShips;
    }
    /**
     * Adds a ship to the set of revealed sunken ships.
     *
     * @param ship The {@link Ship} to mark as revealed.
     */
    public void addRevealedSunkenShip(Ship ship){
        if (ship == null) return;
        getRevealedSunkenShips().add(ship);
    }

    /**
     * Executes a shot targeted by the human player onto the machine's board.
     * Switch turn to machine if the shot results in water.
     *
     * @param c Target coordinate for the attack.
     * @return Resulting {@link CellState} of the target cell.
     * @throws Exception If coordinate is invalid or cell has already been targeted.
     */
    public com.example.battleship.model.enums.CellState humanShoot(Coordinate c) throws Exception{
        CellState result = machine.getBoard().shoot(c);
        // If water, switch turn
        if (result == CellState.WATER){
            humanTurn = false;
        }
        return result;
    }

    /**
     * Executes an AI-selected shot onto the human player's board.
     * Switches turn back to human if the shot misses (hits water).
     *
     * @return Resulting {@link CellState} of the target cell.
     * @throws Exception If selected coordinate is invalid or shot fails.
     */
    public com.example.battleship.model.enums.CellState machineShoot() throws Exception{
        Coordinate c = machine.chooseShotTarget();
        CellState result = human.getBoard().shoot(c);
        machine.registerShotResult(c, result);
        if (result == CellState.WATER){
            humanTurn = true;
        }
        return result;
    }
    /**
     * Re-initializes transient board bindings and non-serializable fields after loading a saved match.
     */
    public void afterLoad() {
        human.getBoard().restoreTransientState();
        machine.getBoard().restoreTransientState();
        if (revealedSunkenShips == null) revealedSunkenShips = new HashSet<>();
    }
}



