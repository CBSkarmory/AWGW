package cbskarmory.terrain;

import cbskarmory.TerrainGrid;
import info.gridworld.actor.Actor;

/**
 *Road is a terrain.
 *Units of MoveType FOOT, TIRES, TREADS costs 0.5 mobility to traverse. SEA costs 999.
 *Provides NO bonus defense to occupying land Units
 *Open roads are good for traveling, but bad for fighting from.
 */
public class Road extends Terrain {

	 /**
	  * Constructs a Road with given row and column coordinates.
	  * @param r the row
	  * @param c the column
	  */
	public Road(int r, int c, TerrainGrid<Actor> hostGrid) {
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

}
