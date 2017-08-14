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

package cbskarmory.terrain.properties;

import info.gridworld.actor.Actor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;

import cbskarmory.Player;
import cbskarmory.TerrainGrid;
import cbskarmory.units.Unit;
import cbskarmory.units.air.*;

/**
 * A Barracks is a factory
 * Barracks builds land Units.
 */
public class AirPort extends Factory {

    /**
     * Constructs a Barracks with given row and column coordinates.
     * all land Unit constructors are added to buildableUnits
     *
     * @param r the row
     * @param c the column
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public AirPort(int r, int c, TerrainGrid<Actor> hostGrid, Player owner) throws NoSuchMethodException, SecurityException {
        super(r, c, hostGrid, owner);
        this.buildableUnits = new ArrayList<Constructor<? extends Unit>>();
        buildableUnits.add(TCopter.class.getConstructor(Player.class));
        buildableUnits.add(BCopter.class.getConstructor(Player.class));
        buildableUnits.add(SCopter.class.getConstructor(Player.class));
        buildableUnits.add(Fighter.class.getConstructor(Player.class));
        buildableUnits.add(Bomber.class.getConstructor(Player.class));
        buildableUnits.add(CAS.class.getConstructor(Player.class));
        buildableUnits.add(JSF.class.getConstructor(Player.class));
        buildableUnits.add(DropShip.class.getConstructor(Player.class));
        buildableUnits.add(StealthBomber.class.getConstructor(Player.class));
        buildableUnits.add(AdvFighter.class.getConstructor(Player.class));

    }

}
