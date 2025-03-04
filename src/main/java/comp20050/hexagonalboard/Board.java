package comp20050.hexagonalboard;

public class Board {
    private Cell[][] cells;

    public Board(int gridSize) {
        cells = new Cell[gridSize][gridSize];
        // Initialize cells here if necessary
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

    /* TODO: Implement the logic to validate placement of a stone.
       Check if the given coordinates (x, y) are valid and if the player can place their stone.
    */
    public boolean validatePlacement(int x, int y, Player player) {
        // This will be implemented later
        return false;  // Temporary return value
    }

    /* TODO: Implement the logic to highlight valid cells for placement by the current player.
       This will help show where the player can place their stone.
    */
    public void highlightValidCells(Player player) {
        // This will be implemented later
    }
}