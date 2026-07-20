package com.example.battleship.controller;

import com.example.battleship.view.SceneManager;
import javafx.fxml.FXML;

public class EndController {

    @FXML
    private void onRestartButtonClick() {
        SceneManager.getInstance().changeScene("menu-view.fxml");
    }

    @FXML
    private void onExitButtonClick() {
        System.exit(0);
    }
}