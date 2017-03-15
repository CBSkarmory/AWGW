package cbskarmory.units.land;

import cbskarmory.Player;
import cbskarmory.PassiveFlag.MoveType;
import cbskarmory.terrain.Terrain;
import cbskarmory.units.Unit;
import cbskarmory.weapons.WeaponType;

/**
 * An Rockets truck is a Unit
 * Gets ROCKET -- ranged ground-to-ground
 *  Costs 1500
 *  RMoveType TIRES
 * Ranged attack vs ground only, Manhattan radius 3-5 
 */
public class Rockets extends Unit{

	/**
	 * Constructs a Rockets truck
	 * sets primary weapon to ROCKET
	 * @param owner the owner of this unit
	 */
	public Rockets(Player owner) {
		super(owner);
		setWeapon(0, WeaponType.ROCKET);
	}
	@Override
	public boolean couldTarget(Unit toCheck, Terrain hypothetical){
		if(!getLocation().equals(hypothetical)){
			return false;//cannot move and fire
		}
		if(null==toCheck||MoveType.AIR.equals(toCheck.getMovementType())){
			return false; //can't target nothing, can target sea, land
		}
		int dist = hypothetical.distanceTo((Terrain) toCheck.getLocation());
		return (dist>=3&&dist<=5);
	}
	@Override
	public boolean canCounter(Unit u) {
		return false;
	}
	@Override
	public int getBuildCost() {
		return 1500;
	}

	@Override
	public double getBaseArmorResistance() {
		return 1;
	}

	@Override
	public void outOfFuel() {
		//do nothing
		
	}

	@Override
	public MoveType getMovementType() {
		return MoveType.TIRES;
	}


}
