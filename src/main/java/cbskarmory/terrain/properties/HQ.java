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

/**
 *
 */
package cbskarmory.terrain.properties;

import cbskarmory.Player;
import cbskarmory.TerrainGrid;
import info.gridworld.actor.Actor;

/**
 * @author George
 */
public class HQ extends Property {

    /**
     * @param r        row
     * @param c        column
     * @param hostGrid hostGrid
     * @param owner    owner of this HQ
     */
    public HQ(int r, int c, TerrainGrid<Actor> hostGrid, Player owner) {
        super(r, c, hostGrid, owner);
    }

    @Override
    public int getDefense() {
        return 4;
    }

}
