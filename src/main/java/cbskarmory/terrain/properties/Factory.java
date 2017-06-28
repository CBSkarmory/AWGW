package cbskarmory.terrain.properties;

import info.gridworld.actor.Actor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JOptionPane;

import cbskarmory.Player;
import cbskarmory.TerrainGrid;
import cbskarmory.PassiveFlag.UnitType;
import cbskarmory.units.Unit;

/**
 * A Factory is a capturable Terrain -- a Property
 * Factories build Units. Different kinds build different types of Units. 
 */
public abstract class Factory extends Property {

	/**
	  * Constructs a Factory with given row and column coordinates.
	  * @param r the row
	  * @param c the column
	  */
	public Factory(int r, int c, TerrainGrid<Actor> hostGrid,Player owner) {
		super(r, c, hostGrid,owner);
	}
	protected ArrayList<Constructor<? extends Unit>> buildableUnits;
	/**
	  * @return a set of constructors for units that 
	  * can be built on this factory
	  */
	public ArrayList<Constructor<? extends Unit>> getBuildableUnits() {
		return this.buildableUnits;
	}
	public void buildUnit(Constructor<? extends Unit> constructor){
		try {
			Unit u = constructor.newInstance(getOwner());
			if(u.getBuildCost()>getOwner().getMoney()){
				System.out.println("costs "+u.getBuildCost());
				System.out.println("when money = "+getOwner().getMoney());
				JOptionPane.showMessageDialog(hostGrid.hostWorld.getWorldFrame(), "not enough money");
				return;
			}else{
				getOwner().setMoney(getOwner().getMoney()-u.getBuildCost());
				u.putSelfInGrid(hostGrid, this);
			}
			
			
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
