package comp20050.hexagonalboard;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Cell} class.
 * <p>
 * These tests verify the behavior of cell occupancy, player assignment, clearing,
 * and coordinate representation.
 */
public class CellTest {

    /**
     * Tests occupying a cell with a player and clearing it afterward.
     * Ensures the cell updates its occupied state, stores the correct player,
     * displays the correct color, and resets properly after clearing.
     */
    @Test
    void testOccupyAndClear() {
        Cell cell = new Cell(2, 3);
        Player red = Player.PLAYERS[0];

        assertFalse(cell.isOccupied());
        assertNull(cell.getOccupyingPlayer());

        cell.occupy(red, new Polygon());

        assertTrue(cell.isOccupied());
        assertEquals(red, cell.getOccupyingPlayer());
        assertEquals(Color.RED, cell.getStoneColor());
        assertNotNull(cell.getStone());

        cell.clear();

        assertFalse(cell.isOccupied());
        assertNull(cell.getOccupyingPlayer());
        assertNull(cell.getStoneColor());
        assertNull(cell.getStone());
    }

    /**
     * Tests that the coordinate string returned by {@code getCoordinate()}
     * matches the expected format.
     */
    @Test
    void testGetCoordinate() {
        Cell cell = new Cell(4, 5);
        assertEquals("(4.0, 5.0)", cell.getCoordinate());
    }
}