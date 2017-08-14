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
 * The fighter aircraft is an Air unit
 * Gets MISSILE (direct A2A)
 * Costs 2000
 * Costs 5 fuel/turn to stay airborne
 * Very strong vs enemy air units in direct combat -- cannot hit ground units
 */
public class Fighter extends Air {

    /**
     * Constructs a fighter aircraft
     * sets primary weapon to MISSILE
     *
     * @param owner owner of the unit
     */
    public Fighter(Player owner) {
        super(owner);
        setWeapon(0, WeaponType.MISSILE);
    }

    @Override
    public int getBuildCost() {
        return 2000;
    }

    @Override
    public double getBaseArmorResistance() {
        return 1;
    }

    @Override
    public double resist(double damage, String source) {
        //50% resistance to small arms
        switch (source) {
            case WeaponType.RIFLE:
                return damage * 0.5;
            case WeaponType.MG:
                return damage * 0.5;
            default:
                return damage;
        }
    }

    @Override
    public int getDailyCost() {
        return 5;
    }

    @Override
    public boolean couldTarget(Unit toCheck, Terrain hypothetical) {//air only
        return super.couldTarget(toCheck, hypothetical) && MoveType.AIR.equals(toCheck.getMovementType());
    }

}
