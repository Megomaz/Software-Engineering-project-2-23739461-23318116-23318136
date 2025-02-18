package comp20050.hexagonalboard;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class HelloController {

    @FXML
    private Pane hexBoardPane; // Pane where hexagons are drawn

    @FXML
    private Polygon hexPrototype; // Reference to the FXML hexagon template

    private static final int GRID_RADIUS = 3; // Hex grid range
    private static final double HEX_RADIUS = 49; // Distance from center to hex corners

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
                    Polygon hexagon = new Polygon();
                    hexagon.getPoints().addAll(hexPrototype.getPoints()); // Copy the points from hexPrototype
                    hexagon.setFill(Color.web("#F1A300"));
                    hexagon.setStroke(Color.BLACK);
                    hexagon.setLayoutX(hexPosition.x);
                    hexagon.setLayoutY(hexPosition.y);

                    // Set a unique ID for the hexagon
                    hexagon.setId("hexagon-" + q + "-" + r); // You can use q and r to make the ID unique



                    hexBoardPane.getChildren().add(hexagon);
                }
            }
        }
    }

    private Point calculateHexPixel(int q, int r) {
        double x = HEX_RADIUS * (Math.sqrt(3) * q + (Math.sqrt(3) / 2) * r);
        double y = HEX_RADIUS * (1.5 * r);
        return new Point(q, r, -q - r, x + 500, y + 100); // Offset to center board
    }

    // Point class (representing hexagonal grid coordinates and calculations)
    private static class Point {
        double x, y;
        int q, r, s;  // Cube coordinates (q, r, s) for hexagons

        // Constructor for initializing q, r, s and x, y
        Point(int q, int r, int s, double x, double y) {
            this.q = q;
            this.r = r;
            this.s = s;
            this.x = x;
            this.y = y;
        }

        // Calculate the length (Manhattan distance from origin)
        public int length() {
            return (Math.abs(q) + Math.abs(r) + Math.abs(s)) / 2;
        }

        // Calculate the distance between this point and another hexagon (Point)
        public int distance(Point b) {
            return subtract(b).length();
        }

        // Subtract another point's coordinates from this point
        private Point subtract(Point b) {
            return new Point(
                    this.q - b.q,  // Difference in q
                    this.r - b.r,  // Difference in r
                    this.s - b.s,  // Difference in s
                    0, 0           // No need for x, y in subtraction
            );
        }
    }
}
