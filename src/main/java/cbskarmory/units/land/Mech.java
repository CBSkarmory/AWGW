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
import cbskarmory.weapons.Suit;
import cbskarmory.weapons.WeaponType;

/**
 * a Mech is an Infantry
 * Gets ROCKET_LAUNCHER, RIFLE
 * Costs 300
 * Does OK vs armor, well vs trucks when attacking first.
 */
public class Mech extends Infantry {

    public Mech(Player owner) {
        super(owner);
        setWeapon(0, WeaponType.ROCKET_LAUNCHER);
    }

    public Mech(Player p, Suit s) {
        super(p, s);
        setWeapon(0, WeaponType.ROCKET_LAUNCHER);
    }

    @Override
    public int getBuildCost() {
        return 300;
    }

}
