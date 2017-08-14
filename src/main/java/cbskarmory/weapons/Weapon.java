package cbskarmory.weapons;

import java.util.ResourceBundle;

import cbskarmory.PassiveFlag.COFlag;
import cbskarmory.PassiveFlag.MoveType;
import cbskarmory.terrain.Terrain;
import cbskarmory.units.Air;
import cbskarmory.units.Unit;


public class Weapon {

	//what unit is using the weapon
	protected Unit weilder;

	public Unit getWeilder(){
		return weilder;
	}

	//damage values
	public double dmgInf;
	public double dmgArm;
	public double dmgAir;
	public double dmgSea;
	
	//get max ammo
	public int getMaxAmmo(){
		return 5;
		//TODO fix
//		ResourceBundle b = ResourceBundle.getBundle("weapon_ammo");
//		return Integer.parseInt(b.getString(weaponType));
	}
	
	//load damage values
	protected double[] loadDmgValues(){
		ResourceBundle b = ResourceBundle.getBundle("weapon_dmg");
		String[] tmp = b.getString(weaponType).split(",");
		double[] ans = new double[tmp.length];
		for (int i = 0; i<tmp.length;i++){
			ans[i]=Double.parseDouble(tmp[i]);
		}
		return ans;
	}
	
	//type of weapon
	private String weaponType;
	public String getWeaponType(){
		return weaponType;
	}
	
	//constructor
	public Weapon(String weaponType, Unit weilder){
		this.weilder = weilder;
		this.weaponType= weaponType;
		double dmg[] = loadDmgValues();
		this.dmgInf = dmg[0];
		this.dmgArm = dmg[1];
		this.dmgAir = dmg[2];
		this.dmgSea = dmg[3];
	}
	/**
	 * Luck can be impacted by CO powers, possibly allowing it do add more than 10% damage
	 * @return a random number from 1-10 for bonus % damage
	 */
	public static int luck(){
		return (int)Math.round(Math.random()*10+0.1);
	}
	
	//damage calculation
	/**
	 * Damage is impacted by target's base armor, terrain resistance, special resistance, CO powers, and attacking unit HP.
	 * This method does not factor in {@link Weapon.luck}, which adds an additional 0-10% damage (excluding CO powers).
	 * @return hypothetical damage dealt in percent 
	 */
	public int damageCalc(Unit target){
		int ans = 0;double base = 0;
		switch (target.getMovementType()) {
		case FOOT:
			base = this.dmgInf;break;
		case TIRES:
			base = this.dmgArm;break;
		case TREADS:
			base = this.dmgArm;break;
		case AIR:
			base = this.dmgAir;break;
		case SEA:
			base = this.dmgSea;break;
		default:
			base = 0;
		}
		base/=2.0;
		Terrain t = (Terrain)(target.getLocation());
		int defStar = t.getDefense();
		if(MoveType.AIR.equals(target.getMovementType())){
			defStar = 0;
		}
		//terrain defense
		double dMilti1 = (10.0-defStar)/10.0;
		if(target instanceof Air){
			dMilti1 = 1.0;
		}
		//base armor
		double dMulti2 = target.getBaseArmorResistance();
		//attacker strength
		double dMulti3 = this.weilder.getHealth()/100.0;
		//Comm towers
		double dMulti4 = (this.weilder.getOwner().getCommTowers()/10.0)+1;
		double preResistAns = base*dMilti1*dMulti2*dMulti3*dMulti4;
		preResistAns = target.getOwner().CO.passive(preResistAns, COFlag.DEFENSE, target.getUnitType());
		//Pseudo-round and convert to percent
		ans = (int) (target.resist(preResistAns, this.weaponType)*10);
		return ans;
	}
}
