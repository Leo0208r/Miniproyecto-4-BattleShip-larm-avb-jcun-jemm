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

/**
 * Controller class for the fleet placement setup phase in Battleship.
 * <p>
 * Manages interactive and automatic positioning of ships onto the human player's
 * board, rotation of ships, resetting placement, and transitioning into the battle scene.
 * </p>
 *
 * @author Leonardo Alexis
 * @author Julio Cesar
 * @author Alejandro Velez
 * @author Juan Esteban Mina
 * @version 1.0
 */

public class PlacementController {

    /** Standard fleet composition order for placing ships. */
    private static final List<ShipType> COMPOSITION = com.example.battleship.model.Fleet.getStandardComposition();
    /** Label displaying the player's nickname. */
    @FXML private Label lblNickname;
    /** Label showing the next ship to be placed. */
    @FXML private Label lblCurrentShip;
    /** Label displaying the current selected ship orientation. */
    @FXML private Label lblOrientation;
    /** Label showing the count of placed ships out of total allowed. */
    @FXML private Label lblShipsPlaced;
    /** Label for showing feedback and status messages to the user. */
    @FXML private Label lblPlacementStatus;
    /** Button for rotating ship orientation between horizontal and vertical. */
    @FXML private Button btnRotate;
    /** Button for automatically placing remaining ships randomly. */
    @FXML private Button btnRandomFill;
    /** Button for clearing the board and restarting placement from scratch. */
    @FXML private Button btnRestartPlacement;
    /** Button to confirm fleet setup and transition to the battle screen. */
    @FXML private Button btnStartBattle;
    /** GridPane container displaying the placement board grid. */
    @FXML private GridPane gridPlacementBoard;

    /** Active game manager handling match state and fleet logic. */
    private GameManager gameManager;
    /** Index tracking the next ship in composition sequence to be placed. */
    private int nextShipIndex;
    /** Selected orientation (Horizontal/Vertical) for placing ships. */
    private Orientation orientation = Orientation.HORIZONTAL;

    /**
     * Initializes the placement view. Loads session data, ensures machine fleet setup,
     * builds interactive board cells, and refreshes the display.
     */
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

    /**
     * Builds the interactive 10x10 grid board layout with mouse click event handlers.
     */
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

    /**
     * Handles manual placement of a ship when a cell in the grid is clicked.
     *
     * @param row Grid row index where placement is attempted.
     * @param col Grid column index where placement is attempted.
     */
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

    /**
     * Refreshes all visual components, text labels, and grid nodes in the scene.
     */
    private void refreshAll() {
        buildGrid();
        renderShips();
        lblNickname.setText("Comandante: " + gameManager.getNickname());
        lblOrientation.setText("Orientación: " + orientation);
        lblShipsPlaced.setText("Barcos colocados: " + gameManager.getHuman().getPlacedShipsCount() + "/10");
        lblCurrentShip.setText("Siguiente barco: " + getCurrentShipLabel());
        btnStartBattle.setDisable(!isPlacementComplete());
    }

    /**
     * Renders visual representations of already placed ships on the grid.
     */
    private void renderShips() {
        Board board = gameManager.getHuman().getBoard();
        for (var ship : board.getFleet().getShips()) {
            ShipView view = ShipView.placeOnGrid(gridPlacementBoard, ship);
            view.setMouseTransparent(true);
        }
    }

    /**
     * Checks whether the human player's fleet placement is fully completed.
     *
     * @return {@code true} if all 10 ships are placed, {@code false} otherwise.
     */
    private boolean isPlacementComplete() {
        return gameManager.getHuman().isFleetComplete();
    }

    /**
     * Formats and returns descriptive text for the current ship pending placement.
     *
     * @return A formatted label string describing the pending ship.
     */
    private String getCurrentShipLabel() {
        if (isPlacementComplete()) {
            return "Flota completa";
        }
        ShipType type = COMPOSITION.get(nextShipIndex);
        return type + " (" + type.getSize() + " celdas)";
    }

    /**
     * Updates the status message label on the placement interface.
     *
     * @param message Message content to be displayed.
     */
    private void updateStatus(String message) {
        lblPlacementStatus.setText(message);
    }

    /**
     * Toggles placement orientation between HORIZONTAL and VERTICAL.
     */
    @FXML
    private void handleRotate() {
        orientation = orientation == Orientation.HORIZONTAL ? Orientation.VERTICAL : Orientation.HORIZONTAL;
        refreshAll();
        updateStatus("Orientación cambiada a " + orientation + ".");
    }

    /**
     * Automatically completes placement of the entire fleet in random positions.
     */
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

    /**
     * Clears all placed ships and resets the placement procedure to initial state.
     */
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

    /**
     * Saves game progress and transitions to the main battle game scene.
     */
    @FXML
    private void handleStartBattle() {
        if (!isPlacementComplete()) {
            updateStatus("Debes colocar los 10 barcos antes de iniciar.");
            return;
        }
        GameSession.getInstance().saveCurrentGame();
        SceneManager.getInstance().changeScene("game-view.fxml");
    }

    /**
     * Saves game session state and returns to the main menu screen.
     */
    @FXML
    private void handleBackToMenu() {
        GameSession.getInstance().saveCurrentGame();
        SceneManager.getInstance().changeScene("menu-view.fxml");
    }
}




