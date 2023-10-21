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

    // Retrieve the Tile object at the specified grid coordinates (x, y)
    // Parameters:
    // - x: The column index of the desired tile (0 to 9)
    // - y: The row index of the desired tile (0 to 9)
    // Returns:
    // - The Tile object located at the specified coordinates
    // x and y flipped because it was all fucky, basically just imagine its not there everything works the way you expect
    public Tile getTile(int x, int y) {
        return grid[y][x];
    }

    // Enum representing the type of the grid (MAP or RADAR)
    public enum GridType {
        MAP,
        RADAR
    }
}
