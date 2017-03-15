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
