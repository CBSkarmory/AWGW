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

package cbskarmory.units.sea;

import cbskarmory.Player;
import cbskarmory.PassiveFlag.MoveType;
import cbskarmory.terrain.Terrain;
import cbskarmory.units.Sea;
import cbskarmory.units.Unit;
import cbskarmory.weapons.WeaponType;

/**
 * A BattleShip is a Sea unit
 * Gets SHELL (ranged surface-to-surface),
 * range Manhattan 2-6,
 * cannot counterattack
 * Costs 2800
 * Costs 1 fuel/turn to stay afloat
 */
public class BattleShip extends Sea {

    /**
     * Constructs a BattleShip
     * sets primary to SHELL
     *
     * @param owner owner of the unit
     */
    public BattleShip(Player owner) {
        super(owner);
        setWeapon(0, WeaponType.SHELL);
    }

    @Override
    public int getBuildCost() {
        return 2800;
    }

    @Override
    public double getBaseArmorResistance() {
        //10% overall resistance
        return 0.90;
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
        return (dist >= 2 && dist <= 6);
    }

    @Override
    public boolean canCounter(Unit u) {
        return false;
    }
}
