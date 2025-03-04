package comp20050.hexagonalboard;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;


public class UIHandler {

    /* TODO: Implement available moves logic later.
    public void renderGraphics(Board board) {
        // This will be implemented later
    }
     */

    /* TODO: Implement available moves logic later.
    public void displayMessage(String message) {
        // This will be implemented later
    }
    */

    public static void updateTurnIndicator(Player player, Label turnLabel) {
        turnLabel.setText(player.getName() + "'s Turn");
        turnLabel.setTextFill(player.getId() == 0 ? Color.RED : Color.BLUE);
    }

    /* TODO: Implement available moves logic later.
    public void renderStonePlacementAnimation(int x, int y) {
        // This will be implemented later
    }
    */
}