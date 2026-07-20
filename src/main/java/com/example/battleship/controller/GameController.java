package com.example.battleship.controller;

import com.example.battleship.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button; // <-- ESTA IMPORTACIÓN TE FALTABA
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;

public class GameController {

    @FXML private GridPane playerGrid;
    @FXML private GridPane enemyGrid;
    @FXML private Label turnLabel;
    @FXML private Label actionStatusLabel;
    @FXML private Button viewEnemyShipsButton; // Vinculado a tu FXML

    private boolean showingEnemyShips = false;

    @FXML
    public void initialize() {
        buildGrid(playerGrid, false);
        buildGrid(enemyGrid, true);

        turnLabel.setText("Tu Turno: Comanda el ataque");
        actionStatusLabel.setText("Selecciona una coordenada en el radar enemigo.");
    }

    private void buildGrid(GridPane grid, boolean isInteractive) {
        grid.getChildren().clear();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Pane cell = new Pane();
                cell.setPrefSize(32, 32);

                cell.setStyle("-fx-border-color: rgba(0, 255, 255, 0.2); -fx-background-color: rgba(10, 20, 40, 0.6);");

                if (isInteractive) {
                    cell.setId("cell_" + row + "_" + col);

                    cell.setOnMouseEntered(e -> cell.setStyle("-fx-border-color: #00ffff; -fx-background-color: rgba(0, 255, 255, 0.25);"));
                    cell.setOnMouseExited(e -> cell.setStyle("-fx-border-color: rgba(0, 255, 255, 0.2); -fx-background-color: rgba(10, 20, 40, 0.6);"));

                    cell.setOnMouseClicked(this::handleAttackEvent);
                }
                grid.add(cell, col, row);
            }
        }
    }

    private void handleAttackEvent(MouseEvent event) {
        Pane targetCell = (Pane) event.getSource();
        String[] coords = targetCell.getId().split("_");
        int row = Integer.parseInt(coords[1]);
        int col = Integer.parseInt(coords[2]);

        if ((row + col) % 3 == 0) {
            targetCell.setStyle("-fx-background-color: #ff3333; -fx-border-color: #ff0000;");
            actionStatusLabel.setText("¡Impacto confirmado en: [" + row + ", " + col + "]!");
        } else {
            targetCell.setStyle("-fx-background-color: rgba(255, 255, 255, 0.4); -fx-border-color: #cccccc;");
            actionStatusLabel.setText("Disparo fallido en agua en: [" + row + ", " + col + "].");
        }

        targetCell.setOnMouseClicked(null);
        targetCell.setOnMouseEntered(null);
        targetCell.setOnMouseExited(null);
    }

    @FXML
    private void onViewEnemyShipsButtonClick() {
        showingEnemyShips = !showingEnemyShips;

        if (showingEnemyShips) {
            viewEnemyShipsButton.setText("🙈 Ocultar Barcos Enemigos");
            actionStatusLabel.setText("¡Satélite activado! Viendo la ubicación de la flota enemiga.");

            for (javafx.scene.Node node : enemyGrid.getChildren()) {
                if (node instanceof Pane) {
                    Pane cell = (Pane) node;
                    String id = cell.getId();
                    if (id != null) {
                        String[] coords = id.split("_");
                        int row = Integer.parseInt(coords[1]);
                        int col = Integer.parseInt(coords[2]);

                        if ((row + col) % 3 == 0) {
                            cell.setStyle("-fx-background-color: rgba(155, 0, 255, 0.7); -fx-border-color: #9900ff;");
                        }
                    }
                }
            }
        } else {
            viewEnemyShipsButton.setText("👁 Ver Barcos Enemigos");
            actionStatusLabel.setText("Modo radar normal restaurado.");

            for (javafx.scene.Node node : enemyGrid.getChildren()) {
                if (node instanceof Pane) {
                    Pane cell = (Pane) node;
                    if (cell.getOnMouseClicked() != null) {
                        cell.setStyle("-fx-border-color: rgba(255, 51, 51, 0.2); -fx-background-color: rgba(10, 20, 40, 0.6);");
                    }
                }
            }
        }
    }

    @FXML
    private void onSurrenderButtonClick() {
        SceneManager.getInstance().changeScene("end-view.fxml");
    }
}