package game;

import gui.GridElement;
import gui.ShipScreen;

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
    private boolean damaged;
    private boolean isShip;
    private shipPart shipPart;

    private int shipID=0;

    private int lastShipID=0;

    private GridElement.GridType type;

    private GridElement grid;

    // sprites
    private final String sprite_location = "src/main/resources/sprites/";
    private BufferedImage waves = ImageIO.read(new File(sprite_location + "waves.png"));
    private BufferedImage radar = ImageIO.read(new File(sprite_location + "radar.png"));
    private BufferedImage enemy_sunk = ImageIO.read(new File(sprite_location + "enemy_sunk.png"));
    private BufferedImage enemy_damaged = ImageIO.read(new File(sprite_location + "enemy_damaged.png"));
    private BufferedImage ship_front = ImageIO.read(new File(sprite_location + "ship_front.png"));
    private BufferedImage ship_middle = ImageIO.read(new File(sprite_location + "ship_middle.png"));
    private BufferedImage ship_back = ImageIO.read(new File(sprite_location + "ship_back.png"));
    private AffineTransform ship_transform = new AffineTransform();
    private AffineTransform radar_transform = new AffineTransform();
    /*
    private BufferedImage ship_front_damaged = ImageIO.read(new File(sprite_location + "ship_front_damaged.png"));
    private BufferedImage ship_middle_damaged = ImageIO.read(new File(sprite_location + "ship_middle_damaged.png"));
    private BufferedImage ship_back_damaged = ImageIO.read(new File(sprite_location + "ship_back_damaged.png"));
     */

    private BufferedImage current_sprite;

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(60, 60);
    }


