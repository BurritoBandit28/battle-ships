package gui;

import game.Tile;
import org.w3c.dom.Text;
import org.w3c.dom.ls.LSOutput;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class ShipScreen extends JFrame implements KeyListener {

    private String playerName;

    public boolean gameWon = false;
    public boolean gameLost = false;

    public static int[] boat_sizes= new int[] {4,4,4,3,2};

    private static GridElement shipGrid;

    private static GridElement ememyShipGrid;

    public static JLabel text;

    private static ArrayList<Integer> destroyedEnemyShips = new ArrayList<>();
    private static ArrayList<Integer> destroyedShips = new ArrayList<>();

    private static ArrayList<ArrayList<Integer>> attackedPlayerIndexes = new ArrayList<>();

    private static JPanel button = new JPanel() {
        private BufferedImage button;
        private BufferedImage button_fire;

        private BufferedImage current_sprite;

        {
            try {
                button = ImageIO.read(ClassLoader.getSystemResourceAsStream("sprites/button.png"));
                button_fire = ImageIO.read(ClassLoader.getSystemResourceAsStream("sprites/button_fire.png"));
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

            AffineTransform at = new AffineTransform();

            Graphics2D g2d = (Graphics2D) g.create();

            at.scale(3.75,3.75);

            if (current_sprite == null || getGameState() != GameState.ATTACK || radarGrid.getTile(radarGrid.getSELECTED_INDEX()[0],radarGrid.getSELECTED_INDEX()[1]).hasRadarHitTexture()) {
                current_sprite = button;
                g2d.drawRenderedImage(current_sprite, at);

                g2d.dispose();
                return;

            }
            if (gameState == GameState.ATTACK && radarGrid.getSELECTED_INDEX() != null) {
                current_sprite = button_fire;
            }



            g2d.drawRenderedImage(current_sprite, at);

            g2d.dispose();

        }

    };

    public void attackPlayer() throws IOException {
        Random r = new Random();
        int x;
        int y;
        ArrayList<Integer> index;
        while (true) {
            index = new ArrayList<>();
            x = r.nextInt(10);
            y = r.nextInt(10);
            index.add(x);
            index.add(y);
            if (!attackedPlayerIndexes.contains(index)) {
                break;
            }
        }

        Tile target = shipGrid.getTile(x, y);
        if (target.isShip()) {
            target.setShipDamaged(true);
            text.setText("You've been hit!");
            if (shipGrid.getIsShipDestroyed(target.getShipID())) {
                destroyedShips.add(target.getShipID());
                System.out.println(destroyedShips.size());
                if (destroyedShips.size() == 5) {
                    text.setText("You have lost all your ships! Game over!");
                    gameLost=true;
                    playSound("lose");

                }
            }
        }
        else {
            target.splishSplashTehe();
            text.setText("Your opponent missed.");
        }
        target.updateUI();
        setGameState(GameState.ATTACK);

    }

    public void doButtonPress() throws InterruptedException, IOException {
        if (gameState == GameState.ATTACK && radarGrid.getSELECTED_INDEX() != null && !gameWon && !gameLost) {
            int x = radarGrid.getSELECTED_INDEX()[0];
            int y = radarGrid.getSELECTED_INDEX()[1];
            radarGrid.getTile(x,y).setSelected(false);
            if (ememyShipGrid.getTile(x,y).isShip()) {
                ememyShipGrid.getTile(x,y).setDamaged(true);
                radarGrid.getTile(x,y).setHit();
                radarGrid.getTile(x,y).updateUI();
                if (ememyShipGrid.getIsShipDestroyed(ememyShipGrid.getTile(x,y).getShipID(), radarGrid)) {
                    text.setText("A hit and its been sunk!");
                    appendDestroyedShip(ememyShipGrid.getTile(x,y).getShipID());
                    radarGrid.getTile(x,y).updateUI();
                }
                text.setText("A hit!");
            }
            else {
                text.setText("Your shot missed.");
                radarGrid.getTile(x,y).setMiss();
                radarGrid.getTile(x,y).updateUI();
            }

            setGameState(GameState.DEFEND);
            getButton().updateUI();

            if (destroyedEnemyShips.size() == 5) {
                gameWon = true;
                text.setText("All enemy ships sunk! You win!");
                playSound("win");
                return;
            }
            attackPlayer();
        }
    }

    private static GridElement radarGrid;

    private static GameState gameState;

    private static int dir = 0;

    private static char[] lookup = {
        'w', 'n','e','s'
    };


    public static synchronized void playSound(String losewin) {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                        ClassLoader.getSystemResourceAsStream("sounds/"+ losewin+ ".wav"));
                clip.open(inputStream);
                clip.start();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }).start();
    }

    private static void setEnemyGridShips() {

        // I could make it randomize but you know what im tired of this now

        //I m not making the ships randomly generated because that would require too much and I've already done too much on this project
        Random r = new Random();
        int n = r.nextInt(3);
        n =0;
        switch (n) {
            case 0 -> {

                // Place ships of length 4 (ID: 1)
                ememyShipGrid.getTile(1, 2).setShip(true);
                ememyShipGrid.getTile(1, 2).setShipID(1);
                ememyShipGrid.getTile(1, 3).setShip(true);
                ememyShipGrid.getTile(1, 3).setShipID(1);
                ememyShipGrid.getTile(1, 4).setShip(true);
                ememyShipGrid.getTile(1, 4).setShipID(1);
                ememyShipGrid.getTile(1, 5).setShip(true);
                ememyShipGrid.getTile(1, 5).setShipID(1);

                // Place ships of length 4 (ID: 2)
                ememyShipGrid.getTile(2, 0).setShip(true);
                ememyShipGrid.getTile(2, 0).setShipID(2);
                ememyShipGrid.getTile(3, 0).setShip(true);
                ememyShipGrid.getTile(3, 0).setShipID(2);
                ememyShipGrid.getTile(4, 0).setShip(true);
                ememyShipGrid.getTile(4, 0).setShipID(2);
                ememyShipGrid.getTile(5, 0).setShip(true);
                ememyShipGrid.getTile(5, 0).setShipID(2);

                // Place ships of length 4 (ID: 3)
                ememyShipGrid.getTile(4, 8).setShip(true);
                ememyShipGrid.getTile(4, 8).setShipID(3);
                ememyShipGrid.getTile(5, 8).setShip(true);
                ememyShipGrid.getTile(5, 8).setShipID(3);
                ememyShipGrid.getTile(6, 8).setShip(true);
                ememyShipGrid.getTile(6, 8).setShipID(3);
                ememyShipGrid.getTile(7, 8).setShip(true);
                ememyShipGrid.getTile(7, 8).setShipID(3);

                // Place ships of length 3 (ID: 4)
                ememyShipGrid.getTile(9, 3).setShip(true);
                ememyShipGrid.getTile(9, 3).setShipID(4);
                ememyShipGrid.getTile(8, 3).setShip(true);
                ememyShipGrid.getTile(8, 3).setShipID(4);
                ememyShipGrid.getTile(7, 3).setShip(true);
                ememyShipGrid.getTile(7, 3).setShipID(4);

                // Place ships of length 2 (ID: 5)
                ememyShipGrid.getTile(5, 5).setShip(true);
                ememyShipGrid.getTile(5, 5).setShipID(5);
                ememyShipGrid.getTile(5, 4).setShip(true);
                ememyShipGrid.getTile(5, 4).setShipID(5);
            }
            case 1 -> {

            }
            case 2 -> {


            }
            case 3 -> {


            }
        }
    }



    public ShipScreen() throws IOException {

        gameState = GameState.PLACE;

        shipGrid = new GridElement(GridElement.GridType.MAP);
        radarGrid = new GridElement(GridElement.GridType.RADAR);
        ememyShipGrid = new GridElement(GridElement.GridType.MAP);
        text = new JLabel();
        setEnemyGridShips();
        BufferedImage image = ImageIO.read(ClassLoader.getSystemResourceAsStream("sprites/icon.png"));
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.setTitle("Battle Ships");
        this.setIconImage(image);

        text.setText("Place your ships. R to rotate");

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //super.mouseClicked(e);
                try {
                    doButtonPress();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        this.add(shipGrid);
        this.add(radarGrid);
        this.add(button);
        this.add(text);

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

    public static void appendDestroyedShip(int id) {
        destroyedEnemyShips.add(id);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'r') {
            dir++;
            if (dir > 3) {
                dir = 0;
            }
        }
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            try {
                doButtonPress();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
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
