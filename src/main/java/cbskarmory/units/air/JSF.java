package cbskarmory.units.air;

import cbskarmory.Player;
import cbskarmory.terrain.Terrain;
import cbskarmory.units.Stealth;
import cbskarmory.units.Unit;
import cbskarmory.weapons.WeaponType;

/**
 * The Joint Strike Fighter is a stealth aircraft
 * Gets MISSILES (ranged A2A), ROCKET_POD (direct A2G)
 *  Costs 4000
 *  Costs 5 fuel/turn to stay airborne, extra 4 when hidden (9 total/turn)
 */
public class JSF extends Stealth {

	/**
	 * Constructs a Joint Strike Fighter.
	 * sets primary weapon to MISSILES, secondary to ROCKET_POD
	 * @param owner owner of the unit
	 */
	public JSF(Player owner) {
		super(owner);
		setWeapon(0, WeaponType.MISSILES);
		setWeapon(1, WeaponType.ROCKET_POD);
	}

	@Override
	public int getExtraDailyCost() {
		return 4;
	}

	@Override
	public int getBuildCost() {
		return 4000;
	}

	@Override
	public double getBaseArmorResistance() {
		return 1;
	}
	@Override
	public int getDailyCost() {
		return 5;
	}
	@Override
	public double resist(double damage, String source) {
		switch(source){
		case WeaponType.MISSILES:
			return damage*0.7;
		case WeaponType.MISSILE:
			return damage*0.7;
		default:
			return damage*0.85;
		}
	}
	@Override
	public boolean couldTarget(Unit toCheck, Terrain hypothetical){
		if(null==toCheck){
			return false; //can't target nothing
		}
		int dist = hypothetical.getDistanceTo((Terrain) toCheck.getLocation());
		return (dist>=1&&dist<=5);
	}
	@Override
	public boolean canTarget(Unit toCheck){
		Terrain hypothetical = (Terrain) getLocation();
		if(null==toCheck){
			return false; //can't target nothing
		}
		int dist = hypothetical.getDistanceTo((Terrain) toCheck.getLocation());
		return (dist>=3&&dist<=5);
	}

}
