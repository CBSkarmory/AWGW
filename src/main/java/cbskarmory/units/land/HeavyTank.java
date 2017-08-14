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
import cbskarmory.weapons.WeaponType;

/**
 * A HeavyTank is a Tank
 * Gets AP, HMG
 * Costs 2200
 * Armor Piercing rounds are strong against  armor and trucks
 */
public class HeavyTank extends Tank {

    /**
     * Constructs a Heavy Tank
     * sets primary weapon to AP,
     * secondary to HMG
     *
     * @param owner owner of the Unit
     */
    public HeavyTank(Player owner) {
        super(owner);
        setWeapon(0, WeaponType.AP);
        setWeapon(1, WeaponType.HMG);
    }

    @Override
    public int getBuildCost() {
        return 2200;
    }

    @Override
    public double getBaseArmorResistance() {
        //35% resistance
        return 0.65;
    }

}
