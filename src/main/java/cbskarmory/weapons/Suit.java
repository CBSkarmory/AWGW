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

package cbskarmory.weapons;

public class Suit {
    /**
     * {@link NANOWEAVE} reduces damage from bullets by 10% per level, from:
     * {@link WeaponType.RIFLE} and {@link WeaponType.MG}
     * <p>{@link Suit.SuitType.FLAK} reduces damage from explosives by 10% per level, from:
     * {@link WeaponType.HE}, {@link WeaponType.ROCKET_POD}, and {@link WeaponType.HEAT}
     */
    public enum SuitType{
        NANOWEAVE,
        FLAK,
    }

    public Suit(SuitType s, int level) {
        this.equipped = s;
        this.suitLevel = level;
    }

    public Suit(){
        this.equipped = null;
        this.suitLevel = 0;
    }

    private SuitType equipped;
    private int suitLevel;
    public SuitType getSuitType() {
        return equipped;
    }

    public int getSuitLevel() {
        return suitLevel;
    }

    public double resist(double damage,String source){
        switch(getSuitType()){
            default:
                return damage;
            case NANOWEAVE:
                if(source.equals(WeaponType.RIFLE)||source.equals(WeaponType.MG)){
                    damage*=(1-(0.1*getSuitLevel()));return damage;
                }break;
            case FLAK:
                if(source.equals(WeaponType.HE)||source.equals(WeaponType.HEAT)||source.equals(WeaponType.ROCKET)||
                        source.equals(WeaponType.ROCKET_POD)||source.equals(WeaponType.SHELL)){
                    damage*=(1-(0.1*getSuitLevel()));return damage;
                }break;
        }
        return damage;
    }
}
