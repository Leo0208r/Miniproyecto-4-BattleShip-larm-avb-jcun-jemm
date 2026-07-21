package com.example.battleship.preview;

import com.example.battleship.model.enums.Orientation;
import com.example.battleship.view.ShipView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Temporary preview class. Not part of the game, only used to check
 * how each ship type looks with the current ShipView design.
 * Safe to delete once the visual design is confirmed.
 */
public class ShipPreviewApp extends Application {

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(24);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #090d16;");

        root.getChildren().add(rowFor("Frigate (1)", 1, Orientation.HORIZONTAL));
        root.getChildren().add(rowFor("Destroyer (2)", 2, Orientation.HORIZONTAL));
        root.getChildren().add(rowFor("Submarine (3)", 3, Orientation.HORIZONTAL));
        root.getChildren().add(rowFor("Aircraft carrier (4)", 4, Orientation.HORIZONTAL));
        root.getChildren().add(rowFor("Destroyer vertical (2)", 2, Orientation.VERTICAL));

        ShipView hitShip = new ShipView(3, Orientation.HORIZONTAL);
        hitShip.markHit();
        ShipView sunkShip = new ShipView(3, Orientation.HORIZONTAL);
        sunkShip.markSunk();
        root.getChildren().add(wrap("Submarine - hit state", hitShip, 3, Orientation.HORIZONTAL));
        root.getChildren().add(wrap("Submarine - sunk state", sunkShip, 3, Orientation.HORIZONTAL));

        Scene scene = new Scene(root, 500, 600);
        scene.getStylesheets().add(getClass().getResource(
                "/com/example/battleship/css/style.css").toExternalForm());

        stage.setTitle("Ship preview (temporary)");
        stage.setScene(scene);
        stage.show();
    }

    private HBox rowFor(String label, int size, Orientation orientation) {
        ShipView view = new ShipView(size, orientation);
        return wrap(label, view, size, orientation);
    }

    private HBox wrap(String label, ShipView view, int size, Orientation orientation) {
        double width = orientation == Orientation.HORIZONTAL
                ? size * ShipView.CELL_SIZE : ShipView.CELL_SIZE;
        double height = orientation == Orientation.VERTICAL
                ? size * ShipView.CELL_SIZE : ShipView.CELL_SIZE;

        Pane holder = new Pane(view);
        holder.setPrefSize(width, height);


        Label text = new Label(label);
        text.setStyle("-fx-text-fill: #8fa0b5; -fx-min-width: 180px;");

        HBox row = new HBox(16, text, holder);
        row.setStyle("-fx-alignment: center-left;");
        return row;
    }

    public static void main(String[] args) {
        launch(args);
    }
}