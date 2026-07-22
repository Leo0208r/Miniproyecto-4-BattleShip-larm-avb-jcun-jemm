package com.example.battleship.controller;

import com.example.battleship.game.GameSession;
import com.example.battleship.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class MenuController {

    // fx:id names must match those defined in FXML
    @FXML private TextField txtNickname;
    @FXML private Button btnNewGame;
    @FXML private Button btnContinue;
    @FXML private Button btnExit;

    @FXML
    public void initialize() {
        // disable "New Game" until nickname is provided
        btnNewGame.disableProperty().bind(txtNickname.textProperty().isEmpty());
        btnContinue.setDisable(!GameSession.getInstance().hasSavedGame());
    }

    @FXML
    private void handleNewGame() {
        String playerNickname = txtNickname.getText().trim();
        GameSession.getInstance().startNewGame(playerNickname);
        SceneManager.getInstance().changeScene("shipplacement-view.fxml");
    }

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

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}