package comp20050.hexagonalboard;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Player {
    private String name;
    private int id;

    // Static array of players
    public static final Player[] PLAYERS = {
            new Player("Red", 0),
            new Player("Blue", 1)
    };

    /* TODO: Implement available moves logic later.
    public List<Object> getAvailableMoves(Board board) {

        return new ArrayList<>();
    }
    */

    public Player(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public boolean isTurn(int currentTurn) {
        return this.id == currentTurn;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public static boolean attemptPlaceStone(Polygon hexagon) {
        return hexagon.getFill().equals(Color.web("#F1A300")); // Ignore if already clicked
    }
}
