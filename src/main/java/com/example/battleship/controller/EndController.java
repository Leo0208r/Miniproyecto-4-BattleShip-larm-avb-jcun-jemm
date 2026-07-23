package com.example.battleship.controller;

import com.example.battleship.game.GameManager;
import com.example.battleship.game.GameSession;
import com.example.battleship.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
/**
 * Controller class for the game over / end game screen in Battleship.
 * <p>
 * Displays the match outcome (Victory or Defeat), presents detailed game statistics
 * such as sunk ships and missed shots for both players, and handles clearing saved match data.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
@SuppressWarnings("unused")
public class EndController {

    /** Title label indicating match outcome (Victory or Defeat). */
    @FXML private Label lblTitle;
    /** Label displaying the human player's nickname. */
    @FXML private Label lblPlayerNickname;
    /** Label displaying the count of enemy ships sunk by the player. */
    @FXML private Label lblEnemyShipsSunk;
    /** Label displaying total missed shots made by the human player. */
    @FXML private Label lblPlayerMissedShots;
    /** Label displaying total missed shots made by the AI. */
    @FXML private Label lblPlayerShipsSunk;
    /** Button to return to the main menu screen. */
    @FXML private Label lblEnemyMissedShots;
    @FXML private Button btnMainMenu;
    /**
     * Initializes the end screen view automatically upon FXML loading.
     * <p>
     * Retrieves final statistics from {@link GameManager}, determines match outcome,
     * updates UI labels, and removes active save files.
     * </p>
     */
    @FXML
    public void initialize() {
        GameManager gameManager = GameSession.getInstance().getCurrentGameManager();
        if (gameManager == null) {
            lblTitle.setText("⚓ PARTIDA FINALIZADA");
            lblPlayerNickname.setText("👤 JUGADOR: N/D");
            lblEnemyShipsSunk.setText("0 / 10");
            lblPlayerMissedShots.setText("0");
            lblPlayerShipsSunk.setText("0 / 10");
            lblEnemyMissedShots.setText("0");
            return;
        }

        boolean humanWon = gameManager.getMachine().getBoard().isFleetDefeated();
        lblTitle.setText(humanWon ? "🏆 VICTORIA 🏆" : "💀 DERROTA 💀");
        lblPlayerNickname.setText("👤 JUGADOR: " + gameManager.getNickname());
        lblEnemyShipsSunk.setText(gameManager.getMachine().getBoard().getFleet().getSunkShipsCount() + " / 10");
        lblPlayerMissedShots.setText(String.valueOf(countShots(gameManager.getMachine().getBoard().getBoard().values(), com.example.battleship.model.enums.CellState.WATER)));
        lblPlayerShipsSunk.setText(gameManager.getHuman().getBoard().getFleet().getSunkShipsCount() + " / 10");
        lblEnemyMissedShots.setText(String.valueOf(countShots(gameManager.getHuman().getBoard().getBoard().values(), com.example.battleship.model.enums.CellState.WATER)));

        clearSavedGameOnce();
    }

    /**
     * Clears the current saved game session file to prevent resuming an ended match.
     */
    private void clearSavedGameOnce() {
        GameSession.getInstance().clearSavedGame();
    }

    /**
     * Counts the total number of cells matching a specific {@link CellState}.
     *
     * @param cells Collection of board cells to evaluate.
     * @param state Target cell state (e.g., WATER for missed shots).
     * @return Total count of cells matching the requested state.
     */
    private long countShots(java.util.Collection<com.example.battleship.model.Cell> cells, com.example.battleship.model.enums.CellState state) {
        return cells.stream().filter(cell -> cell.getCellState() == state).count();
    }
    /**
     * Handles returning the user to the main menu view.
     */
    @FXML
    private void onRestartButtonClick() {
        SceneManager.getInstance().changeScene("menu-view.fxml");
    }
}
