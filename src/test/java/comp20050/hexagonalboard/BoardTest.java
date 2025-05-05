package comp20050.hexagonalboard;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Board} class.
 * <p>
 * These tests verify correct cell management and adjacency logic.
 */
public class BoardTest {

    /**
     * Tests that a cell can be correctly assigned to and retrieved from
     * a specific position on the board.
     */
    @Test
    void testGetAndSetCell() {
        Board board = new Board(13);
        Cell testCell = new Cell(6, 6);
        board.setCell(6, 6, testCell);

        assertSame(testCell, board.getCell(6, 6));
    }

    /**
     * Tests that {@code getAdjacentCells} returns exactly six cells when
     * querying a valid central cell with all adjacent neighbors present.
     */
    @Test
    void testValidAdjacencyReturnsSixCells() {
        Board board = new Board(13);
        int row = 6, col = 6;

        // Set center cell and its 6 neighboring cells
        board.setCell(row, col, new Cell(row, col));
        for (int[] offset : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, -1}}) {
            int r = row + offset[0];
            int c = col + offset[1];
            board.setCell(r, c, new Cell(r, c));
        }

        List<Cell> adjacent = board.getAdjacentCells(row, col);
        assertEquals(6, adjacent.size(), "Expected 6 adjacent cells in the center.");
    }

    /**
     * Tests that requesting adjacent cells from an invalid (out-of-bounds)
     * position returns an empty list.
     */
    @Test
    void testInvalidAdjacencyReturnsEmptyList() {
        Board board = new Board(13);
        List<Cell> adjacent = board.getAdjacentCells(-1, -1);
        assertTrue(adjacent.isEmpty(), "Expected no adjacent cells for invalid position.");
    }
}