// TODO - CURRENTLY THE RADAR SCREEN SPRITES CAN BE FLIPPED IF THE ROTATE VALUE IS SET TO EAST. FIX THIS BECAUSE ITS WEIRD

    public Tile(String coord, GridElement grid) throws IOException {
        this.gridIndex = GridElement.coord_to_index(coord);
        this.damaged = false;
        this.isShip = false;
        this.shipPart = shipPart.NONE;
        this.type = grid.getType();

        ship_transform.scale(3.737, 3.737);
        radar_transform = ship_transform;

        this.grid = grid;

        switch (this.type) {
            case MAP -> {
                current_sprite =  waves;
            }
            case RADAR -> {
                current_sprite = radar;
            }
        }
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (type == GridElement.GridType.MAP) {

                        // does what it says on the tin - value of 6 for testing purposes only
                        drawShipsToTiles(3);

                    }
                    else {
                        if (current_sprite == enemy_damaged) {
                            current_sprite = enemy_sunk;
                        }
                        else {
                            current_sprite = enemy_damaged;
                        }
                    }

                    updateUI();
                }
            });

    }


    protected void paintComponent(Graphics g ) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();

        if (ShipScreen.getDir() == 'e' && current_sprite != this.waves) {
            this.ship_transform.scale(-1, 1);
            ship_transform.translate(-current_sprite.getWidth(), 0);
        }
        if (this.type == GridElement.GridType.MAP) {
            g2d.drawRenderedImage(current_sprite, this.ship_transform);
        }
        else {
            g2d.drawRenderedImage(current_sprite, this.radar_transform);
        }
    }

    private void drawShipsToTiles(int length) {
        int[] ind = getGridIndex();

        if (this.isShip()) {
            return;
        }

        if (length < 2) {
            throw new IllegalArgumentException("Err - Ship lenght shorter than 2");
        }

        // replace with switch case for all directions possible

        switch (ShipScreen.getDir()) {
            case 'n' : {
                if (grid.getTile(ind[0], ind[1]- (length-1)).isShip()) {
                    return;
                }
                setShipPart(shipPart.BACK);
                for(int x = 0; x < length-2; x++) {
                    if (grid.getTile(ind[0], ind[1] - (1+x)).isShip()){
                        for (int y = 1; y < x+2; y++) {
                            grid.getTile(ind[0], ind[1] - (1+(x-y))).setShip(false);
                        }
                        return;
                    }
                    grid.getTile(ind[0], ind[1] - (1+x)).setShipPart(shipPart.MIDDLE);
                }

                grid.getTile(ind[0], ind[1] - length+1).setShipPart(shipPart.FRONT);
                break;
            }
            case 'e' : {

                if (grid.getTile(ind[0] + (length-1), ind[1]).isShip()) {
                    return;
                };

                setShipPart(shipPart.BACK);
                for(int x = 0; x < length-2; x++) {
                    if (grid.getTile(ind[0] + (1 + x), ind[1]).isShip()){
                        for (int y = 1; y < x+2; y++) {
                            grid.getTile(ind[0] + (1 + (x-y)), ind[1]).setShip(false);
                        }
                        return;
                    }
                    grid.getTile(ind[0] + (1 + x), ind[1]).setShipPart(shipPart.MIDDLE);
                }
                grid.getTile(ind[0] + length-1, ind[1]).setShipPart(shipPart.FRONT);

                break;
            }
            case 's' : {

                if (grid.getTile(ind[0], ind[1] + (length-1)).isShip()) {
                    return;
                }
                setShipPart(shipPart.BACK);
                for(int x = 0; x < length-2; x++) {
                    if (grid.getTile(ind[0], ind[1] + (1+x)).isShip()) {
                        for (int y = 1; y < x+2; y++) {
                            grid.getTile(ind[0], ind[1] + (1+(x-y))).setShip(false);
                        }
                        return;
                    }
                    grid.getTile(ind[0], ind[1] + (1+x)).setShipPart(shipPart.MIDDLE);

                }
                grid.getTile(ind[0], ind[1] + length-1).setShipPart(shipPart.FRONT);
                break;
            }
            case 'w' : {
                if (grid.getTile(ind[0]-(length-1), ind[1]).isShip()) {
                    return;
                }
                setShipPart(shipPart.BACK);
                for (int x = 0; x < length-2; x++){
                    if (grid.getTile(ind[0] - (1+x), ind[1]).isShip()) {
                        for (int y = 1; y < x+2; y++) {
                            grid.getTile(ind[0] - (1+(x-y)), ind[1]).setShip(false);
                        }
                        return;
                    }
                    grid.getTile(ind[0] - (1+x), ind[1]).setShipPart(shipPart.MIDDLE);
                }
                grid.getTile(ind[0] - length+1, ind[1]).setShipPart(shipPart.FRONT);
                break;
            }
        }
    }



    public int[] getGridIndex() {
        return this.gridIndex;
    }

    public boolean isShip() {
        return this.isShip;
    }

    public void setShip(boolean yn) {
        if (!yn) {
            this.current_sprite = this.waves;
        }
        this.isShip = yn;
        this.updateUI();
    }

    public boolean getDamaged() {
        return this.damaged;
    }

    public Tile.shipPart getShipPart() {
        return this.shipPart;
    }

    public void setShipID() {
        this.shipID = lastShipID++;
    }

    public int getShipID() {
        return this.shipID;
    }

    public void setDamaged(boolean damaged) {

        this.damaged = damaged;
        /*
        switch (shipPart) {
            case FRONT:
                this.current_sprite = ship_front_damaged;
                break;
            case MIDDLE:
                this.current_sprite = ship_middle_damaged;
                break;
            case BACK:
                this.current_sprite = ship_back_damaged;
                break;
            case NONE:
                setShip(false);
                break;
        }

         */
        this.updateUI();
    }


    public void setShipPart(Tile.shipPart shipPart) {
        this.isShip = true;
        switch (shipPart) {
            case FRONT:
                this.current_sprite = ship_front;
                break;
            case MIDDLE:
                this.current_sprite = ship_middle;
                break;
            case BACK:
                this.current_sprite = ship_back;
                break;
            case NONE:
                setShip(false);
                break;
        }
        this.shipPart = shipPart;
        this.updateUI();
    }

    public enum shipPart {
        FRONT,
        MIDDLE,
        BACK,
        NONE
    }

}

