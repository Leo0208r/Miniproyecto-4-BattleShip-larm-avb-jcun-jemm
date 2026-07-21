package com.example.battleship.controller;

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
    private void onRestartButtonClick() {
        SceneManager.getInstance().changeScene("menu-view.fxml");
    }
}