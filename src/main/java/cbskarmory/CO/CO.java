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
