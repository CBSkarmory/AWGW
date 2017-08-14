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

package cbskarmory.units;

import cbskarmory.Player;
import cbskarmory.PassiveFlag.MoveType;

/**
 * abstract Air represents all air units
 * Air units use some fuel every day to stay in flight and will crash without fuel.
 * These get MoveType.AIR
 * Air units traverse all Terrains for 1 mobility
 */
public abstract class Air extends Unit {

    /**
     * calls super(Player) from child classes
     * don't invoke this
     *
     * @param owner owner of unit
     */
    public Air(Player owner) {
        super(owner);
    }

    @Override
    public void outOfFuel() {
        //TODO crash animation
        this.selfDestruct(true);
    }

    @Override
    public MoveType getMovementType() {
        return MoveType.AIR;
    }

}
