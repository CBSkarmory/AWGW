package cbskarmory.units.land;

import cbskarmory.Player;
import cbskarmory.weapons.Suit;
import cbskarmory.weapons.WeaponType;

/**
 * a Mech is an Infantry
 * Gets ROCKET_LAUNCHER, RIFLE
 * Costs 300
 * Does OK vs armor, well vs trucks when attacking first.
 */
public class Mech extends Infantry {

    public Mech(Player owner) {
        super(owner);
        setWeapon(0, WeaponType.ROCKET_LAUNCHER);
    }

    public Mech(Player p, Suit s) {
        super(p, s);
        setWeapon(0, WeaponType.ROCKET_LAUNCHER);
    }

    @Override
    public int getBuildCost() {
        return 300;
    }

}
