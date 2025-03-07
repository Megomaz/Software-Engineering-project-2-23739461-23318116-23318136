package comp20050.hexagonalboard;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import java.util.*;


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

    /*
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
    */

    public List<Cell> getAdjacentCells(int row, int col) {
        List<Cell> adjacentCells = new ArrayList<>();

        // Define possible adjacent cell positions in a hexagonal grid
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, -1}
        };

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            // Check if new position is within bounds
            if (isInValidRange(newRow, newCol)) {
                adjacentCells.add(getCell(newRow, newCol));
            }
        }

        return adjacentCells;
    }

    public String getAdjacentCellsAsString(int row, int col) {
        // Hexagonal grid direction offsets
        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, -1}, {-1, 1}
        };

        List<Cell> adjacentCells = new ArrayList<>();
        StringBuilder sb = new StringBuilder("Adjacent cells: ");

        // Loop through the directions and calculate adjacent cells
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            // Only proceed if the coordinates are within the valid range
            if (!isInValidRange(newRow, newCol)) {
                continue; // Ignore coordinates that are not part of the valid range
            }

            // Check if within the bounds of the board
            if (newRow >= 0 && newRow < cells.length && newCol >= 0 && newCol < cells[0].length) {
                // If it's a valid adjacent cell, add it to the list and append the coordinates
                adjacentCells.add(cells[newRow][newCol]);
                sb.append(cells[newRow][newCol].getCoordinate()).append(" ");
            }
        }

        // If no adjacent cells were found, output a message
        if (adjacentCells.isEmpty()) {
            sb.append("No valid adjacent cells.");
        }

        return sb.toString();
    }

    // Method to check if the coordinate falls in range
    private boolean isInValidRange(int row, int col) {
        return (row >= 0 && row <= 6 && col == 12) ||   // (0-6,12)
                (row >= 0 && row <= 7 && col == 11) ||   // (0-7,11)
                (row >= 0 && row <= 8 && col == 10) ||   // (0-8,10)
                (row >= 0 && row <= 9 && col == 9)  ||   // (0-9,9)
                (row >= 0 && row <= 10 && col == 8) ||   // (0-10,8)
                (row >= 0 && row <= 11 && col == 7) ||   // (0-11,7)
                (row >= 0 && row <= 12 && col == 6) ||   // (0-12,6)
                (row >= 1 && row <= 12 && col == 5) ||   // (1-12,5)
                (row >= 2 && row <= 12 && col == 4) ||   // (2-12,4)
                (row >= 3 && row <= 12 && col == 3) ||   // (3-12,3)
                (row >= 4 && row <= 12 && col == 2) ||   // (4-12,2)
                (row >= 5 && row <= 12 && col == 1) ||   // (5-12,1)
                (row >= 6 && row <= 12 && col == 0);     // (6-12,0)
    }
}