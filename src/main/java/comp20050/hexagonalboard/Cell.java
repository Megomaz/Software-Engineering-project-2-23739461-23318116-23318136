package comp20050.hexagonalboard;

import javafx.scene.paint.Color;

public class Cell {
    private double x, y;  // Coordinates of the cell in the 2D grid
    private boolean occupied;  // Tracks if the cell is occupied
    private Player occupyingPlayer;  // The player occupying this cell
    private Color stoneColor;  // Color of the stone (RED or BLUE) in this cell

    // Constructor that initializes the coordinates and occupancy status
    public Cell(double x, double y) {
        this.x = x;
        this.y = y;
        this.occupied = false;
        this.occupyingPlayer = null;  // Initially, the cell is not occupied
        this.stoneColor = null;  // No color initially
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

    // Method to clear the occupancy status
    public void clear() {
        this.occupied = false;
        this.occupyingPlayer = null;
        this.stoneColor = null;  // Clear the stone color when clearing the cell
    }

    // Method to occupy the cell with a player and set the stone color
    public void occupy(Player player) {
        this.occupied = true;
        this.occupyingPlayer = player;
        this.stoneColor = (player.getId() == 0) ? Color.RED : Color.BLUE;  // Set stone color based on player
    }

    public Player getOccupyingPlayer() {
        return occupyingPlayer;  // Returns the player occupying this cell
    }

    // Method to get the coordinates as a string in the format "x, y"
    public String getCoordinate() {
        return "(" + getX() + ", " + getY() + ")";
    }

}