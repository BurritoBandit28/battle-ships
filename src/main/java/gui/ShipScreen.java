package gui;

import org.w3c.dom.ls.LSOutput;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ShipScreen extends JFrame implements KeyListener {

    private String playerName;

    public static int[] boat_sizes= new int[] {4,4,4,3,2};

    private static GridElement shipGrid;

    private static JPanel button = new JPanel() {
        private final String sprite_location = "src/main/resources/sprites/";
        private BufferedImage button;
        private BufferedImage button_fire;

        private BufferedImage current_sprite;



        {
            try {
                button = ImageIO.read(new File(sprite_location + "button.png"));
                button_fire = ImageIO.read(new File(sprite_location + "button_fire.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(180, 120);
        }

        protected void paintComponent(Graphics g ) {
            super.paintComponent(g);

            if (current_sprite == null || getGameState() != GameState.ATTACK ) {
                current_sprite = button;
            }
            if (gameState == GameState.ATTACK && radarGrid.getSELECTED_INDEX() != null) {
                current_sprite = button_fire;
            }

            AffineTransform at = new AffineTransform();

            Graphics2D g2d = (Graphics2D) g.create();

            at.scale(3.75,3.75);

            g2d.drawRenderedImage(current_sprite, at);

            g2d.dispose();

        }

    };
    private static GridElement radarGrid;

    private static GameState gameState;

    private static int dir = 0;

    private static char[] lookup = {
        'w', 'n','e','s'
    };





    public ShipScreen() throws IOException {

        gameState = GameState.PLACE;

        shipGrid = new GridElement(GridElement.GridType.MAP);
        radarGrid = new GridElement(GridElement.GridType.RADAR);

        BufferedImage image = ImageIO.read(new File("src/main/resources/sprites/icon.png"));
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.setTitle("Battle Ships");
        this.setIconImage(image);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //super.mouseClicked(e);
                if (gameState == GameState.ATTACK && radarGrid.getSELECTED_INDEX() != null) {
                    setGameState(GameState.DEFEND);
                    getButton().updateUI();
                }
            }
        });
        this.add(shipGrid);
        this.add(radarGrid);
        this.add(button);

        this.setVisible(true);
    }

    public static GridElement getShipGrid(){
        return shipGrid;
    }

    public static GridElement getRadarGrid() {
        return radarGrid;
    }

    public static ShipScreen.GameState getGameState() {
        return gameState;
    }
    public static void setGameState(GameState gamestate) {
        gameState = gamestate;
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

    public static JPanel getButton() {
        return button;
    }

    public enum GameState {
        PLACE,
        ATTACK,
        DEFEND
    }

}
