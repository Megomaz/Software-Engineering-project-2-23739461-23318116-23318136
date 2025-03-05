package comp20050.hexagonalboard;

public class Cell {
    private double x, y;  // Coordinates of the cell in the 2D grid
    private boolean occupied;  // Tracks if the cell is occupied
    private Player occupyingPlayer;
    // Constructor that initializes the coordinates and occupancy status
    public Cell(double x, double y) {
        this.x = x;
        this.y = y;
        this.occupied = false;
        this.occupyingPlayer = null;// Initially, the cell is not occupied
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

    // Method to set the cell as occupied
    public void occupy(Player player) {
        this.occupied = true;
        this.occupyingPlayer = player;
    }

    // Method to clear the occupancy status
    public void clear() {
        this.occupied = false;
        this.occupyingPlayer = null;
    }



    public Player getOccupyingPlayer() {
        return occupyingPlayer;  // Returns the player occupying this cell
    }

    // Method to get the coordinates as a string in the format "x, y"
    public String getCoordinate() {
        return "(" + x + ", " + y + ")";
    }


    public void occupy() {
        this.occupied = true;  // Simply mark the cell as occupied
        this.occupyingPlayer = null;  // We don't associate a player here
    }
}