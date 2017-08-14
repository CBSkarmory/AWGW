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

package cbskarmory.terrain;

import cbskarmory.TerrainGrid;
import info.gridworld.actor.Actor;

/**
 * Units of MoveType FOOT costs 2 mobility to traverse.
 * SEA, TREADS, TIRES costs 999.
 * Provides bonus 30% defense to occupying land Units
 */
public class Mountain extends Terrain {

    /**
     * Constructs a Mountain with given row and column coordinates.
     *
     * @param r the row
     * @param c the column
     */
    public Mountain(int r, int c, TerrainGrid<Actor> hostGrid) {
        super(r, c, hostGrid);
    }

    @Override
    public int getDefense() {
        return 3;
    }

    @Override
    protected double getMoveCostFoot() {
        return 2;
    }

    @Override
    protected double getMoveCostTires() {
        return 999;
    }

    @Override
    protected double getMoveCostTreads() {
        return 999;
    }

}
