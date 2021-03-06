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
import cbskarmory.Runner;
import cbskarmory.PassiveFlag.MoveType;
import cbskarmory.terrain.Terrain;
import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;

public class HiddenUnit extends Unit {
    private Unit containedUnit;

    public Unit getContainedUnit() {
        return containedUnit;
    }

    public Unit unBox() {
        Grid<Actor> gr = getGrid();
        Terrain loc = (Terrain) getLocation();
        Player owner = getOwner();
        owner.getUnitsControlled().remove(this);
        this.removeSelfFromGrid();
        containedUnit.getOwner().getUnitsControlled().add(containedUnit);
        containedUnit.putSelfInGrid(gr, loc);
        containedUnit.resetMovement();
        return containedUnit;
    }

    public HiddenUnit(Player owner, Stealth s) {
        setOwner(owner);
        containedUnit = s;
    }

    public HiddenUnit(Player owner, Stealth2 s) {
        setOwner(owner);
        ;
        containedUnit = s;
    }

    @Override
    public int getBuildCost() {
        return 0;
    }

    @Override
    public double getBaseArmorResistance() {
        return 0;
    }

    @Override
    public void outOfFuel() {
        //do nothing?
    }

    @Override
    public MoveType getMovementType() {
        return MoveType.AIR;
    }

    @Override
    public String getInfo() {
        return "Currently selected: none.\n\nUse your units to move. Click your factories to build. " +
                //					 "DO NOT use arrow keys or Enter"+
                "P1 money: " + Runner.players[1].getMoney() + "  P2 money: " + Runner.players[2].getMoney();
    }

}
