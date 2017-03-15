package cbskarmory.terrain;

import cbskarmory.TerrainGrid;
import info.gridworld.actor.Actor;

/**
 * A Forest is a Terrain. 
 * Units of MoveType FOOT, TIRES, TREADS costs 1.5 mobility to traverse. SEA costs 999.
 * Provides bonus 20% defense to occupying land Units
 */
public class Forest extends Terrain {

	 /**
	  * Constructs a Forest with given row and column coordinates.
	  * @param r the row
	  * @param c the column
	  */
	public Forest(int r, int c, TerrainGrid<Actor> hostGrid) {
		super(r, c, hostGrid);
	}

	@Override
	public int getDefense() {
		return 2;
	}

	@Override
	protected double getMoveCostFoot() {
		return 1;
	}

	@Override
	protected double getMoveCostTires() {
		return 3;
	}

	@Override
	protected double getMoveCostTreads() {
		return 2;
	}

}
