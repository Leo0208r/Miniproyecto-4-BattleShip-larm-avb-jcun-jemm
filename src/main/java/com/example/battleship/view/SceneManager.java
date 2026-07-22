package com.example.battleship.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

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

        Image icon = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/com/example/battleship/fxml/icons/barco.png"))
        );

        stage.getIcons().add(icon);
    }

    public void changeScene(String fxmlFile) {
        try {
            java.net.URL fxmlUrl = getClass().getResource("/com/example/battleship/fxml/" + fxmlFile);
            if (fxmlUrl == null) {
                throw new IOException("No se encontró el recurso FXML: /com/example/battleship/fxml/" + fxmlFile);
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Vincular la hoja de estilos CSS para el look oscuro/neón
            java.net.URL cssUrl = getClass().getResource("/com/example/battleship/css/style.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            stage.setScene(scene);
            stage.setTitle("Battleship - Cyber Fleet");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.err.println("Error crítico al intentar cargar la escena FXML: " + fxmlFile);
            System.err.println("Detalle: " + e.getMessage());
        }
    }
}