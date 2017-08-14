/*
 * Copyright(c) 2017 CBSkarmory (https://github.com/CBSkarmory)
 *
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * @author CBSkarmory
 */

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
