package gui;

import game.Tile;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class GridElement extends JPanel {

    private static String[][] coord_lookup = {

            {"a","0"},
            {"b","1"},
            {"c","2"},
            {"d","3"},
            {"e","4"},
            {"f","5"},
            {"g","6"},
            {"h","7"},
            {"i","8"},
            {"j","9"},
    };

    private Tile[][] grid = new Tile[10][10];



    private int[][] test_grid = {
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
            {10, 11, 12, 13, 14, 15, 16, 17, 18, 19},
            {20, 21, 22, 23, 24, 25, 26, 27, 28, 29},
            {30, 31, 32, 33, 34, 35, 36, 37, 38, 39},
            {40, 41, 42, 43, 44, 45, 46, 47, 48, 49},
            {50, 51, 52, 53, 54, 55, 56, 57, 58, 59},
            {60, 61, 62, 63, 64, 65, 66, 67, 68, 69},
            {70, 71, 72, 73, 74, 75, 76, 77, 78, 79},
            {80, 81, 82, 83, 84, 85, 86, 87, 88, 89},
            {90, 91, 92, 93, 94, 95, 96, 97, 98, 99}
    };

    private GridElement.GridType type;

    GridElement(GridType type) throws IOException {
        super(new   FlowLayout ( FlowLayout.CENTER, 0, 0 ));
        this.type = type;

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

    public GridType getType() {
        return this.type;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }


    public static int letter_to_int(String letter) {
        String l = letter.toLowerCase();
        for (String[] strings : coord_lookup) {
            if (Objects.equals(strings[0], l)) {
                return  Integer.parseInt(strings[1]);
            }
        }
        return -1;
    }

    public Tile getTile(int x, int y) {
        return grid[x][y];
    }

    public static int[] coord_to_index(String coord) {
        String[] coords = coord.split(",");
        return new int[] {letter_to_int(coords[0]), Integer.parseInt(coords[1])-1};
    }

    public enum GridType{
        MAP,
        RADAR

    }
}

