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
 * Bridge is a terrain.
 * Pretty much the same as Road, but this crosses rivers.
 */
public class Bridge extends Road {

    /**
     * Constructs a Bridge with given row and column coordinates.
     *
     * @param r the row
     * @param c the column
     */
    public Bridge(int r, int c, TerrainGrid<Actor> hostGrid) {
        super(r, c, hostGrid);
    }

}
