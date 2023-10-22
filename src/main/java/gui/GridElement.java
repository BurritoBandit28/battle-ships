package gui;

import game.Tile;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

// GridElement class represents a panel for displaying a grid of tiles
public class GridElement extends JPanel {

    // Mapping letters to integers for grid coordinates
    private static String[][] coord_lookup = {
            {"a", "0"},
            {"b", "1"},
            {"c", "2"},
            {"d", "3"},
            {"e", "4"},
            {"f", "5"},
            {"g", "6"},
            {"h", "7"},
            {"i", "8"},
            {"j", "9"},
    };

    // Grid for holding Tile objects
    private Tile[][] grid = new Tile[10][10];

    // Test grid with numeric values (not used in the provided code)
    private int[][] test_grid = {
            // ... (initialization code omitted for brevity)
    };

    // Enum representing the type of the grid (MAP or RADAR)
    private GridElement.GridType type;

    // Constructor for creating a GridElement with a specific type (MAP or RADAR)
    GridElement(GridType type) throws IOException {
        super(new FlowLayout(FlowLayout.CENTER, 0, 0));
        this.type = type;

        // Initialize grid tiles with coordinates and add them to the panel
        char rowChar = 'A';
        for (int i = 0; i < 10; i++) {
            int colNumber = 1;
            for (int j = 0; j < 10; j++) {
                String tileName = rowChar + "," + colNumber;
                grid[i][j] = new Tile(tileName, this);
                this.add(grid[i][j]);
                colNumber++;
            }
            rowChar++;
        }
    }

    // Get the type of the grid (MAP or RADAR)
    public GridType getType() {
        return this.type;
    }

    // Override the getPreferredSize method to specify the preferred size of the grid panel
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(640, 640);
    }

    // Convert a letter to its corresponding integer value (used for grid coordinates)
    public static int letter_to_int(String letter) {
        String l = letter.toLowerCase();
        for (String[] strings : coord_lookup) {
            if (Objects.equals(strings[0], l)) {
                return Integer.parseInt(strings[1]);
            }
        }
        return -1;
    }

    // Convert grid coordinates (e.g., "A,1") to grid indices (row and column)
    public static int[] coord_to_index(String coord) {
        String[] coords = coord.split(",");
        return new int[]{Integer.parseInt(coords[1]) - 1, letter_to_int(coords[0])};
    }

    // this method sickens me to the core. It covers its hideous face in a thin veil of deception - it appears as merely a basic "getter" function
    // you couldn't be more wrong
    // behind its curtain lies a visage so putrid and vile it makes even The Beast quiver where he stands
    // but like a bad habit that plagues your everyday doings, it is relied on like a drug.
    // it hides my deepest and worst failings, why have I made the coordinates rely on sketchy alphanumerical values?
    // on a user side it is entirely hidden, just like the demons that walk among us in our cities, towns and homes
    // but I know it's there
    public Tile getTile(int x, int y) {
        return grid[y][x];
        // I hate it I hate it I hate it I hate it I hate it
    }

    // Enum representing the type of the grid (MAP or RADAR)
    public enum GridType {
        MAP,
        RADAR
    }
}
