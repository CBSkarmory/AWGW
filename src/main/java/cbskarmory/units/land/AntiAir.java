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
import cbskarmory.units.HiddenUnit;
import cbskarmory.units.Unit;
import cbskarmory.weapons.WeaponType;

public class AntiAir extends Tank {

    public AntiAir(Player owner) {
        super(owner);
        this.setWeapon(0, WeaponType.FLAK);
    }

    @Override
    public int getBuildCost() {
        return 800;
    }

    @Override
    public double getBaseArmorResistance() {
        //5% resistance
        return 0.95;
    }

    @Override
    public double resist(double damage, String source) {
        //25% resistance to rocket pods, small arms
        switch (source) {
            case WeaponType.ROCKET_POD:
                return damage * 0.75;
            case WeaponType.RIFLE:
                return damage * 0.75;
            case WeaponType.MG:
                return damage * 0.75;
            default:
                return damage;
        }
    }

    @Override
    public boolean couldTarget(Unit toCheck, Terrain hypothetical) { //cannot target Boats
        if (null == toCheck) {
            return false; //can't target nothing
        }
        return hypothetical.getDistanceTo((Terrain) toCheck.getLocation()) == 1 && !(toCheck instanceof HiddenUnit) && !MoveType.SEA.equals(toCheck.getMovementType());
    }
}
