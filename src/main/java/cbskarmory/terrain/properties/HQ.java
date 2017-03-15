/**
 * 
 */
package cbskarmory.terrain.properties;

import cbskarmory.Player;
import cbskarmory.TerrainGrid;
import info.gridworld.actor.Actor;

/**
 * @author George
 *
 */
public class HQ extends Property {

	/**
	 * @param r row
	 * @param c column
	 * @param hostGrid hostGrid
	 * @param owner owner of this HQ
	 */
	public HQ(int r, int c, TerrainGrid<Actor> hostGrid, Player owner) {
		super(r, c, hostGrid, owner);
	}
	@Override
	public int getDefense() {
		return 4;
	}

}
