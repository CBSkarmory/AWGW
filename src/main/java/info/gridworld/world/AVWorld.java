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

package info.gridworld.world;

import cbskarmory.TerrainGrid;
import cbskarmory.terrain.Terrain;
import info.gridworld.grid.Grid;
import info.gridworld.gui.WorldFrame;

public class AVWorld extends MouseWorld {
    public AVWorld() {
    }

    public AVWorld(Grid g) {
        super(g);
        // TODO Auto-generated constructor stub

    }

    @Override
    public void setGrid(Grid g) {
        super.setGrid(g);
        if (g instanceof TerrainGrid) {
            ((TerrainGrid) g).hostWorld = this;
        }
    }

    public void go() {
//		System.out.println("starting go\n");
        getWorldFrame().control.display.avw = this;
        getWorldFrame().control.display.setCurrentLocation(getLocationWhenClicked());
        getWorldFrame().control.editLocation();
        resetClickedLocation();
        /*redundant cast to avoid "suspicious" .contains warning, where shouldBeHighlighted is Set<Terrain>
         * and clickedLocation is Location (actually Terrain, though)
         */
        if (!getWorldFrame().control.display.shouldBeHighlighted.contains((Terrain) clickedLocation)) {
            getWorldFrame().control.display.shouldBeHighlighted.clear();
        }

    }

    public WorldFrame getWorldFrame() {
        if (null != frame) {
            return (WorldFrame) frame;
        } else {
            return null;
        }
    }

}
