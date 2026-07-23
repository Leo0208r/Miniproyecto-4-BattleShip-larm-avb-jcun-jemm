package com.example.battleship.controller;

import com.example.battleship.game.GameSession;
import com.example.battleship.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
/**
 * Controller class for the main menu screen of the Battleship application.
 * <p>
 * Manages user interactions for starting a new game, continuing a previously saved match,
 * and exiting the application gracefully.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class MenuController {

    /** Text field where the user enters their nickname. */
    @FXML private TextField txtNickname;
    /** Button to start a new match. */
    @FXML private Button btnNewGame;
    /** Button to resume a previously saved game session. */
    @FXML private Button btnContinue;
    /** Button to exit the application. */
    @FXML private Button btnExit;
    /**
     * Initializes the controller automatically upon loading the FXML view.
     * <p>
     * Binds the new game button disabled state to the nickname text field and enables
     * the continue button only if a valid save file exists.
     * </p>
     */
    @FXML
    public void initialize() {
        btnNewGame.disableProperty().bind(txtNickname.textProperty().isEmpty());
        btnContinue.setDisable(!GameSession.getInstance().hasSavedGame());
    }

    /**
     * Handles the event when the user clicks the "New Game" button.
     * Starts a new game session with the entered nickname and navigates to the ship placement view.
     */
    @FXML
    private void handleNewGame() {
        String playerNickname = txtNickname.getText().trim();
        GameSession.getInstance().startNewGame(playerNickname);
        SceneManager.getInstance().changeScene("shipplacement-view.fxml");
    }

    /**
     * Handles the event when the user clicks the "Continue" button.
     * Attempts to load the latest save file and redirects the user to either the battle view
     * or ship placement view based on fleet completion state.
     */
    @FXML
    private void handleContinue() {
        if (GameSession.getInstance().loadLatestSave()) {
            boolean placementComplete = GameSession.getInstance().getCurrentGameManager() != null
                    && GameSession.getInstance().getCurrentGameManager().getHuman().isFleetComplete();
            SceneManager.getInstance().changeScene(placementComplete ? "game-view.fxml" : "shipplacement-view.fxml");
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Partida no disponible");
            alert.setHeaderText("No se encontró una partida guardada válida");
            alert.setContentText("Crea una nueva partida o verifica que el archivo saves/game.dat exista y no esté corrupto.");
            alert.showAndWait();
            btnContinue.setDisable(true);
        }
    }

    /**
     * Handles the event when the user clicks the "Exit" button.
     * Terminates the application execution.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}