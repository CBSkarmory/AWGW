package cbskarmory;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import cbskarmory.CO.TestCO;
import cbskarmory.terrain.*;
import cbskarmory.terrain.properties.*;
import cbskarmory.units.*;
import cbskarmory.units.air.*;
import cbskarmory.units.land.*;
import cbskarmory.units.sea.*;
import info.gridworld.actor.Actor;
import info.gridworld.world.AVWorld;

//TODO add debug map
public class Runner {

    private static final boolean __DEBUG_MODE = false;
    public static Player[] players;
    public static final int externalFPS = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
            .getDisplayMode().getRefreshRate();
    private static int fpsTarget;

    public static int getFpsTarget() {
        return fpsTarget;
    }

    /**
     * precondition: 0 < fpsTarget < 1001
     * Foreground always runs at system refresh rate
     * @param fpsTarget target # of frames per second to update background at
     */
    public static void setFpsTarget(int fpsTarget) {
        if (fpsTarget > 1000 || fpsTarget < 1) {
            throw new IllegalArgumentException("fps target out of range");
        }else if(fpsTarget>Runner.externalFPS){
            JOptionPane.showMessageDialog(null, "Error: your monitor's current resolution is running at"
                    + Runner.externalFPS + " hz , which is <" + fpsTarget + " hz","Bad FPS target",0,null);
        }else{
            Runner.fpsTarget = fpsTarget;
            Runner.msDelay = (int) Math.ceil(1000.0 / fpsTarget);
        }
    }

    public static int getMsDelay() {
        return msDelay;
    }

    private static int msDelay;
    public final static int MONEY_PER_PROPERTY = 200;
    public static String pathToMapPic;


    public static void main(String[] args) {

        setFpsTarget(20);
        AVWorld avw = new AVWorld();
        Player p1 = new Player(new TestCO(), 0, new Color(255, 80, 45));
        Player p2 = new Player(new TestCO(), 0, new Color(75, 150, 255));
        setUpPlayers(p1, p2);
        TerrainGrid<Actor> g = null;
        String[] options = { "map 1", "map 2", "Exit" };
        int selection;
        if(__DEBUG_MODE==false){
            selection = JOptionPane.showOptionDialog(null,
                    "which map?\n\tMap 1: small map (10x10)\n\tMap 2: large map (24x24)", "Map Selection", 0, 0,
                    getMainIcon(), options, 2);
        }else{
            selection = -1;
        }
        if (selection == 1) {
            g = new TerrainGrid<Actor>(24, 24);
            fillTerrainGrid(g, "resources/maps/map2.dat");
            pathToMapPic = "resources/maps/Map_2.png";
            minFill2(g, p1, p2);
            renderAVW(avw, g);
            avw.getWorldFrame().control.display.zoomIn();
        } else if (selection == 0) {
            pathToMapPic = "resources/maps/Map_1.gif";
            g = new TerrainGrid<Actor>(10, 10);
            fillTerrainGrid(g, "resources/maps/map1.dat");
            minFill1(g, p1, p2);
            renderAVW(avw, g);
        } else if (selection==-1) {
            System.out.println("ENTERING DEBUG MAP");
            //TODO add debug map
        } else {
            System.exit(0);
        }
        cycleTurnPlayer();
        avw.getWorldFrame().showDirectionsPopup();
        while (allPlayersCompeting(getCompetitivePlayers())) {
            avw.setMessage("Currently selected: none.\n\nUse your units to move. Click your factories to build. "
                    + "P1 money: " + players[1].getMoney() + "(+"
                    + players[1].getNumPropertiesOwned() * MONEY_PER_PROPERTY + ")  P2 money: " + players[2].getMoney()
                    + "(+" + players[2].getNumPropertiesOwned() * MONEY_PER_PROPERTY + ")");
            avw.go();
        }
        avw.getWorldFrame().control.turnCycleButton.setEnabled(false);
        String victoryDiag = victoryDiag();
        avw.setMessage(victoryDiag);
        JOptionPane.showMessageDialog(avw.getWorldFrame(), (victoryDiag), "Game Over", 0,
                new ImageIcon(Runner.class.getClassLoader().getResource("resources/victory.png")));
    }

    public static void renderAVW(AVWorld avw, TerrainGrid<Actor> g) {
        avw.setGrid(g);
        avw.show();
    }

    private static void setUpPlayers(Player p1, Player p2) {
        players = new Player[3];
        players[p1.id] = p1;
        players[p2.id] = p2;
        turnPlayer = players[2];
    }

    private static ImageIcon getMainIcon() {
        URL icoLoc = Runner.class.getClassLoader().getResource("resources/icon.png");
        return new ImageIcon(icoLoc);
    }

