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
import java.util.ArrayList;

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

    private final String ship_front = "/ship_front.png";
    private BufferedImage ship_middle = ImageIO.read(new File(sprite_location + "ship_middle.png"));
    private BufferedImage ship_middle_top = ImageIO.read(new File(sprite_location + "ship_middle_top.png"));
    private final String ship_back = "/ship_back.png";


    private final String ship_combined = "ship_combined.png";

    private BufferedImage waves = ImageIO.read(new File(sprite_location + "waves.png"));
    private BufferedImage radar = ImageIO.read(new File(sprite_location + "radar.png"));

    private BufferedImage fire = ImageIO.read(new File(sprite_location + "fire.png"));
    private BufferedImage radar_selected = ImageIO.read(new File(sprite_location + "selected.png"));
    private BufferedImage enemy_sunk = ImageIO.read(new File(sprite_location + "enemy_sunk.png"));
    private BufferedImage enemy_damaged = ImageIO.read(new File(sprite_location + "enemy_damaged.png"));

    /*

    private BufferedImage ship_front = ImageIO.read(new File(sprite_location + "ship_front.png"));

    private BufferedImage ship_back = ImageIO.read(new File(sprite_location + "ship_back.png"));

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


        this.grid = grid;

        // dictates which base sprite to use
        switch (this.type) {
            case MAP -> {
                current_sprite =  waves;
            }
            case RADAR -> {
                current_sprite = radar;
            }
        }
            // a lot of this code is temporary - not the drawShipsToTiles method however
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (type == GridElement.GridType.MAP) {

                        if (isShip()) {
                            setDamaged(!getDamaged());
                        }
                        else {
                            // does what it says on the tin - value of 6 for testing purposes only
                            try {
                                drawShipsToTiles(3);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }

                    }
                    else {
                        if (current_sprite == radar_selected) {
                            current_sprite = radar;
                        }
                        else {
                            current_sprite = radar_selected;
                        }
                    }

                    updateUI();
                }
            });

    }


    protected void paintComponent(Graphics g ) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();

        AffineTransform at = new AffineTransform();
        at.scale(3.737,3.737);

        g2d.drawRenderedImage(current_sprite, at);

        if (isShip() && this.getDamaged()) {
            g2d.drawRenderedImage(fire, at);
        }
        g2d.dispose();

    }

    private void drawShipsToTiles(int length) throws IOException {
        int[] ind = getGridIndex();

        if (this.isShip()) {
            return;
        }

        if (length < 2) {
            throw new IllegalArgumentException("Err - Ship lenght shorter than 2");
        }



        switch (ShipScreen.getDir()) {
            case 'n' : {
                //check if final pos is a ship
                if (grid.getTile(ind[0], ind[1]- (length-1)).isShip()) {
                    return;
                }
                setShipPart(shipPart.BACK, ShipScreen.getDir());

                // add middle ship parts using for loop
                for(int x = 0; x < length-2; x++) {
                    if (grid.getTile(ind[0], ind[1] - (1+x)).isShip()){

                        // if landing on a ship during this loop, iterate backwards and reset all placed tiles
                        for (int y = 1; y < x+2; y++) {
                            grid.getTile(ind[0], ind[1] - (1+(x-y))).setShip(false);
                        }
                        return;
                    }
                    grid.getTile(ind[0], ind[1] - (1+x)).setShipPart(shipPart.MIDDLE,ShipScreen.getDir());
                }

                grid.getTile(ind[0], ind[1] - length+1).setShipPart(shipPart.FRONT,ShipScreen.getDir());
                break;
            }

            // the next cases are just repeated, however numbers are swapped out depending on the direction
            case 'e' : {

                if (grid.getTile(ind[0] + (length-1), ind[1]).isShip()) {
                    return;
                };

                setShipPart(shipPart.BACK,ShipScreen.getDir());
                for(int x = 0; x < length-2; x++) {
                    if (grid.getTile(ind[0] + (1 + x), ind[1]).isShip()){
                        for (int y = 1; y < x+2; y++) {
                            grid.getTile(ind[0] + (1 + (x-y)), ind[1]).setShip(false);
                        }
                        return;
                    }
                    grid.getTile(ind[0] + (1 + x), ind[1]).setShipPart(shipPart.MIDDLE,ShipScreen.getDir());
                }
                grid.getTile(ind[0] + length-1, ind[1]).setShipPart(shipPart.FRONT,ShipScreen.getDir());

                break;
            }
            case 's' : {

                if (grid.getTile(ind[0], ind[1] + (length-1)).isShip()) {
                    return;
                }
                setShipPart(shipPart.BACK,ShipScreen.getDir());
                for(int x = 0; x < length-2; x++) {
                    if (grid.getTile(ind[0], ind[1] + (1+x)).isShip()) {
                        for (int y = 1; y < x+2; y++) {
                            grid.getTile(ind[0], ind[1] + (1+(x-y))).setShip(false);
                        }
                        return;
                    }
                    grid.getTile(ind[0], ind[1] + (1+x)).setShipPart(shipPart.MIDDLE,ShipScreen.getDir());

                }
                grid.getTile(ind[0], ind[1] + length-1).setShipPart(shipPart.FRONT,ShipScreen.getDir());
                break;
            }
            case 'w' : {
                if (grid.getTile(ind[0]-(length-1), ind[1]).isShip()) {
                    return;
                }
                setShipPart(shipPart.BACK,ShipScreen.getDir());
                for (int x = 0; x < length-2; x++){
                    if (grid.getTile(ind[0] - (1+x), ind[1]).isShip()) {
                        for (int y = 1; y < x+2; y++) {
                            grid.getTile(ind[0] - (1+(x-y)), ind[1]).setShip(false);
                        }
                        return;
                    }
                    grid.getTile(ind[0] - (1+x), ind[1]).setShipPart(shipPart.MIDDLE,ShipScreen.getDir());
                }
                grid.getTile(ind[0] - length+1, ind[1]).setShipPart(shipPart.FRONT,ShipScreen.getDir());
                break;
            }
        }
    }


    // returns the index of the tile on the GridElement its on
    public int[] getGridIndex() {
        return this.gridIndex;
    }

    // returns true if the Tile is any part of a ship, front middle or back.
    public boolean isShip() {
        return this.isShip;
    }

    // set whether the tile is a ship, if false replace sprite to background
    public void setShip(boolean yn) {
        if (!yn) {
            this.current_sprite = this.waves;
            this.setShipID(0);
        }
        this.isShip = yn;
        this.updateUI();
    }

    // get if the ship is damaged
    public boolean getDamaged() {
        return this.damaged;
    }


    // get what part of the ship it is
    public Tile.shipPart getShipPart() {
        return this.shipPart;
    }

    // set the ship ID, this ID will be used to check if all parts of a ship of the same ID are destroyed.
    public void setShipID(int id) {
        this.shipID = id;
    }

    // get the ships numerical ID
    public int getShipID() {
        return this.shipID;
    }

    // set whether the ship is damaged or not
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


    // set which part of the ship it is - TODO take into consideration the direction for up and down
    public void setShipPart(Tile.shipPart shipPart, char dir) throws IOException {
        this.isShip = true;
        this.setShipID(lastShipID+1);
        switch (shipPart) {
            case FRONT:
                this.current_sprite = ImageIO.read(new File(sprite_location + "/"+dir + ship_front));
                break;
            case MIDDLE:
                if (dir == 'n' || dir == 's') {
                    this.current_sprite = ship_middle_top;
                }
                else {
                    this.current_sprite = ship_middle;
                }
                break;
            case BACK:
                this.current_sprite = ImageIO.read(new File(sprite_location + "/"+dir + ship_back));
                break;
            case NONE:
                setShip(false);
                break;
        }
        this.shipPart = shipPart;
        this.updateUI();
    }

    // an enum because I like enums used to set what part of a ship something is. Good for switch/case statements
    public enum shipPart {
        FRONT,
        MIDDLE,
        BACK,
        NONE
    }

}

