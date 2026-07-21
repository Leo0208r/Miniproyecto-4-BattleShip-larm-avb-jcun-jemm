package com.example.battleship.controller;

import com.example.battleship.view.SceneManager;
import com.example.battleship.view.ModelToViewMapper;
import com.example.battleship.game.GameManager;
import com.example.battleship.persistence.GameSaver;
import com.example.battleship.model.Coordinate;
import com.example.battleship.model.enums.CellState;
import com.example.battleship.view.ShipView;

import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.fxml.FXML;
import javafx.scene.control.Button; // <-- ESTA IMPORTACIÓN TE FALTABA
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;

public class GameController {

    @FXML private GridPane gridPlayerBoard;
    @FXML private GridPane gridEnemyBoard;
    @FXML private Label lblTurnStatus;
    @FXML private Label actionStatusLabel;
    @FXML private ToggleButton btnToggleEnemyBoard; // Vinculado a tu FXML

    private boolean showingEnemyShips = false;
    private GameManager gameManager;
    private final Path saveFile = Paths.get("saves", "game.dat");
    private final Path summaryFile = Paths.get("saves", "summary.txt");

    @FXML
    public void initialize() {
        buildGrid(gridPlayerBoard, false);
        buildGrid(gridEnemyBoard, true);

        // create game manager and place fleets
        gameManager = new GameManager("Player1");
        // auto-place human and machine fleets for now
        gameManager.getHuman().placeFleet();
        gameManager.getMachine().placeFleet();

        // bind model to view
        ModelToViewMapper.bindBoardToGrid(gameManager.getHuman().getBoard(), gridPlayerBoard, true);
        ModelToViewMapper.bindBoardToGrid(gameManager.getMachine().getBoard(), gridEnemyBoard, false);

        lblTurnStatus.setText("🎯 TURNO DEL JUGADOR");
        actionStatusLabel.setText("Selecciona una coordenada en el radar enemigo.");
    }

    private void buildGrid(GridPane grid, boolean isInteractive) {
        grid.getChildren().clear();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Pane cell = new Pane();
                cell.setPrefSize(32, 32);

                cell.setStyle("-fx-border-color: rgba(0, 255, 255, 0.2); -fx-background-color: rgba(10, 20, 40, 0.6);");

                // always set id so mapper can find cells; only attach handlers if interactive
                cell.setId("cell_" + row + "_" + col);
                if (isInteractive) {
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

        try {
            Coordinate c = new Coordinate(row, col);
            CellState result = gameManager.humanShoot(c);
            // disable further clicks on this cell
            targetCell.setOnMouseClicked(null);
            targetCell.setOnMouseEntered(null);
            targetCell.setOnMouseExited(null);

            actionStatusLabel.setText("Resultado: " + result);

            // save state after each play
            try {
                GameSaver.save(gameManager, saveFile);
                GameSaver.saveSummary(gameManager, summaryFile);
            } catch (Exception ex){
                // log but don't crash UI
                ex.printStackTrace();
            }

            // if shot was water, let machine play (in background)
            if (result == CellState.WATER){
                new Thread(() -> {
                    try {
                        // machine keeps playing until it misses
                        do {
                                    Thread.sleep(600);
                                    CellState r = gameManager.machineShoot();
                                    try { GameSaver.save(gameManager, saveFile); GameSaver.saveSummary(gameManager, summaryFile); } catch (Exception ignored){}
                                    if (gameManager.getHuman().getBoard().isFleetDefeated()){
                                // human lost
                                javafx.application.Platform.runLater(() -> {
                                    actionStatusLabel.setText("Has perdido. La máquina hundió toda tu flota.");
                                });
                                break;
                                    }
                                    if (r == CellState.WATER) {
                                        break;
                            }
                        } while (!gameManager.isHumanTurn());
                        javafx.application.Platform.runLater(() -> lblTurnStatus.setText("🎯 TURNO DEL JUGADOR"));
                    } catch (Exception ex){ ex.printStackTrace(); }
                }).start();
            } else {
                // player hits or sinks and keeps turn
                if (gameManager.getMachine().getBoard().isFleetDefeated()){
                    actionStatusLabel.setText("¡Felicidades! Has hundido toda la flota enemiga.");
                } else {
                    actionStatusLabel.setText("Has acertado, sigue disparando.");
                }
            }

        } catch (Exception e){
            actionStatusLabel.setText("Coordenada inválida o error al disparar: "+e.getMessage());
        }
    }

    @FXML
    private void onViewEnemyShipsButtonClick() {
        showingEnemyShips = !showingEnemyShips;

        if (showingEnemyShips) {
            btnToggleEnemyBoard.setText("🙈 Ocultar Barcos Enemigos");
            actionStatusLabel.setText("¡Satélite activado! Viendo la ubicación de la flota enemiga.");

            // place ShipViews for all enemy ships
            for (com.example.battleship.model.ships.Ship ship : gameManager.getMachine().getBoard().getFleet().getShips()){
                ShipView.placeOnGrid(gridEnemyBoard, ship);
            }
        } else {
            btnToggleEnemyBoard.setText("👁 Ver Barcos Enemigos");
            actionStatusLabel.setText("Modo radar normal restaurado.");

            // remove ShipView nodes
            gridEnemyBoard.getChildren().removeIf(n -> n instanceof com.example.battleship.view.ShipView);
        }
    }

    @FXML
    private void onSurrenderButtonClick() {
        SceneManager.getInstance().changeScene("gameover-view.fxml");
    }

    // FXML handlers expected by the FXML files (aliases to existing methods)
    @FXML
    private void handleToggleEnemyBoard() {
        onViewEnemyShipsButtonClick();
    }

    @FXML
    private void handleSaveAndExit() {
        onSurrenderButtonClick();
    }
}