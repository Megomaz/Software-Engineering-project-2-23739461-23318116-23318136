package comp20050.hexagonalboard;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Circle;

public class HelloController {

    @FXML
    private Pane hexBoardPane; // Pane where hexagons are drawn

    @FXML
    private Polygon hexPrototype; // Reference to the FXML hexagon template

    @FXML
    private Label turnLabel; // Label for turn display

    private static final int GRID_RADIUS = 6; // Hex grid range
    private static final double HEX_RADIUS = 24.5; // Distance from center to hex corners
    private boolean isRedTurn = true; // Track current player's turn
    private int currentTurn = 0; // 0 for Red, 1 for Blue

    @FXML
    public void initialize() {
        generateHexBoard();
        updateTurnIndicator(); // Set initial turn label
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

                    // Add event listener to handle turns & disable re-clicking
                    int finalId = id;
                    hexagon.setOnMouseClicked(event -> placeStone(hexagon, finalId));

                    hexBoardPane.getChildren().add(hexagon);

                    id++; // Increment the ID for the next hexagon
                }
            }
        }
        hexBoardPane.setRotate(90); // Rotate the board for better alignment
    }

    private void placeStone(Polygon hexagon, int hexId) {
        // Avoid placing a stone on an already occupied hexagon
        if (!hexagon.getFill().equals(Color.web("#F1A300"))) {
            return; // Ignore if already clicked or has a stone
        }

        // Set color based on the current player's turn
        Color stoneColor = isRedTurn ? Color.RED : Color.BLUE;

        // Create the stone (circle) with a radius that fits within the hexagon
        Circle stone = new Circle(HEX_RADIUS / 2); // Stone radius should be half of hexagon's radius
        stone.setFill(stoneColor);
        stone.setLayoutX(hexagon.getLayoutX());  // Align stone with hexagon's center
        stone.setLayoutY(hexagon.getLayoutY());

        // Add the stone to the hex board
        hexBoardPane.getChildren().add(stone);

        // Disable further clicks on this hexagon
        hexagon.setOnMouseClicked(null);

        // Switch the player's turn
        isRedTurn = !isRedTurn;
        currentTurn = (currentTurn + 1) % 2;
        updateTurnIndicator();
    }

    private void updateTurnIndicator() {
        // Update the label text based on whose turn it is
        if (isRedTurn) {
            turnLabel.setText("Red's Turn");
            turnLabel.setTextFill(Color.RED); // Set label color to red
        } else {
            turnLabel.setText("Blue's Turn");
            turnLabel.setTextFill(Color.BLUE); // Set label color to blue
        }

        Player currentPlayer = Player.PLAYERS[currentTurn];
        turnLabel.setText(currentPlayer.getName() + "'s Turn");
        turnLabel.setTextFill(currentPlayer.getId() == 0 ? Color.RED : Color.BLUE);
    }

    private Point calculateHexPixel(int q, int r) {
        double x = HEX_RADIUS * (Math.sqrt(3) * q + (Math.sqrt(3) / 2) * r);
        double y = HEX_RADIUS * (1.5 * r);
        return new Point(q, r, -q - r, x + 600, y + 200);
    }

    private static class Point {
        double x, y;
        int q, r, s;

        Point(int q, int r, int s, double x, double y) {
            this.q = q;
            this.r = r;
            this.s = s;
            this.x = x;
            this.y = y;
        }
    }
}
