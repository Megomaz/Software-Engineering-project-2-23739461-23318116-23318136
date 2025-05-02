package comp20050.hexagonalboard;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;

public class UIHandler {
private Pane hexBoardPane;

    public UIHandler(Pane hexBoardPane) {
        // Initialize the pane
        this.hexBoardPane = hexBoardPane;  // Use StackPane, GridPane, or similar if needed
    }

    public static void updateTurnIndicator(Player player, Label turnLabel) {
        turnLabel.setText(player.getName() + "'s Turn");
        turnLabel.setTextFill(player.getId() == 0 ? Color.RED : Color.BLUE);
    }

}