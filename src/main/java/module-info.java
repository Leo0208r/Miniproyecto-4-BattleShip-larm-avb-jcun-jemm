/**
 * Main module declaration for the Battleship game application.
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
module com.example.battleship {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    // Esto le da permiso a JavaFX para buscar y cargar los archivos FXML
    opens com.example.battleship to javafx.fxml;
    opens com.example.battleship.controller to javafx.fxml;
    opens com.example.battleship.view to javafx.fxml;

    // Export and open model and other packages needed by view and persistence
    opens com.example.battleship.model to javafx.fxml;
    exports com.example.battleship;
    exports com.example.battleship.controller;
    exports com.example.battleship.view;
    exports com.example.battleship.model;
    exports com.example.battleship.model.enums;
    exports com.example.battleship.model.exceptions;
    exports com.example.battleship.model.players;
    exports com.example.battleship.game;
    exports com.example.battleship.persistence;
}