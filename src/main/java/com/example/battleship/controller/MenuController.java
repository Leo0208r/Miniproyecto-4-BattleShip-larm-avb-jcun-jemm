package com.example.battleship.controller;

import com.example.battleship.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class MenuController {

    @FXML private TextField nicknameField;
    @FXML private Button startButton;

    @FXML
    public void initialize() {
        startButton.disableProperty().bind(nicknameField.textProperty().isEmpty());
    }


    @FXML
    private void onLoadGameButtonClick() {
        System.out.println("Cargando partida guardada...");
        SceneManager.getInstance().changeScene("game-view.fxml");
    }

    @FXML
    private void onStartButtonClick() {
        String playerNickname = nicknameField.getText().trim();
        System.out.println("Jugador registrado: " + playerNickname);



        SceneManager.getInstance().changeScene("game-view.fxml");
    }

    @FXML
    private void onExitButtonClick() {
        System.exit(0);
    }
}