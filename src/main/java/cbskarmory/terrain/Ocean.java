package cbskarmory.terrain;

import cbskarmory.TerrainGrid;
import info.gridworld.actor.Actor;

/**
 *Units of MoveType SEA expend 1 mobility to traverse.
 * FOOT, TREADS, TIRES take 999.
 *Provides NO defensive cover.
 */
public class Ocean extends Terrain{

	/**
	 * Constructs an Ocean with given row and column coordinates.
	 * @param r the row
	 * @param c the column
	 */
	public Ocean(int r, int c, TerrainGrid<Actor> hostGrid) {
		super(r, c, hostGrid);
	}

	@Override
	public double getMoveCostBoat(){
		return 1;
	}
	@Override
	protected double getMoveCostLander() {
		return 1;
	}
	@Override
	public int getDefense() {
		return 0;
	}

	@Override
	protected double getMoveCostFoot() {
		return 999;
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
