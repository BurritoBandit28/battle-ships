package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ShipScreen extends JFrame implements KeyListener {

    private String playerName;

    private static GridElement shipGrid;
    private static GridElement radarGrid;

    private static int dir = 0;

    private static char[] lookup = {
        'w', 'n','e','s'
    };



    public ShipScreen() throws IOException {

        shipGrid = new GridElement(GridElement.GridType.MAP);
        radarGrid = new GridElement(GridElement.GridType.RADAR);

        BufferedImage image = ImageIO.read(new File("src/main/resources/sprites/icon.png"));
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.setTitle("Battle Ships");
        this.setIconImage(image);

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
        if (e.getKeyChar() == 'r') {
            dir++;
            if (dir > 3) {
                dir = 0;
            }
        }

    }

    public static char getDir() {
        return lookup[dir];
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
