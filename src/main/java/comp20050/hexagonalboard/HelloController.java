package comp20050.hexagonalboard;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import java.util.*;

public class HelloController {

    @FXML
    Pane hexBoardPane; // Pane where hexagons are drawn

    @FXML
    Polygon hexPrototype; // Reference to the FXML hexagon template

    @FXML
    Label turnLabel; // Label for turn display

    @FXML
    private Label winnerLabel; // Label for the winner

    @FXML
    Button RestartButton; // Button for restarting

    @FXML
    private void exitGame() {
        ((javafx.stage.Stage) hexBoardPane.getScene().getWindow()).close();
    }


    private static final int GRID_RADIUS = 6; // Hex grid range
    private static final double HEX_RADIUS = 24.5; // Distance from center to hex corners
    private int currentTurn = 0; // 0 for Red, 1 for Blue
    private Board board;
    private int redStoneCount = 0;
    private int blueStoneCount = 0;
    private int redMoves = 0;
    private int blueMoves = 0;

    private Map<String, Circle> previewStones = new HashMap<>(); // Track preview stones for removal


    @FXML
    public void initialize() {
        board = new Board(GRID_RADIUS * 2 + 1); // Initialize the board
        generateHexBoard();
        UIHandler.updateTurnIndicator(Player.PLAYERS[currentTurn], turnLabel); // Set initial turn label
    }

    private void generateHexBoard() {
        int id = 1;  // Start assigning IDs from 1
        for (int q = -GRID_RADIUS; q <= GRID_RADIUS; q++) {
            for (int r = -GRID_RADIUS; r <= GRID_RADIUS; r++) {
                int s = -q - r;
                if (s >= -GRID_RADIUS && s <= GRID_RADIUS) {
                    Point hexPosition = calculateHexPixel(q, r);
                    Polygon hexagon = new Polygon();

                    // Copy points from prototype
                    for (Double point : hexPrototype.getPoints()) {
                        hexagon.getPoints().add(point);
                    }

                    hexagon.setFill(Color.web("#F1A300")); // Default color (hexagon background)
                    hexagon.setStroke(Color.BLACK);
                    hexagon.setLayoutX(hexPosition.x);
                    hexagon.setLayoutY(hexPosition.y);

                    // Sets a unique ID for the hexagon based on position
                    hexagon.setId("hex-" + id);

                    // Creates a Cell object and stores it in the hexBoard array
                    int row = q + GRID_RADIUS;
                    int col = r + GRID_RADIUS;
                    board.setCell(row, col, new Cell(row, col));

                    // An event listener to handle turns & disable re-clicking
                    hexagon.setOnMouseClicked(event -> placeStone(hexagon, row, col));

                    // An event listeners to handle hover (stone preview)
                    hexagon.setOnMouseEntered(event -> previewMoves(hexagon, row, col));

                    // Hides the preview stone when mouse exits from previous cell
                    hexagon.setOnMouseExited(event -> clearPreview(row, col));

                    hexBoardPane.getChildren().add(hexagon);

                    id++; // The ID is incremented for the next hexagon
                }
            }
        }
        hexBoardPane.setRotate(90); // The board is rotated for better alignment
    }

    private void placeStone(Polygon hexagon, int row, int col) {
        // Gets the cell from the board
        Cell cell = board.getCell(row, col);

        // Prevents placing a stone on an already occupied hexagon
        if (cell.isOccupied()) {
            return; // Ignore if already clicked or has a stone
        }

        // Gets the current player
        Player currentPlayer = Player.PLAYERS[currentTurn];

        // Sets the color based on the current player's turn
        Color stoneColor = currentPlayer.getId() == 0 ? Color.RED : Color.BLUE;

        // Gets the adjacent cells
        List<Cell> adjacentCells = board.getAdjacentCells(row, col);

        // Checks if the move captures any stones
        boolean captured = attemptCapture(row, col, stoneColor);

        // If no capture occurs, ensure the move is not adjacent to the same color
        if (!captured) {
            for (Cell adjacentCell : adjacentCells) {
                if (adjacentCell.isOccupied() && adjacentCell.getStoneColor() == stoneColor) {
                    System.out.println("Invalid move: You cannot place next to your own color without capturing.");
                    return; // Prevent placement if next to the same color without capturing
                }
            }
        }

        System.out.println(cell.getCoordinate());

        // Print the adjacent cells
        System.out.println(board.getAdjacentCellsAsString(row, col));

        // Mark the cell as occupied
        cell.occupy(currentPlayer, hexagon);

        // Track stone count and moves
        if (currentPlayer.getId() == 0) {
            redStoneCount++;
            redMoves++;
        } else {
            blueStoneCount++;
            blueMoves++;
        }

        // Add the stone to the hex board
        hexBoardPane.getChildren().add(cell.getStone());

        // Switch the player's turn if they have made a NCP move
        if (!captured) {
            currentTurn = (currentTurn + 1) % 2;
        }

        UIHandler.updateTurnIndicator(Player.PLAYERS[currentTurn], turnLabel);

        checkForWinner();

    }


