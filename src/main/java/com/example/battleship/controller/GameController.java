package com.example.battleship.controller;

import com.example.battleship.game.GameManager;
import com.example.battleship.game.GameSession;
import com.example.battleship.model.Cell;
import com.example.battleship.model.Coordinate;
import com.example.battleship.model.enums.CellState;
import com.example.battleship.model.enums.Orientation;
import com.example.battleship.model.ships.Ship;
import com.example.battleship.view.ModelToViewMapper;
import com.example.battleship.view.SceneManager;
import com.example.battleship.view.ShipView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashSet;
import java.util.Set;

public class GameController {

    private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());

    @FXML private GridPane gridPlayerBoard;
    @FXML private GridPane gridEnemyBoard;
    @FXML private Label lblPlayerName;
    @FXML private Label lblPlayerShipsLeft;
    @FXML private Label lblTurnStatus;
    @FXML private Label lblEnemyShipsLeft;
    @FXML private Label actionStatusLabel;
    @FXML private ToggleButton btnToggleEnemyBoard;
    @FXML private Button btnSaveAndExit;

    private boolean showingEnemyShips = false;
    private GameManager gameManager;
    private final ScheduledExecutorService machineExecutor = Executors.newSingleThreadScheduledExecutor();
    private volatile boolean machineTurnScheduled;

    @FXML
    public void initialize() {
        gameManager = GameSession.getInstance().requireGameManager();
        if (!gameManager.getHuman().isFleetComplete()) {
            SceneManager.getInstance().changeScene("shipplacement-view.fxml");
            return;
        }

        buildGrid(gridPlayerBoard, false);
        buildGrid(gridEnemyBoard, true);

        ModelToViewMapper.bindBoardToGrid(gameManager.getHuman().getBoard(), gridPlayerBoard, true);
        ModelToViewMapper.bindBoardToGrid(gameManager.getMachine().getBoard(), gridEnemyBoard, false);

        // Forzar orden inicial en los grids para evitar que barcos tapen marcas
        enforceGridChildrenViewOrder(gridPlayerBoard);
        enforceGridChildrenViewOrder(gridEnemyBoard);

        // Restaurar barcos revelados que fueron guardados en una partida anterior
        restoreRevealedSunkenShips();

        lblPlayerName.setText("Comandante: " + gameManager.getNickname());
        refreshCounters();
        lblTurnStatus.setText(gameManager.isHumanTurn() ? "🎯 TURNO DEL JUGADOR" : "🤖 TURNO DE LA IA");
        actionStatusLabel.setText("Selecciona una coordenada en el radar enemigo.");
    }

    private void buildGrid(GridPane grid, boolean isInteractive) {
        grid.getChildren().clear();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Pane cell = new Pane();
                cell.setPrefSize(32, 32);
                cell.setId("cell_" + row + "_" + col);
                cell.setStyle("-fx-border-color: rgba(0, 255, 255, 0.2); -fx-background-color: rgba(10, 20, 40, 0.6);");
                if (isInteractive) {
                    cell.setOnMouseEntered(e -> cell.setStyle("-fx-border-color: #00ffff; -fx-background-color: rgba(0, 255, 255, 0.25);") );
                    cell.setOnMouseExited(e -> cell.setStyle("-fx-border-color: rgba(0, 255, 255, 0.2); -fx-background-color: rgba(10, 20, 40, 0.6);") );
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
            targetCell.setOnMouseClicked(null);
            targetCell.setOnMouseEntered(null);
            targetCell.setOnMouseExited(null);

            actionStatusLabel.setText("Resultado: " + result);
            refreshCounters();
            persistGame();

            // Si el disparo hundió un barco, revelarlo en el GridPane
            if (result == CellState.SUNK) {
                revealSunkenShip(c);
            }

            if (gameManager.getMachine().getBoard().isFleetDefeated()) {
                machineExecutor.shutdownNow();
                persistGame();
                SceneManager.getInstance().changeScene("gameover-view.fxml");
                return;
            }

            if (result == CellState.WATER) {
                scheduleMachineTurn();
            } else {
                lblTurnStatus.setText("🎯 TURNO DEL JUGADOR");
                actionStatusLabel.setText("Has acertado, sigue disparando.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error al procesar disparo", e);
            actionStatusLabel.setText("Coordenada inválida o error al disparar: " + e.getMessage());
        }
    }

    @FXML
    private void onViewEnemyShipsButtonClick() {
        showingEnemyShips = !showingEnemyShips;
        gridEnemyBoard.getChildren().removeIf(n -> n instanceof ShipView);

        if (showingEnemyShips) {
            btnToggleEnemyBoard.setText("🙈 Ocultar Barcos Enemigos");
            actionStatusLabel.setText("¡Satélite activado! Viendo la ubicación de la flota enemiga.");

            for (Ship ship : gameManager.getMachine().getBoard().getFleet().getShips()) {
                ShipView view = ShipView.placeOnGrid(gridEnemyBoard, ship);
                view.setMouseTransparent(true);
                // store reference so we can update its visual state later (e.g., mark sunk)
                view.setUserData(ship);
                // if this ship was already revealed as sunken previously, mark its view
                if (gameManager.getRevealedSunkenShips().contains(ship)) {
                    view.markSunk();
                }
            }
        } else {
            btnToggleEnemyBoard.setText("👁 Ver Barcos Enemigos");
            actionStatusLabel.setText("Modo radar normal restaurado.");
            buildGrid(gridEnemyBoard, true);
            ModelToViewMapper.bindBoardToGrid(gameManager.getMachine().getBoard(), gridEnemyBoard, false);

            // Volver a mostrar los barcos hundidos previamente revelados (persistidos en GameManager)
            for (Ship s : gameManager.getRevealedSunkenShips()) {
                try {
                    ShipView v = ShipView.from(s);
                    v.setMouseTransparent(true);
                    Coordinate start = s.getCells().get(0).getCoordinate();
                    int colSpan = s.getOrientation() == Orientation.HORIZONTAL ? s.getSize() : 1;
                    int rowSpan = s.getOrientation() == Orientation.VERTICAL ? s.getSize() : 1;
                    gridEnemyBoard.add(v, start.getCol(), start.getRow(), colSpan, rowSpan);
                    // Marcar hundido y ajustar z-order después de añadir al grid
                    v.markSunk();
                    for (var cell : s.getCells()) {
                        Coordinate cc = cell.getCoordinate();
                        String id = "cell_" + cc.getRow() + "_" + cc.getCol();
                        var node = gridEnemyBoard.lookup("#" + id);
                        if (node instanceof Pane p) {
                            p.toFront();
                        }
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.FINE, "No se pudo re-anadir barco hundido al tablero", ex);
                }
            }
        }
    }

    @FXML
    private void onSurrenderButtonClick() {
        persistGame();
        machineExecutor.shutdownNow();
        SceneManager.getInstance().changeScene("menu-view.fxml");
    }

    private void persistGame() {
        GameSession.getInstance().saveCurrentGame();
    }

    /**
     * Restaura los barcos hundidos que fueron guardados en una partida anterior.
     * Se ejecuta durante la inicialización para mostrar los barcos hundidos persisted.
     */
    private void restoreRevealedSunkenShips() {
        for (Ship s : gameManager.getRevealedSunkenShips()) {
            try {
                ShipView v = ShipView.from(s);
                v.setMouseTransparent(true);
                Coordinate start = s.getCells().get(0).getCoordinate();
                int colSpan = s.getOrientation() == Orientation.HORIZONTAL ? s.getSize() : 1;
                int rowSpan = s.getOrientation() == Orientation.VERTICAL ? s.getSize() : 1;
                gridEnemyBoard.add(v, start.getCol(), start.getRow(), colSpan, rowSpan);
                // Aplicar estilo hundido y ajustar z-order
                v.markSunk();
                for (var cell : s.getCells()) {
                    Coordinate cc = cell.getCoordinate();
                    String id = "cell_" + cc.getRow() + "_" + cc.getCol();
                    var node = gridEnemyBoard.lookup("#" + id);
                    if (node instanceof Pane p) {
                        p.toFront();
                    }
                }
            } catch (Exception ex) {
                LOGGER.log(Level.FINE, "No se pudo restaurar barco hundido al tablero", ex);
            }
        }
    }

    private void refreshCounters() {
        int humanRemaining = gameManager.getHuman().getBoard().getFleet().getRemainingShipsCount();
        int machineRemaining = gameManager.getMachine().getBoard().getFleet().getRemainingShipsCount();
        lblPlayerShipsLeft.setText("Flota Aliada: Restantes " + humanRemaining + "/10 | Hundidos " + gameManager.getHuman().getBoard().getFleet().getSunkShipsCount() + "/10");
        lblEnemyShipsLeft.setText("Flota Enemiga: Restantes " + machineRemaining + "/10 | Hundidos " + gameManager.getMachine().getBoard().getFleet().getSunkShipsCount() + "/10");
    }

    /**
     * Forzar viewOrder en los hijos del grid para garantizar que las marcas
     * de disparo (Panes) se pinten por encima de los ShipView.
     */
    private void enforceGridChildrenViewOrder(GridPane grid) {
        for (var node : grid.getChildren()) {
            if (node instanceof Pane) {
                node.setViewOrder(-1.0);
            } else if (node instanceof ShipView) {
                node.setViewOrder(1.0);
            } else {
                node.setViewOrder(0.0);
            }
        }
    }

    private void scheduleMachineTurn() {
        if (machineTurnScheduled) {
            return;
        }
        machineTurnScheduled = true;
        lblTurnStatus.setText("🤖 TURNO DE LA IA");
        machineExecutor.schedule(this::executeMachineTurnStep, 600, TimeUnit.MILLISECONDS);
    }

    private void executeMachineTurnStep() {
        try {
            CellState result = gameManager.machineShoot();
            persistGame();
            Platform.runLater(this::refreshCounters);

            if (gameManager.getHuman().getBoard().isFleetDefeated()) {
                machineExecutor.shutdownNow();
                Platform.runLater(() -> {
                    persistGame();
                    SceneManager.getInstance().changeScene("gameover-view.fxml");
                });
                machineTurnScheduled = false;
                return;
            }

            if (result == CellState.WATER) {
                Platform.runLater(() -> {
                    lblTurnStatus.setText("🎯 TURNO DEL JUGADOR");
                    actionStatusLabel.setText("La IA falló. Vuelve a disparar.");
                });
                machineTurnScheduled = false;
            } else {
                machineExecutor.schedule(this::executeMachineTurnStep, 600, TimeUnit.MILLISECONDS);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error en el turno de la IA", ex);
            machineTurnScheduled = false;
        }
    }

    /**
     * Revela un barco enemigo hundido en el GridPane.
     * Busca el barco en la coordenada disparada y lo muestra en el tablero enemigo.
     */
    private void revealSunkenShip(Coordinate hitCoordinate) {
        try {
            Cell cell = gameManager.getMachine().getBoard().getCell(hitCoordinate);
            if (cell != null && cell.getShip() != null) {
                Ship sunkenShip = cell.getShip();

                // registrar como revelado para mantenerlo visible al alternar vista y persistir
                gameManager.addRevealedSunkenShip(sunkenShip);

                // Si actualmente estamos mostrando todos los barcos, actualiza su vista
                if (showingEnemyShips) {
                    for (var node : gridEnemyBoard.getChildren()) {
                        if (node instanceof ShipView sv && sv.getUserData() == sunkenShip) {
                            // Si la vista ya está en el grid, marcar como hundida y asegurar z-order
                            sv.markSunk();
                            sv.setMouseTransparent(true);
                            // asegurar panes de las celdas queden por encima
                            for (var c : sunkenShip.getCells()) {
                                Coordinate cc = c.getCoordinate();
                                String id = "cell_" + cc.getRow() + "_" + cc.getCol();
                                var node2 = gridEnemyBoard.lookup("#" + id);
                                if (node2 instanceof Pane p) p.toFront();
                            }
                            enforceGridChildrenViewOrder(gridEnemyBoard);
                            actionStatusLabel.setText("¡Barco enemigo HUNDIDO! 💥");
                            return;
                        }
                    }
                    // si no se encontró (por seguridad), caer hacia la adición normal
                }

                // Mostrar el barco hundido (modo normal)
                ShipView shipView = ShipView.from(sunkenShip);

                Coordinate start = sunkenShip.getCells().get(0).getCoordinate();
                int colSpan = sunkenShip.getOrientation() == Orientation.HORIZONTAL ? sunkenShip.getSize() : 1;
                int rowSpan = sunkenShip.getOrientation() == Orientation.VERTICAL ? sunkenShip.getSize() : 1;

                // Primero añadir la vista al grid, luego aplicar markSunk y ajustar el z-order
                gridEnemyBoard.add(shipView, start.getCol(), start.getRow(), colSpan, rowSpan);
                shipView.markSunk();
                shipView.setMouseTransparent(true);

                // Asegurar que las celdas con marcas (panes) queden por encima del barco
                for (var cell2 : sunkenShip.getCells()) {
                    Coordinate cc = cell2.getCoordinate();
                    String id = "cell_" + cc.getRow() + "_" + cc.getCol();
                    var node3 = gridEnemyBoard.lookup("#" + id);
                    if (node3 instanceof Pane p) {
                        p.toFront();
                    }
                }

                // Forzar orden de renderizado del grid
                enforceGridChildrenViewOrder(gridEnemyBoard);

                actionStatusLabel.setText("¡Barco enemigo HUNDIDO! 💥");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error al revelar barco hundido", ex);
        }
    }
}