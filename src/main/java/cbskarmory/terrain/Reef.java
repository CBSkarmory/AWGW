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

public class Reef extends Ocean {

    public Reef(int r, int c, TerrainGrid<Actor> hostGrid) {
        super(r, c, hostGrid);
    }

    @Override
    public int getDefense() {
        return 1;
    }

    @Override
    public double getMoveCostBoat() {
        return 2;
    }

    @Override
    protected double getMoveCostLander() {
        return 2;
    }

}
