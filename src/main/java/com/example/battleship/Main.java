package com.example.battleship;

import com.example.battleship.view.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        SceneManager.getInstance().setStage(primaryStage);

        SceneManager.getInstance().changeScene("menu-view.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}