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
import cbskarmory.terrain.properties.Property;
import cbskarmory.units.Unit;
import cbskarmory.weapons.Suit;
import cbskarmory.weapons.WeaponType;

/**
 * An Infantry is a Unit
 * Gets NONE, RIFLE (infinite ammo rifle)
 * Costs 100
 * Can capture properties
 * MoveType FOOT
 * Staple expendable foot soldier
 */
public class Infantry extends Unit {

    /**
     * Constructs an Infantry owned by owner
     * also sets the correct weapons
     */
    public Infantry(Player p) {
        super(p);
        setWeapon(0, WeaponType.NONE); //no primary -- secondary has infinite ammo
        setWeapon(1, WeaponType.RIFLE);
    }

    public Infantry(Player p, Suit suit) {
        this(p);
        this.setSuit(suit);
    }

    @Override
    public int getBuildCost() {
        return 100;
    }

    @Override
    public double getBaseArmorResistance() {
        //0% resistance
        return 1;
    }

    @Override
    public void outOfFuel() {
        //do nothing
    }

    @Override
    public MoveType getMovementType() {
        return MoveType.FOOT;
    }

    /**
     * Precondition: Infantry is on an enemy
     * owned or neutral Property
     * Ticks the cap timer of the Property with tickCapTimer()
     */
    public void capture() {
        System.out.println("capturing city");
        Terrain t = (Terrain) this.getLocation();
        if (t instanceof Property) {
            Property p = (Property) t;
            System.out.println("capTimer = " + p.getCapTimer());
            p.tickCapTimer(this);
        } else {
            System.err.println("?? not on property, loc = " + getLocation().getClass().getSimpleName() + " " + getLocation());
        }
        this.immobilize();
    }

    @Override
    public boolean couldTarget(Unit toCheck, Terrain hypothetical) { //cannot target jet fighters, boats
        return super.couldTarget(toCheck, hypothetical) && !toCheck.isJet() && !MoveType.SEA.equals(toCheck.getMovementType());
    }

}
