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
 * The bomber is an air unit
 * Gets HEAT (direct A2G)
 * Costs 2200
 * Costs 5 fuel/turn to stay airborne
 * Good overall against ground units -- cannot hit air units
 */
public class Bomber extends Air {

    /**
     * Constructs a bomber
     * sets primary weapon to HEAT,
     *
     * @param owner owner of the unit
     */
    public Bomber(Player owner) {
        super(owner);
        setWeapon(0, WeaponType.HEAT);
    }

    @Override
    public int getBuildCost() {
        return 2200;
    }

    @Override
    public double getBaseArmorResistance() {
        //5% general resistance
        return 0.95;
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
    public boolean couldTarget(Unit toCheck, Terrain hypothetical) { //cannot target Air units
        return super.couldTarget(toCheck, hypothetical) && !MoveType.AIR.equals(toCheck.getMovementType());
    }
}
