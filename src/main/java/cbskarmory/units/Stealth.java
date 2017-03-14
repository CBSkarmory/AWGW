package cbskarmory.units;

import cbskarmory.Player;
import cbskarmory.terrain.Terrain;
import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;

/**
 * abstract Stealth represents stealth Air units
 * while abstract, may be in grid because I need something to switch the sprite to blank whitespace
 * when a stealth unit is hidden
 */
public abstract class Stealth extends Air{
	/**
	 * calls super(Player) from child classes
	 * don't invoke this
	 * @param owner
	 */
	public Stealth(Player owner) {
		super(owner);
	}
	private boolean hidden;
	/**
	 * Hides the stealth unit -- consumes extra daily fuel.
	 * hides sprite by replacing self in grid
	 * by version casted to Stealth
	 */
	public void hide(){
		
		this.hidden = true;
	}
	public void hideRender(){
		Grid<Actor> gr = getGrid();
		Terrain loc = (Terrain) getLocation();
		Player owner = getOwner();
		owner.getUnitsControlled().remove(this);
		this.removeSelfFromGrid();
		new HiddenUnit(owner, this).putSelfInGrid(gr, loc);		
	}


	/**
	 * reveals the hidden unit -- does opposite of hide()
	 */
	public void unHide(){
		
			this.hidden = false;
	}
	/**
	 * @return whether or not the unit is currently hidden
	 */
	public boolean isHidden(){
		return this.hidden;
	}
	/**
	 * @return extra amount of daily fuel the unit consumes when hidden
	 */
	public abstract int getExtraDailyCost();
	@Override
	public void deductDailyCost(){
		if(this.isHidden()){
			this.setFuel(getFuel()-getExtraDailyCost());
		}
		this.setFuel(getFuel()-this.getDailyCost());
		if(getFuel()<=0){
			setFuel(0);
		}
	}
	private Air hiddenUnit;
}
