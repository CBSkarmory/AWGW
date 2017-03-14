package cbskarmory.CO;

import cbskarmory.PassiveFlag.COFlag;
import cbskarmory.PassiveFlag.UnitType;

public class TestCO extends CO {
	@Override
	public void power() {
		//Nothing

	}

	@Override
	public void superPower() {
		//nothing

	}

	@Override
	public int getPowerCost() {
		return 99;
	}

	@Override
	public int getSuperPowerCost() {
		return 99;
	}

	@Override
	public double passive(double number, COFlag tag, UnitType unitType) {
		return number;
	}

}
