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

package cbskarmory.units.air;

import cbskarmory.Player;
import cbskarmory.PassiveFlag.MoveType;
import cbskarmory.terrain.Terrain;
import cbskarmory.units.Air;
import cbskarmory.units.Unit;
import cbskarmory.weapons.WeaponType;

/**
 * The Close Air Support is an air unit
 * Gets SHELL (ranged A2G)
 * Costs 2800
 * Costs 5 fuel/turn to stay airborne
 * Very potent against enemy ground units
 */
public class CAS extends Air {

    /**
     * Constructs a Close Air Support
     * sets primary weapon to SHELL (ranged A2G)
     *
     * @param owner owner of the unit
     */
    public CAS(Player owner) {
        super(owner);
        setWeapon(0, WeaponType.AP);
    }

    @Override
    public int getBuildCost() {
        return 2800;
    }

    @Override
    public double getBaseArmorResistance() {
        //20% general resistance
        return 0.80;
    }

    @Override
    public double resist(double damage, String source) {
        //70% resistance to small arms, 30% to heavy machine gun fire
        switch (source) {
            case WeaponType.RIFLE:
                return damage * 0.3;
            case WeaponType.MG:
                return damage * 0.3;
            case WeaponType.HMG:
                return damage * 0.7;
            default:
                return damage;
        }
    }

    @Override
    public boolean couldTarget(Unit toCheck, Terrain hypothetical) {
        if (null == toCheck || MoveType.AIR.equals(toCheck.getMovementType())) {
            return false; //can't target nothing, can target sea, land
        }
        int dist = hypothetical.getDistanceTo((Terrain) toCheck.getLocation());
        return (dist >= 1 && dist <= 4);
    }

    @Override
    public boolean canCounter(Unit u) {
        return super.canTarget(u);//1 tile Manhattan radius
    }

    @Override
    public int getDailyCost() {
        return 5;
    }

}
