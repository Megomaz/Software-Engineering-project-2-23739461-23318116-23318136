package comp20050.hexagonalboard;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import java.util.*;

public class HelloController {

    @FXML
    Pane hexBoardPane; // Pane where hexagons are drawn

    @FXML
    Polygon hexPrototype; // Reference to the FXML hexagon template

    @FXML
    Label turnLabel; // Label for turn display

    private static final int GRID_RADIUS = 6; // Hex grid range
    private static final double HEX_RADIUS = 24.5; // Distance from center to hex corners
    private int currentTurn = 0; // 0 for Red, 1 for Blue
    private Board board;
    private int redStoneCount = 0;
    private int blueStoneCount = 0;


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

                    // Set a unique ID for the hexagon based on position
                    hexagon.setId("hex-" + id);

                    // Create a Cell object and store it in the hexBoard array
                    int row = q + GRID_RADIUS;
                    int col = r + GRID_RADIUS;
                    board.setCell(row, col, new Cell(row,col));

                    // Add event listener to handle turns & disable re-clicking
                    int finalId = id;
                    hexagon.setOnMouseClicked(event -> placeStone(hexagon, finalId, row, col));

                    hexBoardPane.getChildren().add(hexagon);

                    id++; // Increment the ID for the next hexagon
                }
            }
        }
        hexBoardPane.setRotate(90); // Rotate the board for better alignment
    }

    private void placeStone(Polygon hexagon, int hexId, int row, int col) {
        // Get the cell from the board
        Cell cell = board.getCell(row, col);

        // Avoid placing a stone on an already occupied hexagon
        if (cell.isOccupied()) {
            return; // Ignore if already clicked or has a stone
        }

        // Get the current player
        Player currentPlayer = Player.PLAYERS[currentTurn];

        // Set color based on the current player's turn
        Color stoneColor = currentPlayer.getId() == 0 ? Color.RED : Color.BLUE;

        // Get adjacent cells
        List<Cell> adjacentCells = board.getAdjacentCells(row, col);

        // Check if the move captures any stones
        boolean captured = attemptCapture(row, col, stoneColor, currentPlayer, hexagon);

        // If no capture occurs, ensure the move is not adjacent to the same color
        if (!captured) {
            for (Cell adjacent : adjacentCells) {
                if (adjacent.isOccupied() && adjacent.getStoneColor() == stoneColor) {
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
        if (this.getCurrentTurn() == 0) {
            redStoneCount++;
        } else {
            blueStoneCount++;
        }

        // Add the stone to the hex board
        hexBoardPane.getChildren().add(cell.getStone());

        // Switch the player's turn if they have made a NCP move
        if (!captured){
            currentTurn = (currentTurn + 1) % 2;
        }

        UIHandler.updateTurnIndicator(Player.PLAYERS[currentTurn], turnLabel);

        // Highlight valid cells after the turn is made
        // highlightValidCells(Player.PLAYERS[currentTurn]);
    }

    private boolean attemptCapture(int row, int col, Color stoneColor, Player currentPlayer, Polygon hexagon) {
        List<Cell> adjacentCells = board.getAdjacentCells(row, col);
        List<Cell> cellsToCheck = new ArrayList<>(adjacentCells);
        Set<Cell> visitedCells = new HashSet<>();

        int playerStoneCount = 0;  // Count of adjacent stones of player's color
        int opponentStoneCount = 0; // Count of adjacent stones of opponent's color

        Color opponentColor = (stoneColor == Color.RED) ? Color.BLUE : Color.RED;

        // Traverse adjacent stones to determine capture
        while (!cellsToCheck.isEmpty()) {
            Cell currentCheckingCell = cellsToCheck.remove(0);

            if (!visitedCells.contains(currentCheckingCell) && currentCheckingCell.isOccupied()) {
                visitedCells.add(currentCheckingCell);

                Color adjacentColor = currentCheckingCell.getStoneColor();
                if (adjacentColor == stoneColor) {
                    // Increment player stone count for adjacent player stones
                    playerStoneCount++;
                } else if (adjacentColor == opponentColor) {
                    // Increment opponent stone count for adjacent opponent stones
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

        // **Check if capture conditions are met based on counts**
        if (playerStoneCount >= opponentStoneCount && opponentStoneCount >= 1) {
            // If player has more adjacent stones than opponent, capture the opponent's stones
            for (Cell currentCheckingCell : visitedCells) {
                if (currentCheckingCell.isOccupied() && currentCheckingCell.getStoneColor() == opponentColor) {
                    // Remove the stone from the UI
                    hexBoardPane.getChildren().remove(currentCheckingCell.getStone());
                    if (this.getCurrentTurn() == 1) {
                        redStoneCount -= playerStoneCount;
                    } else {
                        blueStoneCount-= opponentStoneCount;
                    }
                    // Remove from the board state
                    currentCheckingCell.clear();
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
        return new Point(q, r, -q - r, x + 600, y + 200);
    }

    /*
    // Method to highlight valid cells based on the current player
    private void highlightValidCells(Player currentPlayer) {
        for (int q = -GRID_RADIUS; q <= GRID_RADIUS; q++) {
            for (int r = -GRID_RADIUS; r <= GRID_RADIUS; r++) {
                int s = -q - r; // Hexagonal coordinate calculation
                if (s >= -GRID_RADIUS && s <= GRID_RADIUS) {
                    // Check if the current cell is valid for placement
                    if (board.validatePlacement(q + GRID_RADIUS, r + GRID_RADIUS, currentPlayer)) {
                        // Highlight the cell (e.g., change its border color or fill)
                        Polygon hexagon = (Polygon) hexBoardPane.lookup("#hex-" + (q + GRID_RADIUS) + "-" + (r + GRID_RADIUS));
                        if (hexagon != null) {
                            hexagon.setStroke(Color.GREEN); // Highlight valid cells with green border
                        }
                    }
                }
            }
        }
    }
    */

    public int getCurrentTurn() {
        return currentTurn;
    }

    public Board getBoard() {
        return board;
    }



    public int getBlueStoneCount() {
        return blueStoneCount;
    }
    public int getRedStoneCount(){
        return redStoneCount;
    }

    /* TODO: Implement the logic to check for a winner.
    public boolean checkwin() {
        // This will be implemented later
    }
    */
}
