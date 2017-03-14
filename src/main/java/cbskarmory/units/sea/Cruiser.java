package cbskarmory.units.sea;

import java.util.ArrayList;

import cbskarmory.Player;
import cbskarmory.PassiveFlag.MoveType;
import cbskarmory.terrain.Terrain;
import cbskarmory.units.Carry;
import cbskarmory.units.Sea;
import cbskarmory.units.Unit;
import cbskarmory.weapons.WeaponType;

/**
 * A Cruiser is a Sea Unit strong against submarines and aircraft
 * Gets DEPTH_CHARGE (direct anti-sub) and FLAK (direct anti-air)
 * Costs 1800
 * Can carry 2 Helicopters (eg 1 BCopter and 1 TCopter carrying 1 Infantry)
 * Costs 1 fuel/turn to stay afloat
 */
public class Cruiser extends Sea implements Carry {

	/**
	 * Constructs a Cruiser
	 * sets primary weapon to DEPTH_CHARGE,
	 * secondary to FLAK
	 * @param owner owner of the unit
	 */
	public Cruiser(Player owner) {
		super(owner);
		setWeapon(0, WeaponType.DEPTH_CHARGE);
		setWeapon(1, WeaponType.FLAK);
		carried = new ArrayList<Unit>();
	}

	private ArrayList<Unit> carried;
	@Override
	public ArrayList<Unit> getUnits() {
		return carried;
	}

	@Override
	public void resupply() {
		//nope
	}
	@Override
	public int getMaxCapacity() {
		return 2;
	}
	@Override
	public boolean canResupply() {
		return false;
	}

	@Override
	public boolean canCarry(Unit u) {
		//can carry 2 choppers
		switch(u.getUnitType()){
		case B_COPTER:
			return true;
		case S_COPTER:
			return true;
		case T_COPTER:
			return true;
		default:
			return false;
		}
	}

	@Override
	public int getBuildCost() {
		return 1800;
	}

	@Override
	public double getBaseArmorResistance() {
		//10% overall resistance
		return 0.90;
	}
	@Override
	public double resist(double damage, String source) {
		//40% resistance to torpedoes
		switch(source){
		case WeaponType.TORPEDO:
			return damage*0.6;
		default:
			return damage;
		}
	}
	@Override
	public boolean couldTarget(Unit toCheck, Terrain hypothetical) { //cannot target land
		return super.couldTarget(toCheck, hypothetical)&&(toCheck instanceof Sub||
				MoveType.AIR.equals(toCheck.getMovementType()));
	}

}
