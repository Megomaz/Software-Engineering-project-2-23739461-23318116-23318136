/*e comp20050.hexagonalboard;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Enable custom ordering
class HelloControllerTest extends ApplicationTest {

    private HelloController controller;

    @Override
    public void start(Stage stage) {
        // Initialize controller and mock UI components
        controller = new HelloController();
        controller.hexBoardPane = new Pane();
        controller.hexPrototype = createMockHexagon();
        controller.turnLabel = new Label();
        controller.initialize();

        stage.setScene(new javafx.scene.Scene(controller.hexBoardPane));
        stage.show();
    }

    private Polygon createMockHexagon() {
        return new Polygon(0.0, 0.0, 10.0, 0.0, 5.0, 10.0);
    }



    @Test
    @Order(1)
    void testInitialState() {
        assertNotNull(controller.hexBoardPane, "Hex board pane should be initialized.");
        assertEquals(0, HelloController.getCurrentTurn(), "Initial turn should be player 0 (Red).");

    }

    @Test
    void testGenerateHexBoard() {
        // Ensure hexagons are generated correctly
        assertFalse(controller.hexBoardPane.getChildren().isEmpty(), "Hex board should contain hexagons.");
        assertFalse(controller.hexBoardPane.getChildren().isEmpty(), "Hex board should have multiple hexagons.");

        Polygon hexagon = (Polygon) controller.hexBoardPane.getChildren().get(0);
        assertEquals(Color.web("#F1A300"), hexagon.getFill(), "Hexagon should have the default color.");
    }

    @Test
    void testUpdateTurnIndicator() {


        controller.updateTurnIndicator();
        assertEquals(Player.PLAYERS[HelloController.getCurrentTurn()].getName() + "'s Turn",
                controller.turnLabel.getText(), "Turn label should match the current player's turn.");



    }

    @Test
    @Order(2)
    void testSwitchTurns() {
        Polygon hexagon = (Polygon) controller.hexBoardPane.getChildren().get(0);

        // Simulate clicking a hexagon
        interact(() -> hexagon.getOnMouseClicked().handle(null));

        // Ensure turn is updated
        assertEquals(1, HelloController.getCurrentTurn(), "Current turn should switch to player 1 (Blue).");

        // Verify hexagon color changes correctly
        assertEquals(Color.RED, hexagon.getFill(), "First player's move should turn the hexagon red.");

        // Simulate second click
        Polygon hexagon2 = (Polygon) controller.hexBoardPane.getChildren().get(1);
        interact(() -> hexagon2.getOnMouseClicked().handle(null));
        assertEquals(0, HelloController.getCurrentTurn(), "Current turn should switch back to player 0 (Red).");
    }

    @Test
    public void testHexagonCoordinatesAreStored() {
        // Simulate the user clicking on a specific hexagon, e.g., hexagon at (1, 1)
        int row = 1;
        int col = 1;
        Polygon hexagon = (Polygon) controller.hexBoardPane.getChildren().get(0);

        // Use interact() to simulate clicking the hexagon
        interact(() -> hexagon.getOnMouseClicked().handle(null));

        // Verify that the coordinates are stored correctly in the cell
        Cell cell = controller.board.getCell(row, col);

        // Assert that the stored coordinates match the expected ones
        assertEquals(cell.getCoordinate(), "(x, y)");  // Replace x, y with actual expected coordinates

        // Check that the cell is marked as occupied after the click
        assertTrue(cell.isOccupied());
    }
    

}
*/