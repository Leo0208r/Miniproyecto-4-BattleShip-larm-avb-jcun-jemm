package com.example.battleship.controller;

import com.example.battleship.game.GameManager;
import com.example.battleship.game.GameSession;
import com.example.battleship.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

@SuppressWarnings("unused")
public class EndController {

    @FXML private Label lblTitle;
    @FXML private Label lblPlayerNickname;
    @FXML private Label lblEnemyShipsSunk;
    @FXML private Label lblPlayerMissedShots;
    @FXML private Label lblPlayerShipsSunk;
    @FXML private Label lblEnemyMissedShots;
    @FXML private Button btnMainMenu;

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

        // Limpiar la partida guardada al terminar
        clearSavedGameOnce();
    }

    /**
     * Limpia la partida guardada una sola vez cuando se muestra la pantalla de fin.
     * Esto previene que se acumulen partidas antiguas y asegura que el botón "Continuar"
     * se deshabilite correctamente en el menú.
     */
    private void clearSavedGameOnce() {
        GameSession.getInstance().clearSavedGame();
    }

    private long countShots(java.util.Collection<com.example.battleship.model.Cell> cells, com.example.battleship.model.enums.CellState state) {
        return cells.stream().filter(cell -> cell.getCellState() == state).count();
    }

    @FXML
    private void onRestartButtonClick() {
        SceneManager.getInstance().changeScene("menu-view.fxml");
    }
}
