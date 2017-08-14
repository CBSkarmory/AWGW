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
 * Units of MoveType SEA expend 1 mobility to traverse.
 * FOOT, TREADS, TIRES take 999.
 * Provides NO defensive cover.
 */
public class Ocean extends Terrain {

    /**
     * Constructs an Ocean with given row and column coordinates.
     *
     * @param r the row
     * @param c the column
     */
    public Ocean(int r, int c, TerrainGrid<Actor> hostGrid) {
        super(r, c, hostGrid);
    }

    @Override
    public double getMoveCostBoat() {
        return 1;
    }

    @Override
    protected double getMoveCostLander() {
        return 1;
    }

    @Override
    public int getDefense() {
        return 0;
    }

    @Override
    protected double getMoveCostFoot() {
        return 999;
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
