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
 * The battle helicopter is an air unit
 * Gets ROCKET_POD, MG
 * Costs 900
 * Costs 2 fuel/turn to stay airborne
 * Nicely harasses enemy land units
 */
public class BCopter extends Air {

    /**
     * Constructs a battle helicopter
     * sets primary weapon to ROCKET_POD,
     * secondary to MG
     *
     * @param owner owner of the unit
     */
    public BCopter(Player owner) {
        super(owner);
        setWeapon(0, WeaponType.ROCKET_POD);
        setWeapon(1, WeaponType.MG);
    }

    @Override
    public int getBuildCost() {
        return 900;
    }

    @Override
    public double getBaseArmorResistance() {
        return 1;
    }

    @Override
    public int getDailyCost() {
        return 2;
    }

    @Override
    public boolean couldTarget(Unit toCheck, Terrain hypothetical) { //cannot target jet fighters
        return super.couldTarget(toCheck, hypothetical) && !toCheck.isJet();
    }
}
