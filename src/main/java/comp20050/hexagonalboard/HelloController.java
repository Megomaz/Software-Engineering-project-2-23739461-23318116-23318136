package comp20050.hexagonalboard;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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

    @FXML
    public void initialize() {
        generateHexBoard();
        updateTurnIndicator(); // Set initial turn label
    }

    private void generateHexBoard() {
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

                    hexagon.setFill(Color.web("#F1A300")); // Default color
                    hexagon.setStroke(Color.BLACK);
                    hexagon.setLayoutX(hexPosition.x);
                    hexagon.setLayoutY(hexPosition.y);

                    // Set a unique ID for debugging
                    hexagon.setId("hexagon-" + q + "-" + r);

                    // Add event listener to handle turns & disable re-clicking
                    hexagon.setOnMouseClicked(event -> {
                        if (Player.attemptPlaceStone(hexagon)) {
                            placeStone(hexagon);
                        }
                    });

                    hexBoardPane.getChildren().add(hexagon);
                }
            }
        }
        hexBoardPane.setRotate(90); // Rotate board
    }

    private void placeStone(Polygon hexagon) {
        // Set color based on current player
        hexagon.setFill(Player.PLAYERS[currentTurn].getId() == 0 ? Color.RED : Color.BLUE);
        hexagon.setOnMouseClicked(null); // Disable further clicks

        // Switch turn
        currentTurn = (currentTurn + 1) % 2;
        updateTurnIndicator();
    }

    private void updateTurnIndicator() {
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