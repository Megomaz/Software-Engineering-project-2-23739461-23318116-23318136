package comp20050.hexagonalboard;



import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;


import javax.sound.midi.SysexMessage;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Enable custom ordering
class HelloControllerTest extends ApplicationTest {

    private static final int GRID_RADIUS = 6;
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
        assertEquals(0, controller.getCurrentTurn(), "Initial turn should be player 0 (Red).");

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


        UIHandler.updateTurnIndicator(Player.PLAYERS[controller.getCurrentTurn()], controller.turnLabel);
        assertEquals(Player.PLAYERS[controller.getCurrentTurn()].getName() + "'s Turn",
                controller.turnLabel.getText(), "Turn label should match the current player's turn.");



    }

    @Test
    @Order(2)
    void testSwitchTurns() {
        Polygon hexagon = (Polygon) controller.hexBoardPane.getChildren().get(0);

        // Simulate clicking a hexagon
        interact(() -> hexagon.getOnMouseClicked().handle(null));

        // Ensure turn is updated
        assertEquals(1, controller.getCurrentTurn(), "Current turn should switch to player 1 (Blue).");


        // Simulate second click
        Polygon hexagon2 = (Polygon) controller.hexBoardPane.getChildren().get(1);
        interact(() -> hexagon2.getOnMouseClicked().handle(null));
        assertEquals(0, controller.getCurrentTurn(), "Current turn should switch back to player 0 (Red).");
    }



    @Test
    void testPlaceStone(){
        Polygon hexagon = (Polygon) controller.hexBoardPane.getChildren().get(5);
        assertFalse(controller.getBoard().getCell(0, 11).isOccupied());
        interact(() -> hexagon.getOnMouseClicked().handle(null));
        assertTrue(controller.getBoard().getCell(0, 11).isOccupied());

    }

    @Test
    // Test to see if occupied hexagon stays same colour after being pressed again
    void testPlaceStoneOnOccupiedHexagon(){

        Polygon hexagon = (Polygon) controller.hexBoardPane.getChildren().get(5);

        interact(() -> hexagon.getOnMouseClicked().handle(null));
        Color occupiedStoneColour = controller.getBoard().getCell(0, 11).getStoneColor();

        interact(() -> hexagon.getOnMouseClicked().handle(null));
        assertEquals(occupiedStoneColour , controller.getBoard().getCell(0, 11).getStoneColor());

    }

    @Test
    // Test that a stone is not placed adjacent to the same colour stone when not capturing
    void testPlaceStoneAdjacentWhenNotCapturing(){
        Polygon hexagon = (Polygon) controller.hexBoardPane.getChildren().get(0);
        interact(() -> hexagon.getOnMouseClicked().handle(null));


        Polygon hexagon2 = (Polygon) controller.hexBoardPane.getChildren().get(25);
        interact(() -> hexagon2.getOnMouseClicked().handle(null));


        Polygon hexagon3 = (Polygon) controller.hexBoardPane.getChildren().get(1);
        interact(() -> hexagon3.getOnMouseClicked().handle(null));
        assertFalse(controller.getBoard().getCell(0, 7).isOccupied());
    }


    @Test
    void testAttemptCapture(){

        Polygon hexagon = (Polygon) controller.hexBoardPane.getChildren().get(0);
        interact(() -> hexagon.getOnMouseClicked().handle(null));

        Polygon hexagon2 = (Polygon) controller.hexBoardPane.getChildren().get(1);
        System.out.println("Current turn " + controller.getCurrentTurn());
        interact(() -> hexagon2.getOnMouseClicked().handle(null));

        Polygon hexagon3 = (Polygon) controller.hexBoardPane.getChildren().get(2);
        System.out.println("Current turn " + controller.getCurrentTurn());
        System.out.println(controller.getBoard().getCell(0, 7).isOccupied());
        assertTrue(controller.getBoard().getCell(0, 7).isOccupied());
        interact(() -> hexagon3.getOnMouseClicked().handle(null));

        System.out.println("Current turn " + controller.getCurrentTurn());

        assertFalse(controller.getBoard().getCell(0, 7).isOccupied());


    }

}
