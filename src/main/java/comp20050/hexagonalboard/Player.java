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

    public Player(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}