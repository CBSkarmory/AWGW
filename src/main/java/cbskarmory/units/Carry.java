package cbskarmory.units;

import java.util.ArrayList;
/**
 * interface Carry represents all Units that can carry other Units
 * Can carry up to getMaxCapacity() units that meet certain requirements in carryCheck().
 *  Some that implement this can also resupply allied units.
 *  If the unit implementing this is destroyed, then all carried units are destroyed. 
 *  Implemented by APC, TCopter, DropShip, Lander, Cruiser, Carrier(and more TBI)
 */
public interface Carry {
	/**
	 * @return whether or not this Carry can carry any more Units
	 */
	default boolean isFull(){
		return getMaxCapacity()==getUnits().size();
	}

	/**
	 * @return maximum number of Units this can carry
	 */
	default public int getMaxCapacity(){
		return 1;
	}

	/**
	 * @return a List of the units carried (can be empty)
	 */
	public ArrayList<Unit> getUnits();

	/**
	 * Refills {@link fuel} and {@link ammo} for adjacent allied {@link Unit}s
	 */
	public void resupply();

	/**
	 * @return whether or not this Carry has resupplying capabilities
	 */
	public boolean canResupply();
	
	/**
	 * Precondition: Carry is not full and Carry can carry the Unit
	 *   precondition is checked with canCarry() and isFull()
	 * PostCondition: getUnits() returns ArrayList containing Unit u
	 */
	public default void addUnit(Unit u){
		this.getUnits().add(u);
	}
	
	/**
	 * Drops off one carried Unit in valid Manhattan adjacent tile
	 * can still move after this action; this can drop off multiple Units given space
	 * Precondition: This Carry is carrying at least one Unit for which
	 * it can be dropped in a Manhattan adjacent tile
	 */
	public default void drop(){
		//TODO Implement me
	}
	
	/**
	 * @return whether or not this Carry can carry the Unit
	 */
	boolean canCarry(Unit u);

}
