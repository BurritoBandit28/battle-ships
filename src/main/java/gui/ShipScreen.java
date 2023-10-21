package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class ShipScreen extends JFrame implements KeyListener {

    private String playerName;

    private static GridElement shipGrid;
    private static GridElement radarGrid;



    public ShipScreen() throws IOException {

        shipGrid = new GridElement(GridElement.GridType.MAP);
        radarGrid = new GridElement(GridElement.GridType.RADAR);

        this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.setTitle("Battle Ships");

        this.add(shipGrid);
        this.add(radarGrid);

        this.setVisible(true);
    }

    public static GridElement getShipGrid(){
        return shipGrid;
    }

    public static GridElement getRadarGrid() {
        return radarGrid;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'e') {
            System.out.println( shipGrid.getTile(GridElement.coord_to_index("I,7")[0], GridElement.coord_to_index("I,7")[1]).getCoords());

        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
