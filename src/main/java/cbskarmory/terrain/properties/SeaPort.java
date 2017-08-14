package cbskarmory.terrain.properties;

import info.gridworld.actor.Actor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;

import cbskarmory.Player;
import cbskarmory.TerrainGrid;
import cbskarmory.units.Unit;
import cbskarmory.units.sea.*;

/**
 * A Barracks is a factory
 * Barracks builds land Units.
 */
public class SeaPort extends Factory {

    /**
     * Constructs a Barracks with given row and column coordinates.
     * all land Unit constructors are added to buildableUnits
     *
     * @param r the row
     * @param c the column
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public SeaPort(int r, int c, TerrainGrid<Actor> hostGrid, Player owner) throws NoSuchMethodException, SecurityException {
        super(r, c, hostGrid, owner);
        this.buildableUnits = new ArrayList<Constructor<? extends Unit>>();
        buildableUnits.add(Lander.class.getConstructor(Player.class));
        buildableUnits.add(Cruiser.class.getConstructor(Player.class));
        buildableUnits.add(Sub.class.getConstructor(Player.class));
        buildableUnits.add(BattleShip.class.getConstructor(Player.class));
        buildableUnits.add(Carrier.class.getConstructor(Player.class));
    }

    @Override
    protected double getMoveCostBoat() {
        return 1;
    }

}
