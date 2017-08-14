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

public class TestTerrain extends Terrain {

    public TestTerrain(int r, int c, TerrainGrid<Actor> hostGrid) {
        super(r, c, hostGrid);
    }

    @Override
    protected double getMoveCostFoot() {
        return 1;
    }

    @Override
    protected double getMoveCostTires() {
        return 1;
    }

    @Override
    protected double getMoveCostTreads() {
        return 1;
    }

    @Override
    protected double getMoveCostBoat() {
        return 1;
    }

    @Override
    public int getDefense() {
        // TODO Auto-generated method stub
        return 0;
    }

}
