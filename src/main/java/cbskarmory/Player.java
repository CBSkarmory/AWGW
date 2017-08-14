package cbskarmory;

import java.awt.Color;
import java.util.ArrayList;

import cbskarmory.CO.CO;
import cbskarmory.terrain.properties.HQ;
import cbskarmory.terrain.properties.Property;
import cbskarmory.units.Unit;

/**
 * The Player class models a player
 * Player has team color, money, List of properties owned,
 * List of Units controlled, UID number,
 * static numberOfPlayers
 */
public class Player {

    public static int numberOfPlayers;
    public final CO CO;
    public final int id;
    private ArrayList<Unit> unitsControlled;
    private int money;
    private ArrayList<Property> propertiesOwned;
    private int commTowers;
    private Color teamColor;

    /**
     * Constructs a Player
     *
     * @param startingMoney starting amount of money
     * @param teamColor     team color to be assigned to this Player
     */
    public Player(CO commandingOfficer, int startingMoney, Color teamColor) {
        //if error when setting starting funds, set to 0
        if (setMoney(startingMoney) == -1) {
            setMoney(0);
        } else {
            setMoney(startingMoney);
        }
        this.id = ++Player.numberOfPlayers;
        this.CO = commandingOfficer;
        this.teamColor = teamColor;
        this.unitsControlled = new ArrayList<>();
        this.propertiesOwned = new ArrayList<>();
    }

    /**
     * @return the player's team color
     */
    public Color getTeamColor() {
        return this.teamColor;
    }

    /**
     * @return the amount of money this player has
     */
    public int getMoney() {
        return this.money;
    }

    /**
     * @param money a positive integer to set money to
     * @return the amount of money that this player has after execution
     * or  -1 if the amount was invalid
     */
    public int setMoney(int money) {
        if (money >= 0) {
            this.money = money;
            return this.money;
        } else {
            return -1;
        }
    }

    /**
     * @return a list of all of the units that this player controls
     */
    public ArrayList<Unit> getUnitsControlled() {
        return unitsControlled;
    }

    /**
     * @return the number of Units that this player controls
     */
    public int getNumUnitControlled() {
        return getUnitsControlled().size();
    }

    /**
     * @return a list of all of the Properties that this player owns
     */
    public ArrayList<Property> getPropertiesOwned() {
        return this.propertiesOwned;
    }

    /**
     * @return the number of Properties that this player controls
     */
    public int getNumPropertiesOwned() {
        return getPropertiesOwned().size();
    }

    public int getCommTowers() {
        return commTowers;
    }

    public void setCommTowers(int commTowers) {
        this.commTowers = commTowers;
    }

    public boolean hasHQ() {
        for (Property prop : getPropertiesOwned()) {
            if (prop instanceof HQ) {
                return true;
            }
        }
        return false;
    }
}
