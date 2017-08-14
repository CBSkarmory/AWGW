package cbskarmory.units.air;

import cbskarmory.Player;
import cbskarmory.terrain.Terrain;
import cbskarmory.units.Stealth;
import cbskarmory.units.Unit;
import cbskarmory.weapons.WeaponType;

/*
 * A stealth helicopter is a stealth air unit
 * Gets ROCKET_POD and MG
 * Costs 1900
 * Costs 2 fuel/turn to stay airborne, costs extra 4 when hidden (6 total /turn)
 * very effective at harassing enemy land units 
 */
public class SCopter extends Stealth {

    /**
     * Constructs a stealth helicopter.
     * sets primary weapon to ROCKET_POD, secondary to MG
     *
     * @param owner owner of the unit
     */
    public SCopter(Player owner) {
        super(owner);
        setWeapon(0, WeaponType.ROCKET_POD);
        setWeapon(1, WeaponType.MG);
    }

    @Override
    public int getExtraDailyCost() {
        return 4;
    }

    @Override
    public int getBuildCost() {
        return 1900;
    }

    @Override
    public double getBaseArmorResistance() {
        return 1;
    }

    @Override
    public int getDailyCost() {
        return 2;
    }

    @Override
    public boolean couldTarget(Unit toCheck, Terrain hypothetical) { //cannot target jet fighters
        return super.couldTarget(toCheck, hypothetical) && !toCheck.isJet();
    }

    @Override
    public double resist(double damage, String source) {
        switch (source) {
            case WeaponType.MISSILES:
                return damage * 0.7;
            case WeaponType.MISSILE:
                return damage * 0.7;
            default:
                return damage * 0.85;
        }
    }
}
