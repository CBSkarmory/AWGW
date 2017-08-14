package cbskarmory;

import info.gridworld.grid.AbstractGrid;
import info.gridworld.grid.Location;
import info.gridworld.world.AVWorld;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A <code>TerrainGrid</code> is a rectangular grid with a finite number of
 * rows and columns. <br />
 * Please only use with Terrains
 */
public class TerrainGrid<E> extends AbstractGrid<E> {
    public AVWorld hostWorld;
    private Object[][] occupantArray; // the array storing the grid elements
    private Location[][] locationArray; // array containing map

    public Location[][] getLocationArray() {
        return locationArray;
    }

    public ArrayList<Location> getAllLocations() {
        ArrayList<Location> ans = new ArrayList<>();
        for (Location[] l0 : locationArray) {
            ans.addAll(new ArrayList<>(Arrays.asList(l0)));
        }
        return ans;
    }

    /**
     * Constructs an empty bounded grid with the given dimensions.
     * (Precondition: <code>rows > 0</code> and <code>cols > 0</code>.)
     *
     * @param rows number of rows in TerrainGrid
     * @param cols number of columns in TerrainGrid
     */
    public TerrainGrid(int rows, int cols) {
        if (rows <= 0)
            throw new IllegalArgumentException("rows <= 0");
        if (cols <= 0)
            throw new IllegalArgumentException("cols <= 0");
        occupantArray = new Object[rows][cols];
        locationArray = new Location[rows][cols];
    }

    public int getNumRows() {
        return occupantArray.length;
    }

    public int getNumCols() {
        // Note: according to the constructor precondition, numRows() > 0, so
        // theGrid[0] is non-null.
        return occupantArray[0].length;
    }

    public boolean isValid(Location loc) {
        if (null == loc) {
            return false;
        }
        return 0 <= loc.getRow() && loc.getRow() < getNumRows()
                && 0 <= loc.getCol() && loc.getCol() < getNumCols();
    }

    public ArrayList<Location> getOccupiedLocations() {
        ArrayList<Location> theLocations = new ArrayList<Location>();

        // Look at all grid locations.
        for (int r = 0; r < getNumRows(); r++) {
            for (int c = 0; c < getNumCols(); c++) {
                // If there's an object at this location, put it in the array.
                Location loc = locationArray[r][c];
                //FIXME something weird
                if (null != loc) {
                    if (get(loc) != null) {
                        theLocations.add(loc);
                    }
                } else {
                    System.out.println("grid[" + r + "][" + c + "] is null, moving on");
                }
            }
        }

        return theLocations;
    }

    public E get(Location loc) {
        //TODO
        if (!isValid(loc))
            throw new IllegalArgumentException("Location " + loc
                    + " is not valid");
        return (E) occupantArray[loc.getRow()][loc.getCol()]; // unavoidable warning
    }

    public E put(Location loc, E obj) {
        if (!isValid(loc))
            throw new IllegalArgumentException("Location " + loc
                    + " is not valid, cannot put in grid");
        if (obj == null)
            throw new NullPointerException("obj == null, cannot put in grid");

        // Add the object to the grid.
        E oldOccupant = get(loc);
        occupantArray[loc.getRow()][loc.getCol()] = obj;
        //System.out.println("success putting in unit. See line 106 of TerrainGrid");
        return oldOccupant;
    }

    public E remove(Location loc) {
        if (!isValid(loc))
            throw new IllegalArgumentException("Location " + loc
                    + " is not valid");

        // Remove the object from the grid.
        E r = get(loc);
        occupantArray[loc.getRow()][loc.getCol()] = null;
        return r;
    }
}
