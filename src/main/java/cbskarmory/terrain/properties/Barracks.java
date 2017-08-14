package cbskarmory.terrain.properties;

import info.gridworld.actor.Actor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;

import cbskarmory.Player;
import cbskarmory.TerrainGrid;
import cbskarmory.units.Unit;
import cbskarmory.units.land.*;
import cbskarmory.weapons.Suit;

/**
 * A Barracks is a factory
 * Barracks builds land Units.
 */
public class Barracks extends Factory {

    /**
     * Constructs a Barracks with given row and column coordinates.
     * all land Unit constructors are added to buildableUnits
     *
     * @param r the row
     * @param c the column
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public Barracks(int r, int c, TerrainGrid<Actor> hostGrid, Player owner) throws NoSuchMethodException, SecurityException {
        super(r, c, hostGrid, owner);
        this.buildableUnits = new ArrayList<Constructor<? extends Unit>>();
        buildableUnits.add(Infantry.class.getConstructor(Player.class));
        buildableUnits.add(Mech.class.getConstructor(Player.class));
        buildableUnits.add(Recon.class.getConstructor(Player.class));
        buildableUnits.add(APC.class.getConstructor(Player.class));
        buildableUnits.add(Artillery.class.getConstructor(Player.class));
        buildableUnits.add(Tank.class.getConstructor(Player.class));
        buildableUnits.add(AntiAir.class.getConstructor(Player.class));
        buildableUnits.add(Missiles.class.getConstructor(Player.class));
        buildableUnits.add(MedTank.class.getConstructor(Player.class));
        buildableUnits.add(Rockets.class.getConstructor(Player.class));
        buildableUnits.add(HeavyTank.class.getConstructor(Player.class));
    }

}
