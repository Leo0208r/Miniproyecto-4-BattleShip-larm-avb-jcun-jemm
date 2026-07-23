package com.example.battleship;

import com.example.battleship.view.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;
/**
 * Clase principal que inicia la aplicación de Battleship.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        SceneManager.getInstance().setStage(primaryStage);
        SceneManager.getInstance().changeScene("menu-view.fxml");
    }
    /**
     * Punto de entrada principal del programa.
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
