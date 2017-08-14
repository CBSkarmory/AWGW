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

package cbskarmory.units.land;

import java.util.ArrayList;

import cbskarmory.Player;
import cbskarmory.PassiveFlag.MoveType;
import cbskarmory.terrain.Terrain;
import cbskarmory.units.Carry;
import cbskarmory.units.Unit;
import cbskarmory.weapons.WeaponType;

/**
 * The armored personnel carrier is an unarmed support unit
 * Gets NONE (unarmed)
 * Costs 500
 * Can carry 1 Infantry, can drop off into any valid (for infantry) Manhattan
 * adjacent tile (not in same turn)
 * Can resupply units (Manhattan adjacent allied units only)
 */
public class APC extends Unit implements Carry {

    private ArrayList<Unit> carried;

    /**
     * Constructs an armored personnel carrier
     * sets primary weapon to NONE (unarmed)
     *
     * @param owner owner of the unit
     */
    public APC(Player owner) {
        super(owner);
        setWeapon(0, WeaponType.NONE); //unarmed
        carried = new ArrayList<>();
    }

    @Override
    public ArrayList<Unit> getUnits() {
        return carried;
    }

    @Override
    public void resupply() {
        ArrayList<Unit> targetUnits = new ArrayList<>();
        try {
            for (int i = 0; i < 360; i += 90) {
                Terrain targetLoc = (Terrain) this.getLocation().getAdjacentLocation(i);
                if (getGrid().isValid(targetLoc)) {
                    Unit maybe = (Unit) getGrid().get(targetLoc);
                    if (null != maybe) {
                        targetUnits.add(maybe);
                    }
                }
            }
        } catch (ClassCastException e) {
            System.out.println("class cast error resupplying from APC at "
                    + "(r,c) : (" + getLocation().getRow() + ", " + getLocation().getCol() + ")");
            e.printStackTrace();
        }
        for (Unit u : targetUnits) {
            if (this.getOwner() == u.getOwner()) {
                u.setAmmo(u.getWeapons()[0].getMaxAmmo());
                u.setFuel(99);
            }
        }

    }

    @Override
    public double resist(double damage, String source) {
        //40% small arms resistance
        switch (source) {
            case WeaponType.RIFLE:
                return damage * 0.6;
            case WeaponType.MG:
                return damage * 0.6;
            default:
                return damage;
        }
    }

    @Override
    public boolean canCarry(Unit u) {
        //APC can carry 1 infantry or mechanized infantry
        switch (u.getUnitType()) {
            case INFANTRY:
                return true;
            case MECH:
                return true;
            default:
                return false;

        }
    }

    @Override
    public int getBuildCost() {
        return 500;
    }

    @Override
    public double getBaseArmorResistance() {
        return 1;
    }

    @Override
    public void outOfFuel() {
        //do nothing

    }

    @Override
    public MoveType getMovementType() {
        return MoveType.TREADS;
    }

    @Override
    public boolean canResupply() {
        return true;
    }

}