    private static void customFillTest1(TerrainGrid<Actor> g, Player p1, Player p2) {
        Infantry inf1 = new Infantry(players[1]);
        inf1.putSelfInGrid(g, g.getLocationArray()[1][1]);
        Infantry inf2 = new Infantry(players[1]);
        inf2.putSelfInGrid(g, g.getLocationArray()[9][6]);
        Mech mech1 = new Mech(players[2]);
        mech1.putSelfInGrid(g, g.getLocationArray()[2][8]);
        MedTank medTank1 = new MedTank(players[1]);
        medTank1.putSelfInGrid(g, g.getLocationArray()[1][8]);
        APC apc1 = new APC(players[1]);
        apc1.putSelfInGrid(g, g.getLocationArray()[2][1]);
        Carrier carrier1 = new Carrier(players[1]);
        carrier1.putSelfInGrid(g, g.getLocationArray()[3][6]);
        BCopter copter1 = new BCopter(players[1]);
        copter1.putSelfInGrid(g, g.getLocationArray()[7][7]);
        TCopter copter2 = new TCopter(players[1]);
        copter2.putSelfInGrid(g, g.getLocationArray()[7][6]);
        AdvFighter avf1 = new AdvFighter(players[1]);
        avf1.setFuel(1.0);
        avf1.putSelfInGrid(g, g.getLocationArray()[5][5]);
    }

    private static void minFill1(TerrainGrid<Actor> g, Player p1, Player p2) {
        Infantry inf1 = new Infantry(players[2]);
        inf1.putSelfInGrid(g, g.getLocationArray()[6][0]);
        Infantry inf3 = new Infantry(players[2]);
        inf3.putSelfInGrid(g, g.getLocationArray()[7][1]);
        Infantry inf2 = new Infantry(players[1]);
        inf2.putSelfInGrid(g, g.getLocationArray()[6][9]);
    }

    private static void minFill2(TerrainGrid<Actor> g, Player p1, Player p2) {
        Infantry inf1 = new Infantry(players[2]);
        inf1.putSelfInGrid(g, g.getLocationArray()[2][19]);
        Infantry inf3 = new Infantry(players[2]);
        inf3.putSelfInGrid(g, g.getLocationArray()[2][17]);
        Infantry inf2 = new Infantry(players[1]);
        inf2.putSelfInGrid(g, g.getLocationArray()[21][4]);
    }

    /**
     * A player is competitve if he/she controls Units and his/her HQ To knock
     * out another Player, destroy all of their Units or capture his/her HQ
     *
     * @return whether or not all players are competitive
     */
    private static boolean allPlayersCompeting(ArrayList<Player> competitivePlayers) {
        // TODO more than 2 player support
        return competitivePlayers.size() == 2;
    }

    private static ArrayList<Player> getCompetitivePlayers() {
        ArrayList<Player> competitivePlayers = new ArrayList<>();
        for (Player p : players) {
            if (null != p && p.getUnitsControlled().size() > 0) {
                boolean hasHQ = p.hasHQ();
                if (hasHQ) {
                    competitivePlayers.add(p);
                }
            }
        }
        return competitivePlayers;
    }

    /**
     * Precondition: a Player has won
     *
     * @return a description of the Player's victory
     */
    private static String victoryDiag() {
        ArrayList<Player> allPlayers = new ArrayList<Player>(
                Arrays.asList(players).stream().filter(p -> null != p).collect(Collectors.toList())),
                compPlayers = getCompetitivePlayers();
        StringBuilder victoryMessage = new StringBuilder(
                "Congratulations to Player " + compPlayers.get(0).id + " for eliminating\n");
        allPlayers.remove(compPlayers.get(0));
        for (Player loser : allPlayers) {
            String loserName = "Player " + loser.id;
            victoryMessage.append("\t" + loserName + " by ");
            if (!loser.hasHQ()) {
                victoryMessage.append("capturing " + loserName + "'s HQ");
            }
            if (loser.getUnitsControlled().size() == 0) {
                victoryMessage.append("destroying all of " + loserName + "'s units");
            }
        }
        return victoryMessage.toString();
    }

