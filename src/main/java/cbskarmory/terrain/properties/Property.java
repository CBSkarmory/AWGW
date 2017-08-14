/*
 * Copyright(c) 2017 CBSkarmory (https://github.com/CBSkarmory)
 *
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * @author CBSkarmory
 */

package cbskarmory.terrain.properties;

import info.gridworld.actor.Actor;
import info.gridworld.gui.GridPanel;
import info.gridworld.gui.MenuMaker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import cbskarmory.Player;
import cbskarmory.TerrainGrid;
import cbskarmory.PassiveFlag.COFlag;
import cbskarmory.terrain.Terrain;
import cbskarmory.units.Air;
import cbskarmory.units.Sea;
import cbskarmory.units.Unit;
import cbskarmory.units.land.Infantry;

/**
 * Property represents all capturable Terrains Instantiated, it is a plain city.
 * Provides 30% defense boost to occupying land units. Units of MoveType FOOT,
 * TIRES, TREADS costs 1 mobility to traverse. SEA costs 999.
 */
public class Property extends Terrain {

    // cap timer
    public static final int FULL_CAP_TIMER = 200;
    private int capTimer;
    // capture mechanics
    private Player owner;

    /**
     * Constructs a Property with given row and column coordinates.
     *
     * @param r     the row
     * @param c     the column
     * @param owner owner of property, null if neutral
     */
    public Property(int r, int c, TerrainGrid<Actor> hostGrid, Player owner) {
        super(r, c, hostGrid);
        this.setOwner(owner);
        this.capTimer = 200;
    }

    /**
     * @return the current capture timer
     */
    public int getCapTimer() {
        return this.capTimer;
    }

    /**
     * resets the capture timer to the FULL_CAP_TIMER
     */
    public void resetCapTimer() {
        this.capTimer = Property.FULL_CAP_TIMER;
    }

    /**
     * @return the current owner of the property If the property is neutral, it
     * returns null.
     */
    public Player getOwner() {
        return this.owner;
    }

    /**
     * Sets the owner of the property. Should only be invoked internally by
     * tickCapTimer() Precondition: Player p is not null
     */
    public void setOwner(Player p) {
        this.owner = p;
        if (null != owner) {
            p.getPropertiesOwned().add(this);
        }
    }

    /**
     * Decrements the capture timer by the health of the occupying infantry unit
     * If capTimer falls below or equal to 0, the property is captured.
     */
    public void tickCapTimer(Unit u) {
        // currently, only infantry can cap. CO powers will probably allow other
        // units to cap later.
        if (!(u instanceof Infantry)) {
            JOptionPane.showMessageDialog(null,
                    "not sure how you invoked this method without " + "an infantry unit...see line 39 of Property");
            System.exit(-1);
        }
        // actual code
        // TODO checks for extra capturing points, to be implemented in CO
        int tickBy = (int) u.getOwner().CO.passive(u.getHealth(), COFlag.CAPTURE, u.getUnitType());
        this.capTimer -= tickBy;
        System.out.println("capTimer = " + capTimer);
        if (capTimer <= 0) {
            if (null != getOwner()) {
                ArrayList<Property> oldOwnerProperties = getOwner().getPropertiesOwned();
                oldOwnerProperties.remove(this);
            }
            this.setOwner(u.getOwner());
            resetCapTimer();
            MenuMaker.noBugsPls(hostGrid.hostWorld.getWorldFrame().control.display, hostGrid);
            JOptionPane.showMessageDialog(hostGrid.hostWorld.getWorldFrame(),
                    this.getClass().getSimpleName() + " at " + this + " captured\n\tby: Player " + getOwner().id, "Poperty Captured!", 0,
                    new ImageIcon(Property.class.getClassLoader().getResource("32x/capture.png")));
        }
    }

    public void tryResupply() {
        Unit occ = (Unit) hostGrid.get(this);
        if (occ != null &&
                (occ.getAmmo() != occ.getWeapons()[0].getMaxAmmo() || occ.getFuel() != 99 || occ.getHealth() != 100)) {
            if (this.getOwner() == occ.getOwner()) {
                occ.setAmmo(occ.getWeapons()[0].getMaxAmmo());
                occ.setFuel(99);
                if (tryRepair(occ)) {
                    getOwner().setMoney((int) Math.max(0.0, getOwner().getMoney() - (0.2 * occ.getBuildCost())));
                }

                Set<Terrain> where = new HashSet<>();
                where.add(this);
                GridPanel display = hostGrid.hostWorld.getWorldFrame().control.display;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ImageIcon supplyIco = new ImageIcon(this.getClass().getClassLoader().getResource(
                                "32x/supply.png"));
                        for (int x = 0; x < 4; x++) {
                            display.showIconsOnSetOfLocations(supplyIco.getImage(), where);
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            display.paintImmediately(display.getBounds());
                        }

                    }
                }).start();
            }
        }
    }

    private boolean tryRepair(Unit occ) {
        if (getOwner() != occ.getOwner()) {
            return false;
        }
        if (occ.getHealth() == 100) {
            return false;
        } else if ((occ instanceof Air && this instanceof AirPort) ||
                (occ instanceof Sea && this instanceof SeaPort) ||
                (!(occ instanceof Sea || occ instanceof Air))) {
            occ.setHealth((int) Math.min(100.0, occ.getHealth() + 20));
            return true;
        }
        return false;

    }

    @Override
    protected double getMoveCostFoot() {
        return 1;
    }

    @Override
    protected double getMoveCostTreads() {
        return 1;
    }

    @Override
    protected double getMoveCostTires() {
        return 1;
    }

    @Override
    public int getDefense() {
        return 3;
    }
}
