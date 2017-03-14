package cbskarmory.terrain;

import cbskarmory.TerrainGrid;
import info.gridworld.actor.Actor;

/**
 * Units of MoveType FOOT costs 2 mobility to traverse. 
 * SEA, TREADS, TIRES costs 999.
 *Provides NO bonus defense to occupying land Units
 *Attacking over a river is a dangerous but potentially rewarding strategy.
 */
public class River extends Terrain {

	 /**
	  * Constructs a River with given row and column coordinates.
	  * @param r the row
	  * @param c the column
	  */
	public River(int r, int c, TerrainGrid<Actor> hostGrid) {
		super(r, c, hostGrid);
	}

	@Override
	public int getDefense() {
		return 0;
	}

	@Override
	protected double getMoveCostFoot() {
		return 2;
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