    public static void fillTerrainGrid(TerrainGrid<Actor> g, String fileName) {

        // String fileName = JOptionPane.showInputDialog("file name");
        try {
            Scanner sc = new Scanner(Runner.class.getClassLoader().getResourceAsStream(fileName));
            if (!(g.getNumRows() == sc.nextInt() && g.getNumCols() == sc.nextInt())) {
                System.out.println("map size != grid size");
                throw new Exception("map size != grid size");
            }
            // hmm??? good
            sc.nextLine();
            for (int r = 0; r < g.getNumRows(); r++) {

                String[] rowStringForm = sc.nextLine().split(",");
                // System.out.println("good\n");
                // System.out.print(new
                // ArrayList<String>(Arrays.asList(rowStringForm)));
                for (int c = 0; c < g.getNumCols(); c++) {
                    try {
                        g.getLocationArray()[r][c] = makeTerrain(r, c, g, rowStringForm[c]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("array out of bounds");
                        System.err.println("check map, row:" + r + "\tcol:" + c);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("error reading file");
            e.printStackTrace();
        }
    }

    private static Player turnPlayer;

    public static Player getTurnPlayer() {
        return turnPlayer;
    }

    public static Player getNextTurnPlayer() {
        Player old = getTurnPlayer(), next;
        try {
            if (players[old.id + 1] == null) {
                next = players[1];
            } else {
                next = players[old.id + 1];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            next = players[1];
        }
        return next;
    }

    public static Player cycleTurnPlayer() {
        Player old = turnPlayer;
        Player next;
        Iterator<Unit> i0 = old.getUnitsControlled().iterator();
        while (i0.hasNext()) {
            Unit u = i0.next();
            if (u.canMove()) {
                u.immobilize();
            }
            if (u instanceof Stealth && ((Stealth) u).isHidden()) {
                Stealth s = (Stealth) u;
                s.hideRender();
            } else if (u instanceof Stealth2 && ((Stealth2) u).isHidden()) {
                Stealth2 s = (Stealth2) u;
                s.hideRender();
            }
        }
        next = getNextTurnPlayer();
        // reset movement
        Iterator<Unit> i = next.getUnitsControlled().iterator();
        while (i.hasNext()) {
            Unit u = i.next();
            u.resetMovement();
            if (u instanceof HiddenUnit) {
                ((HiddenUnit) u).unBox();
            }
        }
        // supply from properties
        Iterator<Property> i2 = next.getPropertiesOwned().iterator();
        while (i2.hasNext()) {
            Property p = i2.next();
            p.tryResupply();
        }

        turnPlayer = next;
        turnPlayer.getPropertiesOwned().get(0).getHostGrid().hostWorld.getWorldFrame()
                .setTitle("AdvanceWars GridWorld ("+Runner.fpsTarget+"hz) : player " + turnPlayer.id);
        for (Property p : turnPlayer.getPropertiesOwned()) {
            turnPlayer.setMoney(turnPlayer.getMoney() + MONEY_PER_PROPERTY);
            if (p.getCapTimer() != p.FULL_CAP_TIMER
                    && (p.getHostGrid().get(p) == null || ((Unit) p.getHostGrid().get(p)).getOwner() == p.getOwner())) {
                p.resetCapTimer();
            }
        }
        return turnPlayer;

    }

    public static Terrain makeTerrain(int r, int c, TerrainGrid<Actor> hostGrid, String terrainType) throws Exception {
        switch (terrainType) {
            case "Beach":
                return new Beach(r, c, hostGrid);
            case "Plains":
                return new Plains(r, c, hostGrid);
            case "Bridge":
                return new Bridge(r, c, hostGrid);
            case "Forest":
                return new Forest(r, c, hostGrid);
            case "Mountain":
                return new Mountain(r, c, hostGrid);
            case "Ocean":
                return new Ocean(r, c, hostGrid);
            case "Reef":
                return new Reef(r, c, hostGrid);
            case "River":
                return new River(r, c, hostGrid);
            case "Road":
                return new Road(r, c, hostGrid);
            case "Test":
                return new TestTerrain(r, c, hostGrid);
            default:
                String[] propProp = terrainType.split("_");
                if (propProp.length != 2) {
                    throw new Exception("error reading file, bad property?");
                } else {
                    String ownerID = propProp[1];
                    Player ownerOfProp;
                    if (!("none".equals(ownerID) || "neutral".equals(ownerID))) {
                        ownerOfProp = players[Integer.parseInt(ownerID)];
                    } else {
                        ownerOfProp = null;
                    }
                    switch (propProp[0]) {
                        case "Property":
                            return new Property(r, c, hostGrid, ownerOfProp);
                        case "Barracks":
                            return new Barracks(r, c, hostGrid, ownerOfProp);
                        case "SeaPort":
                            return new SeaPort(r, c, hostGrid, ownerOfProp);
                        case "AirPort":
                            return new AirPort(r, c, hostGrid, ownerOfProp);
                        case "HQ":
                            return new HQ(r, c, hostGrid, ownerOfProp);
                        default:
                            System.err.println("r:" + r + "\tc:" + c);
                            throw new Exception("error reading file, bad property?");

                    }
                }
        }
    }
}
