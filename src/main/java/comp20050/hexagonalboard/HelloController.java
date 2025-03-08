package comp20050.hexagonalboard;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import java.util.*;

public class HelloController {

    @FXML
    private Pane hexBoardPane; // Pane where hexagons are drawn

    @FXML
    private Polygon hexPrototype; // Reference to the FXML hexagon template

    @FXML
    private Label turnLabel; // Label for turn display

    private static final int GRID_RADIUS = 6; // Hex grid range
    private static final double HEX_RADIUS = 24.5; // Distance from center to hex corners
    private int currentTurn = 0; // 0 for Red, 1 for Blue
    private Board board;

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
        // Avoid placing a stone on an already occupied hexagon
        if (!Player.attemptPlaceStone(hexagon)) {
            return; // Ignore if already clicked or has a stone
        }

        // Get the cell from the board
        Cell cell = board.getCell(row, col); // Get cell from board

        // Get the current player
        Player currentPlayer = Player.PLAYERS[currentTurn];

        // Set color based on the current player's turn
        Color stoneColor = currentPlayer.getId() == 0 ? Color.RED : Color.BLUE;

        // Get the adjacent cells
        List<Cell> adjacentCells = board.getAdjacentCells(row, col);

        // List to store cells that are part of the current search path
        List<Cell> cellsToCheck = new ArrayList<>(adjacentCells);

        // Variables to count the number of red and blue stones
        int redCount = 0;
        int blueCount = 0;

        // Set to track visited cells
        Set<Cell> visitedCells = new HashSet<>();

        // Loop through adjacent cells
        while (!cellsToCheck.isEmpty()) {
            Cell currentCheckingCell = cellsToCheck.remove(0);  // Get the next cell to check

            // If the current checking cell is occupied and not visited
            if (!visitedCells.contains(currentCheckingCell) && currentCheckingCell.isOccupied()) {
                visitedCells.add(currentCheckingCell);  // Mark the cell as visited

                // Count the red or blue stones
                Color adjacentColor = currentCheckingCell.getStoneColor();
                if (adjacentColor == Color.RED) {
                    redCount++;
                } else if (adjacentColor == Color.BLUE) {
                    blueCount++;
                }

                // Add adjacent cells of the current checking cell to the list to continue searching
                List<Cell> nextAdjacentCells = board.getAdjacentCells((int) currentCheckingCell.getX(), (int) currentCheckingCell.getY());
                for (Cell nextCell : nextAdjacentCells) {
                    // Only add the cell if it's not already visited
                    if (!visitedCells.contains(nextCell)) {
                        cellsToCheck.add(nextCell);  // Add to list of cells to check
                    }
                }
            }
        }

        // After the loop, print the counts of red and blue stones in the adjacent cells
        System.out.println("Red stones in adjacent cells: " + redCount);
        System.out.println("Blue stones in adjacent cells: " + blueCount);

        // Check if the current player's color count + 1 is greater than or equal to the opponent's count,
        // and there is at least 1 stone of the opponent's color in the adjacent cells.
        if ((stoneColor == Color.RED && redCount + 1 >= blueCount && blueCount >= 1 && redCount >= 1) ||
                (stoneColor == Color.BLUE && blueCount + 1 >= redCount && redCount >= 1 && blueCount >= 1)) {

            // Loop through the visited cells and flip opponent's stones
            for (Cell currentCheckingCell : visitedCells) {
                // If the current cell has the opponent's color, flip it
                if (currentCheckingCell.isOccupied()) {
                    if (stoneColor == Color.RED && currentCheckingCell.getStoneColor() == Color.BLUE) {
                        currentCheckingCell.getStone().setFill(Color.RED);
                        currentCheckingCell.occupy(currentPlayer, hexagon);
                        System.out.println("You have captured " + blueCount + " blue stones");
                        System.out.println("Flipping blue stone at (" + currentCheckingCell.getX() + ", " + currentCheckingCell.getY() + ") to red.");
                    } else if (stoneColor == Color.BLUE && currentCheckingCell.getStoneColor() == Color.RED) {
                        currentCheckingCell.getStone().setFill(Color.BLUE);
                        System.out.println("You have captured " + redCount + " red stones");
                        System.out.println("Flipping red stone at (" + currentCheckingCell.getX() + ", " + currentCheckingCell.getY() + ") to blue.");
                    }
                }
            }

            // Update counts after flipping stones
            if (stoneColor == Color.RED) {
                redCount = redCount + blueCount + 1;  // Red count increases by the number of flipped blue stones
                blueCount = 0;  // No blue stones left in adjacent cells
            } else{
                blueCount = blueCount + redCount + 1;  // Blue count increases by the number of flipped red stones
                redCount = 0;  // No red stones left in adjacent cells
            }

            System.out.println("Red stones after flipping: " + redCount);
            System.out.println("Blue stones after flipping: " + blueCount);
        } else {
            System.out.println("Condition not met for flipping stones.");
        }

        System.out.println(cell.getCoordinate());

        // Print the adjacent cells
        System.out.println(board.getAdjacentCellsAsString(row, col));

        // Mark the cell as occupied
        cell.occupy(currentPlayer, hexagon);

        // Disable further clicks on this hexagon
        hexagon.setOnMouseClicked(null);

        // Add the stone to the hex board
        hexBoardPane.getChildren().add(cell.getStone());

        // Switch the player's turn
        currentTurn = (currentTurn + 1) % 2;
        UIHandler.updateTurnIndicator(Player.PLAYERS[currentTurn], turnLabel);

        // Highlight valid cells after the turn is made
        // highlightValidCells(Player.PLAYERS[currentTurn]);
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


    /* TODO: Implement the logic to check for a winner.
    public boolean checkwin() {
        // This will be implemented later
    }
    */
}
