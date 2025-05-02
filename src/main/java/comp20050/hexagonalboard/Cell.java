package comp20050.hexagonalboard;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class Cell {
    private double x, y;  // Coordinates of the cell in the 2D grid
    private boolean occupied;  // Tracks if the cell is occupied
    private Player occupyingPlayer;  // The player occupying this cell
    private Color stoneColor;  // Color of the stone (RED or BLUE) in this cell
    private Circle stone;

    // Constructor that initializes the coordinates and occupancy status
    public Cell(double x, double y) {
        this.x = x;
        this.y = y;
        this.occupied = false;
        this.occupyingPlayer = null;  // Initially, the cell is not occupied
        this.stoneColor = null;  // No color initially
        this.stone = null;
    }

    // Getter for x-coordinate
    public double getX() {
        return x;
    }

    // Getter for y-coordinate
    public double getY() {
        return y;
    }

    // Getter for the occupancy status
    public boolean isOccupied() {
        return occupied;
    }

    // Getter for stone color (RED or BLUE)
    public Color getStoneColor() {
        return stoneColor;
    }

    public void setStoneColor(Color color) {
        this.stoneColor = color;
    }

    // Getter for the stone (Circle) in this cell
    public Circle getStone() {
        return stone;
    }

    // Setter for the stone (Circle)
    public void setStone(Circle stone) {
        this.stone = stone;
    }

    // Method to clear the occupancy status
    public void clear() {
        this.occupied = false;
        this.occupyingPlayer = null;
        this.stoneColor = null;  // Clear the stone color when clearing the cell
        this.stone = null;  // Remove the stone (Circle) when clearing the cell
    }

    // Method to occupy the cell with a player and set the stone color
    public void occupy(Player player, Polygon hexagon) {
        this.occupied = true;
        this.occupyingPlayer = player;
        this.stoneColor = (player.getId() == 0) ? Color.RED : Color.BLUE;  // Sets the stone color based on player

        if (this.stone == null) {
            // Creates the stone only if it doesn't exist
            this.stone = new Circle(24.5 / 2);  // As HEX_RADIUS is 24.5
            this.stone.setLayoutX(hexagon.getLayoutX());  // Positions the stone at the cell's x-coordinate
            this.stone.setLayoutY(hexagon.getLayoutY());  // Positions the stone at the cell's y-coordinate
        }

        // Visually updates the color of the existing stone
        this.stone.setFill(this.stoneColor);
    }

    public Player getOccupyingPlayer() {
        return occupyingPlayer;  // Returns the player occupying this cell
    }

    // Method to get the coordinates as a string in the format "x, y"
    public String getCoordinate() {
        return "(" + getX() + ", " + getY() + ")";
    }

}