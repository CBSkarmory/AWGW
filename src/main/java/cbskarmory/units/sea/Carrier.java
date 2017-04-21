package cbskarmory.units.sea;

import java.util.ArrayList;

import cbskarmory.Player;
import cbskarmory.PassiveFlag.MoveType;
import cbskarmory.terrain.Terrain;
import cbskarmory.units.Air;
import cbskarmory.units.Carry;
import cbskarmory.units.Sea;
import cbskarmory.units.Unit;
import cbskarmory.weapons.WeaponType;

/**
 * The aircraft carrier is a sea unit
 * Gets ROTARY (midrange anti-air)
 * Can target aircraft in Manhattan radius of 1-4
 * Costs 3000
 * Can carry 3 air units
 * Costs 1 fuel/turn to stay afloat
 */
public class Carrier extends Sea implements Carry {

	/**
	 * Constructs am aircraft carrier
	 * sets primary weapon to ROTARY
	 * @param owner owner of the unit
	 */
	public Carrier(Player owner) {
		super(owner);
		setWeapon(0, WeaponType.ROTARY);
		carried = new ArrayList<Unit>();
	}

	ArrayList<Unit> carried;
	@Override
	public ArrayList<Unit> getUnits() {
		return carried;
	}
	@Override
	public int getMaxCapacity() {
		return 3;
	}

	@Override
	public void resupply() {
		//nope
	}

	@Override
	public boolean canResupply() {
		return false;
	}

	@Override
	public boolean canCarry(Unit u) {
		//can carry 3 air units
		if(u instanceof Air){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public int getBuildCost() {
		return 3000;
	}

	@Override
	public double getBaseArmorResistance() {
		//15% overall resistance
		return 0.85;
	}
	@Override
	public boolean couldTarget(Unit toCheck, Terrain hypothetical){
		if(!hypothetical.equals(getLocation())){
			return false;//cannot move and fire
		}
		if(null==toCheck||!MoveType.AIR.equals(toCheck.getMovementType())){
			return false; //can't target nothing, can only target Air units
		}
		int dist = hypothetical.getDistanceTo((Terrain) toCheck.getLocation());
		return (dist>=1&&dist<=4);
	}
	@Override
	public boolean canCounter(Unit u) {
		return false;
	}
}
