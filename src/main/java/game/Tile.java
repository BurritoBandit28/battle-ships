package game;

import gui.GridElement;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Tile extends JPanel {

    private int[] gridIndex;
    private String coords;
    private int damage;
    private boolean isShip;
    private shipPart shipPart;

    private GridElement.GridType type;

    private Tile tile;

    // sprites
    private String sprite_location = "src/main/resources/sprites/";
    private BufferedImage waves = ImageIO.read(new File(sprite_location + "waves.png"));

    private BufferedImage background;

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(60, 60);
    }



    public Tile(String coord, GridElement.GridType type) throws IOException {
        this.gridIndex = GridElement.coord_to_index(coord);
        this.damage=0;
        this.isShip = false;
        this.shipPart = shipPart.NONE;
        this.type = type;
        this.coords = coord;

        this.tile = this;

        switch (this.type) {
            case MAP -> {
                background =  ImageIO.read(new File(sprite_location + "waves.png"));
            }
            case RADAR -> {
                background = ImageIO.read(new File(sprite_location + "radar.png"));
            }
        }
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    System.out.println(coord);
                    try {
                        background = ImageIO.read(new File(sprite_location + "creepy_ass_smiley.png"));
                        tile.updateUI();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

    }

    protected void paintComponent(Graphics g ) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();

        AffineTransform transform = new AffineTransform();

        transform.scale(3.75, 3.75);
        //transform.translate(1.25, 1.25);
        g2d.drawRenderedImage(background, transform);
    }

    public int[] getGridIndex() {
        return this.gridIndex;
    }

    public boolean isShip() {
        return isShip;
    }

    public int getDamage() {
        return damage;
    }

    public String getCoords() {
        return coords;
    }

    public Tile.shipPart getShipPart() {
        return shipPart;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }


    public void setShipPart(Tile.shipPart shipPart) {
        this.isShip = true;
        this.shipPart = shipPart;
    }

    public enum shipPart {
        FRONT,
        MIDDLE,
        BACK,
        NONE
    }

}
