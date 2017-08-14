package cbskarmory.units.air;

import cbskarmory.Player;
import cbskarmory.PassiveFlag.MoveType;
import cbskarmory.terrain.Terrain;
import cbskarmory.units.Stealth;
import cbskarmory.units.Unit;
import cbskarmory.weapons.WeaponType;

/**
 * the advanced fighter is a stealth aircraft
 * Gets MISSILE (direct A2A) , ROTARY (ranged A2A)
 * Costs 3000
 * Costs 5 fuel/turn to stay airborne, costs 5 extra when hidden (10 total /turn)
 * Highly mobile stealth air superiority fighter
 */
public class AdvFighter extends Stealth {

    /**
     * Constructs an advanced fighter.
     * sets primary weapon to MISSILE, secondary to ROTARY
     *
     * @param owner owner of the unit
     */
    public AdvFighter(Player p) {
        super(p);
        setWeapon(0, WeaponType.MISSILE);
        setWeapon(1, WeaponType.ROTARY);
    }


    //this aircraft has fuel issues, but fast
    @Override
    public int getExtraDailyCost() {
        return 5;
    }

    @Override
    public int getBuildCost() {
        return 4000;
    }

    @Override
    public double getBaseArmorResistance() {
        //10% of damage misses the aircraft
        return 0.90;
    }

    /**
     * 90% of small arms miss, HMG lacks net velocity -- 90% damage reduction
     * missiles have limited effectiveness against stealth, 30% damage reduction
     */
    @Override
    public double resist(double damage, String source) {
        //
        switch (source) {
            case WeaponType.RIFLE:
                return damage * 0.1;
            case WeaponType.MG:
                return damage * 0.1;
            case WeaponType.HMG:
                return damage * 0.1;
            case WeaponType.MISSILE:
                return damage * 0.7;
            case WeaponType.MISSILES:
                return damage * 0.7;
            default:
                return damage;
        }
    }

    @Override
    public boolean couldTarget(Unit toCheck, Terrain hypothetical) {
        if (null == toCheck || !MoveType.AIR.equals(toCheck.getMovementType())) {
            return false; //can't target nothing, can target Air units only
        }
        int dist = hypothetical.getDistanceTo((Terrain) toCheck.getLocation());
        return (dist >= 1 && dist <= 5);
    }

    @Override
    public boolean canCounter(Unit u) {
        return super.canCounter(u);
    }

}
