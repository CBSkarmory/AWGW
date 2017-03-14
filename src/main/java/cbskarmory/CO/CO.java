package cbskarmory.CO;

import cbskarmory.PassiveFlag.COFlag;
import cbskarmory.PassiveFlag.UnitType;

/**
 * Commanding Officer: other classes are COs and implement methods. CO itself cannot be instantiated.
 */
public abstract class CO {
	//mandatory
	public abstract void power();
	public abstract void superPower();
	public abstract int getPowerCost();
	public abstract int getSuperPowerCost();
	public abstract double passive(double number, COFlag tag, UnitType unitType);
}
