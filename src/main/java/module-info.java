module com.example.battleship {
    requires javafx.controls;
    requires javafx.fxml;

    // Esto le da permiso a JavaFX para buscar y cargar los archivos FXML
    opens com.example.battleship to javafx.fxml;
    opens com.example.battleship.controller to javafx.fxml;
    opens com.example.battleship.view to javafx.fxml;

    // Esto exporta los paquetes para que el sistema modular de Java los pueda ejecutar
    exports com.example.battleship;
    exports com.example.battleship.controller;
    exports com.example.battleship.view;
}