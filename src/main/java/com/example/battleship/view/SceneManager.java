package com.example.battleship.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
/**
 * Singleton manager class responsible for controlling window transitions, loading FXML layouts,
 * and applying application-wide styling and window icons in JavaFX.
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class SceneManager {
    /** Singleton instance of the SceneManager. */
    private static SceneManager instance;
    /** Primary application stage. */
    private Stage stage;
    /**
     * Private constructor to enforce the Singleton design pattern.
     */
    private SceneManager() {}
    /**
     * Retrieves the global static instance of the {@link SceneManager}.
     *
     * @return The single {@link SceneManager} instance.
     */
    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }
    /**
     * Configures the primary window {@link Stage} for scene switching and attaches the default app icon.
     *
     * @param stage The primary JavaFX {@link Stage} instance.
     */
    public void setStage(Stage stage) {
        this.stage = stage;

        Image icon = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/com/example/battleship/fxml/icons/barco.png"))
        );

        stage.getIcons().add(icon);
    }
    /**
     * Loads a specified FXML layout file, attaches the global stylesheet, and sets it as the active scene.
     *
     * @param fxmlFile The filename of the FXML resource to load (e.g., "MainView.fxml").
     */
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