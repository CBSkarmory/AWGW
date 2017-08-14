package cbskarmory.terrain;

import cbskarmory.TerrainGrid;
import info.gridworld.actor.Actor;

/**
 * A Beach is a Terrain.
 * All non- Sea units expend 1 mobility to traverse.
 * This is where the Landers land and drop off other Units.
 * Provides NO defensive cover.
 */
public class Beach extends Terrain {

    /**
     * Constructs a Beach with given row and column coordinates.
     *
     * @param r the row
     * @param c the column
     */
    public Beach(int r, int c, TerrainGrid<Actor> hostGrid) {
        super(r, c, hostGrid);
    }

    @Override
    public int getDefense() {
        return 0;
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
    protected double getMoveCostLander() {
        return 1;
    }

}
