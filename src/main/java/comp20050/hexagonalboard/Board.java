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

    // Returns a list of valid adjacent cells for a given position in a hexagonal grid
    public List<Cell> getAdjacentCells(int row, int col) {
        return findValidAdjacentCells(row, col);
    }

    // Returns a string representation of the valid adjacent cells and their coordinates
    public String getAdjacentCellsAsString(int row, int col) {
        List<Cell> adjacentCells = findValidAdjacentCells(row, col);
        StringBuilder sb = new StringBuilder("Adjacent cells of the stone placed: ");

        // If no valid adjacent cells were found
        if (adjacentCells.isEmpty()) {
            sb.append("No valid adjacent cells.");
        } else {
            // Else we append the coordinates of each adjacent cell
            for (Cell cell : adjacentCells) {
                sb.append(cell.getCoordinate()).append(" ");
            }
        }

        return sb.toString().trim();
    }

    // Method to return a list of valid adjacent cells based on hexagonal position
    private List<Cell> findValidAdjacentCells(int row, int col) {
        // Direction offsets for adjacent cells in a hexagonal grid
        int[][] directions = {
                {-1, 0}, {1, 0},
                {0, -1}, {0, 1},
                {-1, 1}, {1, -1}
        };

        List<Cell> result = new ArrayList<>();

        // Loop through all possible adjacent positions
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            // Check if the position is within valid bounds of the board and grid constraints
            if (isInValidRange(newRow, newCol) &&
                    newRow >= 0 && newRow < cells.length &&
                    newCol >= 0 && newCol < cells[0].length) {
                result.add(cells[newRow][newCol]);
            }
        }

        return result;
    }

    // Checks whether the specified row and col falls within the defined valid range of our board
    private boolean isInValidRange(int row, int col) {
        return COLUMN_RANGES.containsKey(col)
                && row >= COLUMN_RANGES.get(col)[0]
                && row <= COLUMN_RANGES.get(col)[1];
    }
}