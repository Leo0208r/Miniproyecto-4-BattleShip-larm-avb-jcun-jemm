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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main controller for the Battleship gameplay view.
 * <p>
 * Manages player interactions on the enemy grid, handles turn scheduling for the AI,
 * updates game status labels, and renders visual markers (e.g., water images and hits).
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */
public class GameController {

    /**
     * Logger instance for tracking application events and error handling.
     */
    private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());
    /**
     * GridPane representing the human player's board.
     */
    @FXML
    private GridPane gridPlayerBoard;
    /**
     * GridPane representing the AI opponent's radar grid.
     */
    @FXML
    private GridPane gridEnemyBoard;
    /**
     * Label displaying the active player's nickname.
     */
    @FXML
    private Label lblPlayerName;
    /**
     * Label showing the count of remaining ships for the human player.
     */
    @FXML
    private Label lblPlayerShipsLeft;
    /**
     * Label indicating whose turn is currently active.
     */
    @FXML
    private Label lblTurnStatus;
    /**
     * Label showing the count of remaining ships for the enemy player.
     */
    @FXML
    private Label lblEnemyShipsLeft;
    /**
     * Label displaying outcome messages for the last performed action.
     */
    @FXML
    private Label actionStatusLabel;
    /**
     * Toggle button for showing or hiding enemy ships via satellite view.
     */
    @FXML
    private ToggleButton btnToggleEnemyBoard;
    /**
     * Button for surrendering and saving the current game state.
     */
    @FXML
    private Button btnSaveAndExit;

    /**
     * Flag indicating whether the satellite view showing enemy ships is active.
     */
    private boolean showingEnemyShips = false;
    /**
     * Active game manager handling business and match logic.
     */
    private GameManager gameManager;
    /**
     * Executor service for running AI turns asynchronously with delays.
     */
    private final ScheduledExecutorService machineExecutor = Executors.newSingleThreadScheduledExecutor();
    /**
     * Prevents multiple scheduled machine turn executions simultaneously.
     */
    private volatile boolean machineTurnScheduled;

    /**
     * Initializes the controller automatically upon FXML loading.
     * Restores game session data, constructs grid layouts, and binds board models to views.
     */
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

        enforceGridChildrenViewOrder(gridPlayerBoard);
        enforceGridChildrenViewOrder(gridEnemyBoard);

        restoreRevealedSunkenShips();

        lblPlayerName.setText("Comandante: " + gameManager.getNickname());
        refreshCounters();
        lblTurnStatus.setText(gameManager.isHumanTurn() ? "🎯 TURNO DEL JUGADOR" : "🤖 TURNO DE LA IA");
        actionStatusLabel.setText("Selecciona una coordenada en el radar enemigo.");
    }

    /**
     * Builds a grid layout filled with interactive or static {@link Pane} instances.
     *
     * @param grid          The {@link GridPane} to populate.
     * @param isInteractive {@code true} if cells should react to mouse hovering and clicks.
     */
    private void buildGrid(GridPane grid, boolean isInteractive) {
        grid.getChildren().clear();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Pane cell = new Pane();
                cell.setPrefSize(32, 32);
                cell.setId("cell_" + row + "_" + col);
                cell.setStyle("-fx-border-color: rgba(0, 255, 255, 0.2); -fx-background-color: rgba(10, 20, 40, 0.6);");
                if (isInteractive) {
                    cell.setOnMouseEntered(e -> cell.setStyle("-fx-border-color: #00ffff; -fx-background-color: rgba(0, 255, 255, 0.25);"));
                    cell.setOnMouseExited(e -> cell.setStyle("-fx-border-color: rgba(0, 255, 255, 0.2); -fx-background-color: rgba(10, 20, 40, 0.6);"));
                    cell.setOnMouseClicked(this::handleAttackEvent);
                }
                grid.add(cell, col, row);
            }
        }
    }

    /**
     * Handles mouse click events on enemy radar cells to perform attack shots.
     *
     * @param event The mouse event containing target cell details.
     */
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

            if (result == CellState.WATER) {
                try {
                    String imagePath = "/com/example/battleship/fxml/images/water.jpg";
                    var resourceStream = getClass().getResourceAsStream(imagePath);

                    if (resourceStream != null) {
                        Image waterImage = new Image(resourceStream);
                        ImageView imageView = new ImageView(waterImage);

                        imageView.setFitWidth(32);
                        imageView.setFitHeight(32);
                        imageView.setPreserveRatio(false);
                        imageView.setMouseTransparent(true);

                        targetCell.getChildren().add(imageView);
                    } else {
                        LOGGER.warning("No se encontró la imagen en: " + imagePath);
                        targetCell.setStyle("-fx-background-color: #0000FF;");
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error al cargar la imagen water.jpg", e);
                }
                scheduleMachineTurn();
            } else {
                if (result == CellState.SUNK) {
                    revealSunkenShip(c);
                }
                lblTurnStatus.setText("🎯 TURNO DEL JUGADOR");
                actionStatusLabel.setText("Has acertado, sigue disparando.");
            }

            if (gameManager.getMachine().getBoard().isFleetDefeated()) {
                machineExecutor.shutdownNow();
                persistGame();
                SceneManager.getInstance().changeScene("gameover-view.fxml");
                return;
            }

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error al procesar disparo", e);
            actionStatusLabel.setText("Coordenada inválida o error al disparar: " + e.getMessage());
        }
    }

    /**
     * Toggles the satellite view to temporarily show or hide unsunk enemy ships.
     */
    @FXML
    private void onViewEnemyShipsButtonClick() {
        showingEnemyShips = !showingEnemyShips;

        if (showingEnemyShips) {
            btnToggleEnemyBoard.setText("🙈 Ocultar Barcos Enemigos");
            actionStatusLabel.setText("¡Satélite activado! Viendo la ubicación de la flota enemiga.");

            for (Ship ship : gameManager.getMachine().getBoard().getFleet().getShips()) {
                ShipView view = ShipView.placeOnGrid(gridEnemyBoard, ship);
                view.setMouseTransparent(true);
                view.setUserData(ship);
                view.setViewOrder(10.0);

                if (gameManager.getRevealedSunkenShips().contains(ship)) {
                    view.markSunk();
                }
            }
        } else {
            btnToggleEnemyBoard.setText("👁 Ver Barcos Enemigos");
            actionStatusLabel.setText("Modo radar normal restaurado.");

            // Elimina solo las vistas de barcos no hundidos sin destruir las celdas ni borrar water.jpg
            gridEnemyBoard.getChildren().removeIf(node ->
                    node instanceof ShipView && !gameManager.getRevealedSunkenShips().contains(((ShipView) node).getUserData())
            );
        }
    }

    /**
     * Handles surrender events, saving the match state before switching to the main menu.
     */
    @FXML
    private void onSurrenderButtonClick() {
        persistGame();
        machineExecutor.shutdownNow();
        SceneManager.getInstance().changeScene("menu-view.fxml");
    }

    /**
     * Persists the current game session state to disk storage.
     */
    private void persistGame() {
        GameSession.getInstance().saveCurrentGame();
    }

    /**
     * Restores visual representations of enemy ships that were previously sunk.
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
                v.markSunk();
                for (var cell : s.getCells()) {
                    Coordinate cc = cell.getCoordinate();
                    String markId = "mark_" + cc.getRow() + "_" + cc.getCol();
                    var node = gridEnemyBoard.lookup("#" + markId);
                    if (node != null) node.toFront();
                }
            } catch (Exception ex) {
                LOGGER.log(Level.FINE, "No se pudo restaurar barco hundido al tablero", ex);
            }
        }
    }

    /**
     * Refreshes text counters displaying remaining and sunk ships for both sides.
     */
    private void refreshCounters() {
        int humanRemaining = gameManager.getHuman().getBoard().getFleet().getRemainingShipsCount();
        int machineRemaining = gameManager.getMachine().getBoard().getFleet().getRemainingShipsCount();
        lblPlayerShipsLeft.setText("Flota Aliada: Restantes " + humanRemaining + "/10 | Hundidos " + gameManager.getHuman().getBoard().getFleet().getSunkShipsCount() + "/10");
        lblEnemyShipsLeft.setText("Flota Enemiga: Restantes " + machineRemaining + "/10 | Hundidos " + gameManager.getMachine().getBoard().getFleet().getSunkShipsCount() + "/10");
    }

    /**
     * Enforces the rendering view order (z-index) of nodes within a grid board.
     *
     * @param grid The target {@link GridPane} to adjust.
     */
    private void enforceGridChildrenViewOrder(GridPane grid) {
        for (var node : grid.getChildren()) {
            if (node instanceof Pane) {
                node.setViewOrder(100.0);
            } else if (node instanceof ShipView) {
                node.setViewOrder(0.0);
            } else {
                node.setViewOrder(0.0);
            }
        }
    }

    /**
     * Schedules the execution of the AI player's turn with a slight delay.
     */
    private void scheduleMachineTurn() {
        if (machineTurnScheduled) {
            return;
        }
        machineTurnScheduled = true;
        lblTurnStatus.setText("🤖 TURNO DE LA IA");
        machineExecutor.schedule(this::executeMachineTurnStep, 600, TimeUnit.MILLISECONDS);
    }

    /**
     * Executes an AI shooting turn step and handles turn repetitions or match ending.
     */
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
     * Reveals an enemy ship on the board view once it has been destroyed completely.
     *
     * @param hitCoordinate The coordinate where the final fatal shot landed.
     */
    private void revealSunkenShip(Coordinate hitCoordinate) {
        try {
            Cell cell = gameManager.getMachine().getBoard().getCell(hitCoordinate);
            if (cell != null && cell.getShip() != null) {
                Ship sunkenShip = cell.getShip();
                gameManager.addRevealedSunkenShip(sunkenShip);

                if (showingEnemyShips) {
                    for (var node : gridEnemyBoard.getChildren()) {
                        if (node instanceof ShipView sv && sv.getUserData() == sunkenShip) {
                            sv.markSunk();
                            sv.setMouseTransparent(true);
                            for (var c : sunkenShip.getCells()) {
                                Coordinate cc = c.getCoordinate();
                                String markId = "mark_" + cc.getRow() + "_" + cc.getCol();
                                var node2 = gridEnemyBoard.lookup("#" + markId);
                                if (node2 != null) node2.toFront();
                            }
                            enforceGridChildrenViewOrder(gridEnemyBoard);
                            actionStatusLabel.setText("¡Barco enemigo HUNDIDO! 💥");
                            return;
                        }
                    }
                }

                ShipView shipView = ShipView.from(sunkenShip);
                Coordinate start = sunkenShip.getCells().get(0).getCoordinate();
                int colSpan = sunkenShip.getOrientation() == Orientation.HORIZONTAL ? sunkenShip.getSize() : 1;
                int rowSpan = sunkenShip.getOrientation() == Orientation.VERTICAL ? sunkenShip.getSize() : 1;

                gridEnemyBoard.add(shipView, start.getCol(), start.getRow(), colSpan, rowSpan);
                shipView.markSunk();
                shipView.setMouseTransparent(true);

                for (var cell2 : sunkenShip.getCells()) {
                    Coordinate cc = cell2.getCoordinate();
                    String markId = "mark_" + cc.getRow() + "_" + cc.getCol();
                    var node3 = gridEnemyBoard.lookup("#" + markId);
                    if (node3 != null) node3.toFront();
                }

                enforceGridChildrenViewOrder(gridEnemyBoard);
                actionStatusLabel.setText("¡Barco enemigo HUNDIDO! 💥");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error al revelar barco hundido", ex);
        }
    }
}