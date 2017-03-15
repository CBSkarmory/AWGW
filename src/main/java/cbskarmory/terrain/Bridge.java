package cbskarmory.terrain;

import cbskarmory.TerrainGrid;
import info.gridworld.actor.Actor;

/**
 *Bridge is a terrain.
 *Pretty much the same as Road, but this crosses rivers. 
 */
public class Bridge extends Road {

	 /**
	  * Constructs a Bridge with given row and column coordinates.
	  * @param r the row
	  * @param c the column
	  */
	public Bridge(int r, int c, TerrainGrid<Actor> hostGrid){
		super(r, c, hostGrid);
	}

}
