package cbskarmory.units.air;

import java.util.ArrayList;

import cbskarmory.Player;
import cbskarmory.units.Air;
import cbskarmory.units.Carry;
import cbskarmory.units.Unit;
import cbskarmory.weapons.WeaponType;

/**
 * The transport helicopter carriers infantry
 * Gets NONE (unarmed)
 * Costs 500
 * Can carry 1 infantry -- good troop transport, crosses all terrain
 * Costs 2 fuel/turn to stay airborne
 */
public class TCopter extends Air implements Carry {

    private ArrayList<Unit> carried;

    /**
     * Constructs a transport helicopter
     * sets primary weapon to NONE (unarmed)
     *
     * @param owner owner of the unit
     */
    public TCopter(Player owner) {
        super(owner);
        setWeapon(0, WeaponType.NONE); // unarmed
        carried = new ArrayList<Unit>();
    }

    @Override
    public ArrayList<Unit> getUnits() {
        return carried;
    }

    @Override
    public void resupply() {
        //cannot resupply
    }

    @Override
    public boolean canResupply() {
        return false;
    }

    @Override
    public boolean canCarry(Unit u) {
        switch (u.getMovementType()) {
            case FOOT:
                return true;
            default:
                return false;

        }
    }

    @Override
    public int getBuildCost() {
        return 500;
    }

    @Override
    public double getBaseArmorResistance() {
        return 1;
    }

    @Override
    public int getDailyCost() {
        return 2;
    }

}
