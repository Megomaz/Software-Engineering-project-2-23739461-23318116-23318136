package comp20050.hexagonalboard;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;


public class Board {
    private Cell[][] cells;
    private static final int GRID_RADIUS = 6;

    public Board(int gridSize) {
        cells = new Cell[gridSize][gridSize];
        // Initialize cells with default Cell objects (not occupied)
        for (int q = 0; q < gridSize; q++) {
            for (int r = 0; r < gridSize; r++) {
                cells[q][r] = new Cell(0, 0);  // Default x, y positions; you can modify accordingly
            }
        }
    }

    // Getter for cell
    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    // Setter for cell
    public void setCell(int row, int col, Cell cell) {
        cells[row][col] = cell;
    }

    /* TODO: Implement the logic to render the board.
    public void renderBoard() {
        // This will be implemented later
    }
    */


    public boolean validatePlacement(int row, int col, Player currentPlayer) {
        // Check if the selected cell is already occupied
        if (cells[row][col].isOccupied()) {
            return false;  // Cannot place a stone on an already occupied cell
        }

        // Get the opponent's ID (opponent is the other player)
        Player opponent = Player.PLAYERS[(currentPlayer.getId() + 1) % 2];

        // Check adjacent cells for opponent's stones
        for (Point adjacent : getAdjacentCells(row, col)) {
            // Directly access the adjacent cell, check if it's occupied and if the opponent's stone is there
            if (getCell(adjacent.q + GRID_RADIUS, adjacent.r + GRID_RADIUS).isOccupied() &&
                    getCell(adjacent.q + GRID_RADIUS, adjacent.r + GRID_RADIUS).getOccupyingPlayer().getId() == opponent.getId()) {
                return true;  // Valid placement (adjacent to opponent's stone)
            }
        }

        // Return false if no adjacent opponent's stone found
        return false;
    }





    public Point[] getAdjacentCells(int row, int col) {
        // Adjacent cells in hexagonal coordinates
        Point[] adjacentCells = new Point[6];

        // Assuming the standard hexagonal directions for adjacent cells (q, r, s coordinates)
        // Adjust these based on your hexagonal grid setup
        adjacentCells[0] = new Point(row - 1, col, 0, 0, 0);  // Example, adjust for correct directions
        adjacentCells[1] = new Point(row + 1, col, 0, 0, 0);
        adjacentCells[2] = new Point(row, col - 1, 0, 0, 0);
        adjacentCells[3] = new Point(row, col + 1, 0, 0, 0);
        adjacentCells[4] = new Point(row - 1, col + 1, 0, 0, 0);
        adjacentCells[5] = new Point(row + 1, col - 1, 0, 0, 0);

        // Return the array of adjacent cells
        return adjacentCells;
    }
    // To add Boundary checks etc


}