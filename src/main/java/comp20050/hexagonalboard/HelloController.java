package comp20050.hexagonalboard;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

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
                    board.setCell(row, col, new Cell(hexPosition.x, hexPosition.y));

                    // Add event listener to handle turns & disable re-clicking
                    int finalId = id;
                    hexagon.setOnMouseClicked(event -> placeStone(hexagon, finalId,row,col));

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

        // Create the stone (circle) with a radius that fits within the hexagon
        Circle stone = new Circle(HEX_RADIUS / 2); // Stone radius should be half of hexagon's radius
        stone.setFill(stoneColor);
        stone.setLayoutX(hexagon.getLayoutX());  // Align stone with hexagon's center
        stone.setLayoutY(hexagon.getLayoutY());

        // Add the stone to the hex board
        hexBoardPane.getChildren().add(stone);

        // Mark the cell as occupied
        cell.occupy();

        // Disable further clicks on this hexagon
        hexagon.setOnMouseClicked(null);

        // Switch the player's turn
        currentTurn = (currentTurn + 1) % 2;
        UIHandler.updateTurnIndicator(Player.PLAYERS[currentTurn], turnLabel);
    }

    private Point calculateHexPixel(int q, int r) {
        double x = HEX_RADIUS * (Math.sqrt(3) * q + (Math.sqrt(3) / 2) * r);
        double y = HEX_RADIUS * (1.5 * r);
        return new Point(q, r, -q - r, x + 600, y + 200);
    }

    /* TODO: Implement the logic to check for a winner.
    public boolean checkwin() {
        // This will be implemented later
    }
    */
}