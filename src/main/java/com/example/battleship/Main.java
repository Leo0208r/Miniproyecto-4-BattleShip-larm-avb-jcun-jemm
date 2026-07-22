package com.example.battleship;

import com.example.battleship.game.GameSession;
import com.example.battleship.view.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        SceneManager.getInstance().setStage(primaryStage);
        if (GameSession.getInstance().loadLatestSave()) {
            boolean placementComplete = GameSession.getInstance().getCurrentGameManager() != null
                    && GameSession.getInstance().getCurrentGameManager().getHuman().isFleetComplete();
            SceneManager.getInstance().changeScene(placementComplete ? "game-view.fxml" : "shipplacement-view.fxml");
        } else {
            SceneManager.getInstance().changeScene("menu-view.fxml");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}