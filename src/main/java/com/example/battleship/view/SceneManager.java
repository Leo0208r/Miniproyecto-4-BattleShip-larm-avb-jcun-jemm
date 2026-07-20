package com.example.battleship.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneManager {
    private static SceneManager instance;
    private Stage stage;

    private SceneManager() {}

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void changeScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/battleship/fxml/" + fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Vincular la hoja de estilos CSS para el look oscuro/neón
            String css = getClass().getResource("/com/example/battleship/css/style.css").toExternalForm();
            scene.getStylesheets().add(css);

            stage.setScene(scene);
            stage.setTitle("Battleship - Cyber Fleet");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.err.println("Error crítico al intentar cargar la escena FXML: " + fxmlFile);
            e.printStackTrace();
        }
    }
}