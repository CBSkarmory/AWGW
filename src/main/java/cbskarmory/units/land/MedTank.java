package cbskarmory.units.land;

import cbskarmory.Player;
import cbskarmory.PassiveFlag.MoveType;
import cbskarmory.terrain.Terrain;
import cbskarmory.units.Unit;
import cbskarmory.weapons.WeaponType;

/*
 * A MedTank is a Tank
 * Gets HEAT and MG
 * Costs 700
 * MoveType TREADS
 * Medium tank is all-around medium strong unit
 */
public class MedTank extends Tank {

    /**
     * Constructs a Medium Tank
     * sets primary weapon to HEAT, secondary to MG
     *
     * @param owner owner of the Unit
     */
    public MedTank(Player owner) {
        super(owner);
        setWeapon(0, WeaponType.HEAT);
        setWeapon(1, WeaponType.MG);
    }

    @Override
    public int getBuildCost() {
        return 1400;
    }

    @Override
    public double getBaseArmorResistance() {
        //25% resistance
        return 0.75;
    }

    @Override
    public boolean couldTarget(Unit toCheck, Terrain hypothetical) { //cannot target jet fighters
        return super.couldTarget(toCheck, hypothetical) && !toCheck.isJet();
    }

}
