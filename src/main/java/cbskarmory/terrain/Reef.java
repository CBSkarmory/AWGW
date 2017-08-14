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
