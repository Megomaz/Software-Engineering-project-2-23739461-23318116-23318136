package comp20050.hexagonalboard;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;  // paint colours
import javafx.scene.shape.Polygon;

public class HelloController {

    @FXML
    private Pane hexBoardPane; // Pane where hexagons are drawn

    @FXML
    private Polygon hexPrototype; // Reference to the FXML hexagon template// initilaised in hello-view.fxml makes code more efficient

    private static final int GRID_RADIUS = 3; // Hex grid range
    private static final double HEX_RADIUS = 30; // Distance from center to hex corners

    @FXML
    public void initialize() {
        generateHexBoard();
    }

    private void generateHexBoard() {
        for (int q = -GRID_RADIUS; q <= GRID_RADIUS; q++) {
            for (int r = -GRID_RADIUS; r <= GRID_RADIUS; r++) {
                int s = -q - r;
                if (s >= -GRID_RADIUS && s <= GRID_RADIUS) {
                    Point hexPosition = calculateHexPixel(q, r);
                    Polygon hexagon = createHexCopy(hexPosition.x, hexPosition.y);
                    hexBoardPane.getChildren().add(hexagon);
                }
            }
        }
    }

    private Point calculateHexPixel(int q, int r) {
        double x = HEX_RADIUS * Math.sqrt(3) * (q + 0.5 * r); // Adjust the horizontal distance
        double y = HEX_RADIUS * 1.5 * r; // Adjust the vertical distance
        return new Point(x + 200, y + 200); // Offset to center the board
    }


    private Polygon createHexCopy(double x, double y) {
        Polygon newHex = new Polygon();
        newHex.getPoints().addAll(hexPrototype.getPoints()); // Copy template points
        newHex.setFill(Color.web("#1f78ff"));
        newHex.setStroke(Color.BLACK);
        newHex.setLayoutX(x);
        newHex.setLayoutY(y);
        return newHex;
    }

    private static class Point {
        double x, y;
        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
