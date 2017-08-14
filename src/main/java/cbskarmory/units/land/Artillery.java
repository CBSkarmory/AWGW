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

import cbskarmory.Player;
import cbskarmory.PassiveFlag.MoveType;
import cbskarmory.terrain.Terrain;
import cbskarmory.units.Unit;
import cbskarmory.weapons.WeaponType;

/**
 * An Artillery is a Tank, but is an indirect fire unit
 * Gets SHELL -- ranged ground-to-ground
 * Costs 600
 * Ranged attack, Manhattan radius 2-3 -- cannot counterattack
 * Strong vs armor at range, very weak when defending
 */
public class Artillery extends Tank {

    /**
     * Constructs an Artillery
     * sets primary weapon to SHELL
     *
     * @param owner owner of the Unit
     */
    public Artillery(Player owner) {
        super(owner);
        setWeapon(1, WeaponType.SHELL);
    }

    @Override
    public boolean couldTarget(Unit toCheck, Terrain hypothetical) {
        if (!getLocation().equals(hypothetical)) {
            return false;//cannot move and fire
        }
        if (null == toCheck || MoveType.AIR.equals(toCheck.getMovementType())) {
            return false; //can't target nothing, can target sea, land
        }
        int dist = hypothetical.getDistanceTo((Terrain) toCheck.getLocation());
        return (dist >= 2 && dist <= 3);
    }

    @Override
    public boolean canCounter(Unit u) {
        return false;
    }

    @Override
    public int getBuildCost() {
        return 600;
    }

    @Override
    public double getBaseArmorResistance() {
        return 1;
    }
}
