package comp20050.hexagonalboard;
import java.util.*;


public class Board {
    protected Cell[][] cells;

    private static final Map<Integer, int[]> COLUMN_RANGES = Map.ofEntries(
            Map.entry(12, new int[]{0, 6}),
            Map.entry(11, new int[]{0, 7}),
            Map.entry(10, new int[]{0, 8}),
            Map.entry(9,  new int[]{0, 9}),
            Map.entry(8,  new int[]{0, 10}),
            Map.entry(7,  new int[]{0, 11}),
            Map.entry(6,  new int[]{0, 12}),
            Map.entry(5,  new int[]{1, 12}),
            Map.entry(4,  new int[]{2, 12}),
            Map.entry(3,  new int[]{3, 12}),
            Map.entry(2,  new int[]{4, 12}),
            Map.entry(1,  new int[]{5, 12}),
            Map.entry(0,  new int[]{6, 12})
    );

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

        return COLUMN_RANGES.containsKey(col) && row >= COLUMN_RANGES.get(col)[0]  && row <= COLUMN_RANGES.get(col)[1];
    }
}