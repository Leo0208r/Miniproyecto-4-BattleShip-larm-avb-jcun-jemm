package com.example.battleship.controller;

import com.example.battleship.game.GameManager;
import com.example.battleship.game.GameSession;
import com.example.battleship.model.Board;
import com.example.battleship.model.Coordinate;
import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.enums.ShipType;
import com.example.battleship.model.exceptions.InvalidCoordinateException;
import com.example.battleship.model.exceptions.ShipOverLapException;
import com.example.battleship.view.SceneManager;
import com.example.battleship.view.ShipView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.List;

public class PlacementController {

    private static final List<ShipType> COMPOSITION = com.example.battleship.model.Fleet.getStandardComposition();

    @FXML private Label lblNickname;
    @FXML private Label lblCurrentShip;
    @FXML private Label lblOrientation;
    @FXML private Label lblShipsPlaced;
    @FXML private Label lblPlacementStatus;
    @FXML private Button btnRotate;
    @FXML private Button btnRandomFill;
    @FXML private Button btnRestartPlacement;
    @FXML private Button btnStartBattle;
    @FXML private GridPane gridPlacementBoard;

    private GameManager gameManager;
    private int nextShipIndex;
    private Orientation orientation = Orientation.HORIZONTAL;

    @FXML
    public void initialize() {
        gameManager = GameSession.getInstance().requireGameManager();
        if (gameManager.getMachine().getBoard().getFleet().getShips().isEmpty()) {
            gameManager.getMachine().placeFleet();
        }

        nextShipIndex = gameManager.getHuman().getBoard().getFleet().getShips().size();
        buildGrid();
        refreshAll();
        updateStatus("Coloca tus barcos haciendo clic en el tablero.");
        btnStartBattle.setDisable(!isPlacementComplete());
    }

    private void buildGrid() {
        gridPlacementBoard.getChildren().clear();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                final int cellRow = row;
                final int cellCol = col;
                Pane cell = new Pane();
                cell.setPrefSize(38, 38);
                cell.setId("cell_" + cellRow + "_" + cellCol);
                cell.setStyle("-fx-border-color: rgba(56, 189, 248, 0.25); -fx-background-color: rgba(15, 23, 42, 0.75);");
                cell.setOnMouseClicked(event -> handlePlacement(cellRow, cellCol));
                gridPlacementBoard.add(cell, cellCol, cellRow);
            }
        }
    }

    private void handlePlacement(int row, int col) {
        if (isPlacementComplete()) {
            updateStatus("La flota ya está completa.");
            return;
        }

        ShipType shipType = COMPOSITION.get(nextShipIndex);
        try {
            gameManager.getHuman().getBoard().placeShip(new Coordinate(row, col), orientation, shipType);
            nextShipIndex++;
            GameSession.getInstance().saveCurrentGame();
            refreshAll();

            if (isPlacementComplete()) {
                updateStatus("Flota completa. Ya puedes iniciar la batalla.");
                btnStartBattle.setDisable(false);
            } else {
                updateStatus("Colocado " + shipType + ". Ahora coloca " + getCurrentShipLabel());
            }
        } catch (ShipOverLapException e) {
            updateStatus("No puedes superponer barcos: " + e.getMessage());
        } catch (InvalidCoordinateException | IllegalArgumentException e) {
            updateStatus("Ubicación inválida para " + shipType + ": " + e.getMessage());
        }
    }

    private void refreshAll() {
        buildGrid();
        renderShips();
        lblNickname.setText("Comandante: " + gameManager.getNickname());
        lblOrientation.setText("Orientación: " + orientation);
        lblShipsPlaced.setText("Barcos colocados: " + gameManager.getHuman().getPlacedShipsCount() + "/10");
        lblCurrentShip.setText("Siguiente barco: " + getCurrentShipLabel());
        btnStartBattle.setDisable(!isPlacementComplete());
    }

    private void renderShips() {
        Board board = gameManager.getHuman().getBoard();
        for (var ship : board.getFleet().getShips()) {
            ShipView view = ShipView.placeOnGrid(gridPlacementBoard, ship);
            view.setMouseTransparent(true);
        }
    }

    private boolean isPlacementComplete() {
        return gameManager.getHuman().isFleetComplete();
    }

    private String getCurrentShipLabel() {
        if (isPlacementComplete()) {
            return "Flota completa";
        }
        ShipType type = COMPOSITION.get(nextShipIndex);
        return type + " (" + type.getSize() + " celdas)";
    }

    private void updateStatus(String message) {
        lblPlacementStatus.setText(message);
    }

    @FXML
    private void handleRotate() {
        orientation = orientation == Orientation.HORIZONTAL ? Orientation.VERTICAL : Orientation.HORIZONTAL;
        refreshAll();
        updateStatus("Orientación cambiada a " + orientation + ".");
    }

    @FXML
    private void handleRandomFill() {
        GameSession.getInstance().startNewGame(gameManager.getNickname());
        gameManager = GameSession.getInstance().requireGameManager();
        gameManager.getHuman().placeFleet();
        nextShipIndex = COMPOSITION.size();
        refreshAll();
        updateStatus("La flota se colocó automáticamente.");
        GameSession.getInstance().saveCurrentGame();
    }

    @FXML
    private void handleRestartPlacement() {
        GameSession.getInstance().startNewGame(gameManager.getNickname());
        gameManager = GameSession.getInstance().requireGameManager();
        nextShipIndex = 0;
        orientation = Orientation.HORIZONTAL;
        refreshAll();
        updateStatus("Se reinició la colocación desde cero.");
        GameSession.getInstance().saveCurrentGame();
    }

    @FXML
    private void handleStartBattle() {
        if (!isPlacementComplete()) {
            updateStatus("Debes colocar los 10 barcos antes de iniciar.");
            return;
        }
        GameSession.getInstance().saveCurrentGame();
        SceneManager.getInstance().changeScene("game-view.fxml");
    }

    @FXML
    private void handleBackToMenu() {
        SceneManager.getInstance().changeScene("menu-view.fxml");
    }
}



