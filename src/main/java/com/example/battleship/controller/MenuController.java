package com.example.battleship.controller;

import com.example.battleship.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
        btnContinue.disableProperty().set(false); // make available if save exists (could be improved)
    }

    @FXML
    private void handleNewGame() {
        String playerNickname = txtNickname.getText().trim();
        System.out.println("Nueva partida para: " + playerNickname);
        SceneManager.getInstance().changeScene("game-view.fxml");
    }

    @FXML
    private void handleContinue() {
        System.out.println("Continuar partida...");
        SceneManager.getInstance().changeScene("game-view.fxml");
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}