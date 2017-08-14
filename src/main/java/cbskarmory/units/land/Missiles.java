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
 * An Missiles truck is a Unit
 * Gets MISSILES (ground-to-air, ranged)
 * Costs 1200
 * MoveType TIRES
 * Ranged attack vs air only, Manhattan radius 3-5
 * cannot counterattack
 */
public class Missiles extends Unit {

    /**
     * Constructs a Missiles truck
     * sets primary weapon to MISSILES
     *
     * @param owner the owner of this unit
     */
    public Missiles(Player owner) {
        super(owner);
        setWeapon(0, WeaponType.MISSILES);
    }

    @Override
    public boolean couldTarget(Unit toCheck, Terrain hypothetical) {
        if (!getLocation().equals(hypothetical)) {
            return false;//cannot move and fire
        }
        if (null == toCheck || !MoveType.AIR.equals(toCheck.getMovementType())) {
            return false; //can't target nothing, can target Air units only
        }
        int dist = hypothetical.getDistanceTo((Terrain) toCheck.getLocation());
        return (dist >= 3 && dist <= 5);
    }

    @Override
    public boolean canCounter(Unit u) {
        return false;
    }

    @Override
    public int getBuildCost() {
        return 1200;
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
        return MoveType.TIRES;
    }
}