    private void previewMoves(Polygon hexagon, int row, int col) {

        Cell cell = board.getCell(row, col);

        if (cell.isOccupied()) {
            return;
        }
        // Check if preview stone already exists for this cell
        String hexId = "hex-" + row + "-" + col;
        if (!previewStones.containsKey(hexId)) {
            // Create preview stone for the current player
            Circle previewStone = new Circle(HEX_RADIUS / 2);
            previewStone.setLayoutX(hexagon.getLayoutX());
            previewStone.setLayoutY(hexagon.getLayoutY());
            previewStone.setFill(Player.PLAYERS[currentTurn].getId() == 0 ? Color.RED : Color.BLUE);
            previewStone.setVisible(true);

            previewStone.setMouseTransparent(true);
            // Add preview stone to hexBoardPane
            hexBoardPane.getChildren().add(previewStone);

            // Store the reference of the preview stone for later removal
            previewStones.put(hexId, previewStone);

        }

    }

    private void clearPreview(int row, int col) {
        // Find the preview stone reference
        String hexId = "hex-" + row + "-" + col;
        Circle previewStone = previewStones.get(hexId);

        // If preview stone exists, remove it
        if (previewStone != null) {
            hexBoardPane.getChildren().remove(previewStone);
            previewStones.remove(hexId); // Clean up the map
        }
    }

    private boolean attemptCapture(int row, int col, Color stoneColor) {
        List<Cell> adjacentCells = board.getAdjacentCells(row, col);
        List<Cell> cellsToCheck = new ArrayList<>(adjacentCells);
        Set<Cell> visitedCells = new HashSet<>();

        int playerStoneCount = 0;  // Count of adjacent stones of player's color
        int opponentStoneCount = 0; // Count of adjacent stones of opponent's color

        Color opponentColor = (stoneColor == Color.RED) ? Color.BLUE : Color.RED;

        // Traverses the adjacent stones to determine capture
        while (!cellsToCheck.isEmpty()) {
            Cell currentCheckingCell = cellsToCheck.removeFirst();

            if (!visitedCells.contains(currentCheckingCell) && currentCheckingCell.isOccupied()) {
                visitedCells.add(currentCheckingCell);

                Color adjacentColor = currentCheckingCell.getStoneColor();
                if (adjacentColor == stoneColor) {
                    // Increment the player's stone count for adjacent player stones
                    playerStoneCount++;
                } else if (adjacentColor == opponentColor) {
                    // Increment the opponent's stone count for adjacent opponent stones
                    opponentStoneCount++;
                }

                // Only add cells adjacent to the current cell for further checking
                List<Cell> nextAdjacentCells = board.getAdjacentCells(
                        (int) currentCheckingCell.getX(),
                        (int) currentCheckingCell.getY()
                );
                for (Cell nextCell : nextAdjacentCells) {
                    if (!visitedCells.contains(nextCell)) {
                        cellsToCheck.add(nextCell);
                    }
                }
            }
        }

        // Checks if capture conditions are met based on counts
        if (playerStoneCount >= opponentStoneCount && opponentStoneCount >= 1) {
            // If the player has more adjacent stones than the opponent, capture the opponent's stones
            for (Cell currentCheckingCell : visitedCells) {
                if (currentCheckingCell.isOccupied() && currentCheckingCell.getStoneColor() == opponentColor) {
                    // Removes the stone from the UI
                    hexBoardPane.getChildren().remove(currentCheckingCell.getStone());

                    // Removes the cell from the board state
                    currentCheckingCell.clear();

                    // Decrements the opponent's stone count
                    if (opponentColor == Color.RED) {
                        redStoneCount--;
                    } else {
                        blueStoneCount--;
                    }
                }
            }
            System.out.println("Captured stones. Player Stones: " + playerStoneCount + ", Opponent Stones: " + opponentStoneCount);
            return true; // Capture successful
        }
        return false; // No valid capture
    }

    private Point calculateHexPixel(int q, int r) {
        double x = HEX_RADIUS * (Math.sqrt(3) * q + (Math.sqrt(3) / 2) * r);
        double y = HEX_RADIUS * (1.5 * r);
        return new Point(q, r, -q - r, x + 200, y + 200);
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public Board getBoard() {
        return board;
    }

    // A button to restart game at any point
    public void restartGame() {
        // Reset counters and state
        currentTurn = 0;
        redStoneCount = 0;
        blueStoneCount = 0;
        redMoves = 0;
        blueMoves = 0;

        board = new Board(GRID_RADIUS * 2 + 1);
        hexBoardPane.getChildren().clear();
        generateHexBoard();

        // Reset UI labels and visibility
        turnLabel.setText("Red's Turn");
        turnLabel.setTextFill(Color.RED);

        if (winnerLabel != null) {
            winnerLabel.setVisible(false);
        }

        previewStones.clear();
    }


    public boolean checkForWinner() {
        if (redStoneCount == 0 && blueMoves >= 2) {
            endGame("Blue wins!");
            return true;
        } else if (blueStoneCount == 0 && redMoves >= 2) {
            endGame("Red wins!");
            return true;
        }
        return false;
    }

    private void endGame(String winnerMessage) {
        for (javafx.scene.Node node : hexBoardPane.getChildren()) {
            node.setDisable(true);
        }

        // Update turn label to show winner
        turnLabel.setText(winnerMessage);
        turnLabel.setTextFill(winnerMessage.contains("Red") ? Color.RED : Color.BLUE);

        // Show winner label and buttons
        winnerLabel.setText(winnerMessage);
        winnerLabel.setVisible(true);
    }

}
