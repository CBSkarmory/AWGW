package cbskarmory.units;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;

import cbskarmory.Player;
import cbskarmory.TerrainGrid;
import cbskarmory.CO.CO;
import cbskarmory.PassiveFlag.COFlag;
import cbskarmory.PassiveFlag.MoveType;
import cbskarmory.PassiveFlag.UnitType;
import cbskarmory.terrain.Terrain;
import cbskarmory.units.air.*;
import cbskarmory.weapons.Suit;
import cbskarmory.weapons.Weapon;
import cbskarmory.weapons.WeaponType;
import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;
import info.gridworld.gui.GridPanel;
import info.gridworld.gui.MenuMaker;
import info.gridworld.world.AVWorld;
import javafx.scene.layout.GridPane;

public abstract class Unit extends Actor{
	//FIXME kinda useless
	@Override public void act(){}

	//constructor
	/**
	 *Constructs a Unit owned by owner
	 *also loads the values for daily fuel cost and max mobility
	 */
	public Unit(Player owner){
		if(null!=owner){
			setOwner(owner);
			this.setDailyCost(loadDailyCost());
			this.setMaxMobility(loadMaxMobility());
			//this.canMove = true;
			this.setFuel(99);
			this.resetMovement();
		}else{
			//FOR FACTORY MENU, FIXME in Factory class NOT VERY EFFICIENT
			//			System.out.println("Warning: owner is null");
			//			throw new IllegalArgumentException("owner is null");

		}
	}
	//debug purposes ONLY
	public Unit(){}


	//ownership
	private Player owner;
	public Player getOwner(){
		return this.owner;
	}
	public void setOwner(Player newOwner){
		if(null!=newOwner){
			this.owner = newOwner;
			this.owner.getUnitsControlled().add(this);
			this.setColor(owner.getTeamColor());
		}else{
			throw new IllegalArgumentException("owner is null");
		}
	}

	//money
	public abstract int getBuildCost();

	//combat defense
	private Suit suit;
	public Suit getSuit(){
		return suit;
	}
	public void setSuit(Suit s){
		this.suit = s;
	}
	/**
	 * @return percent of damage taken generally as decimal
	 */
	public abstract double getBaseArmorResistance();
	private int health = 100;
	public int getHealth() {
		return health;
	}
	/**
	 * only use this for true dmg aka ICBM or internally
	 * @param hp to set to
	 */
	public void setHealth(int hp) {
		health = hp;
		if(health<=0){
			this.selfDestruct();
		}
	}
	public UnitType getUnitType(){
		ResourceBundle b = ResourceBundle.getBundle("resources/unit_to_unit_type");
		return UnitType.valueOf(b.getString(getType()));
	}
	public String getType() {
		return this.getClass().getSimpleName();
	}
	//TODO override this to set resistances
	public double resist(double damage, String source){
		return damage;
	}
	//taking damage and dying
	public void takeDamage(double damage, String source){
		if(suit!=null){
			damage = suit.resist(damage, source);
			damage = this.resist(damage, source);
		}
		this.health -= Math.round(damage);
		if (this.health<=0){
			this.health = 0;
			this.selfDestruct();
		}
	}
	protected void selfDestruct(boolean...showNoFuelIcon){
		//TODO animation

		URL fireIconLocation = this.getClass().getClassLoader().getResource("resources/32x/fire.png");
		URL noFuelIconLocation = this.getClass().getClassLoader().getResource("resources/32x/noFuel.png");
		Set<Terrain> where = new HashSet<Terrain>();
		where.add((Terrain) getLocation());
		try{
			TerrainGrid tg = (TerrainGrid)getGrid();
			GridPanel display = tg.hostWorld.getWorldFrame().control.display;
			AVWorld avw = tg.hostWorld;
			final Unit _this = this;
			new Thread(new Runnable(){

				@Override
				public void run() {
					try {
						if(showNoFuelIcon.length!=0&&showNoFuelIcon[0]){	
							for(int x=0;x<3;x++){
								display.showIconsOnSetOfLocations(new ImageIcon(noFuelIconLocation).getImage(), where);
								Thread.sleep(250);
							}
						}
						display.repaint();
						for(int x=0;x<3;x++){
							display.showIconsOnSetOfLocations(new ImageIcon(fireIconLocation).getImage(), where);
							Thread.sleep(250);

						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					finally {
						_this.removeSelfFromGrid();
						_this.getOwner().getUnitsControlled().remove(_this);	
						display.repaint();
					}

				}

			}).start();

		}catch(ClassCastException cce){
			System.err.println("?? Host grid is not TerrainGrid. wat. PROBABLY FATAL ERROR Moving on...");
			this.removeSelfFromGrid();
			this.getOwner().getUnitsControlled().remove(this);
			return;
		}
	}


	//combat offense
	protected void setWeapon(int index, String weaponType){
		this.weapons[index] = new Weapon(weaponType, this);
		if(index==0){
			this.setAmmo(weapons[0].getMaxAmmo());
		}
	}
	private Weapon[] weapons = new Weapon[2];
	public Weapon[] getWeapons(){
		return weapons;
	}
	private int ammo;
	public int getAmmo(){
		return ammo;
	}
	public void setAmmo(int x){
		if(x>=0){
			ammo = x;
		}else{
			throw new IllegalArgumentException("bad attempt to set ammo to negative");
		}
	}
	/**
	 * @return whether or not this unit can attack Unit u
	 * Gets overridden for ranged units, indirect fire, etc
	 */
	public boolean canTarget(Unit u){
		if((WeaponType.NONE.equals(getWeapons()[0].getWeaponType())&&null==getWeapons()[1])||(getAmmo()==0)){
			return false;
		}
		return couldTarget(u,(Terrain) getLocation());
	}

	/**
	 * @return whether or not this unit can attack Unit toCheck from Terrain hypothetical
	 * Gets overridden for ranged units, indirect fire, etc
	 */
	public boolean couldTarget(Unit toCheck, Terrain hypothetical){
		if(null==toCheck||toCheck instanceof HiddenUnit){
			return false; //can't target nothing or hidden things
		}
		return hypothetical.getDistanceTo((Terrain) toCheck.getLocation())==1;
	}

	/**
	 * @return whether or not this unit can counterattack Unit u
	 * Gets overridden for ranged units, indirect fire, etc
	 */
	public boolean canCounter(Unit u){
		return canTarget(u);
	}
	/**
	 *  * This Unit attacks the targeted Unit. If the target is still alive and 
	 * can counter this unit, as determined by canCounter(Unit), it counterattacks afterwards
	 * Precondition: ammo > 0 or has secondary weapon, target is in range 
	 * as determined by canTarget(Unit)
	 */
	public void fire(Unit target){
		immobilize();
		//fire first
		//stronger weapon
		double wep1,wep2,damage;
		wep1 = this.weapons[0].damageCalc(target);
		if (null!=this.weapons[1]){
			wep2 = this.weapons[1].damageCalc(target);
		}else{
			wep2 = 0;
		}
		boolean usedPrimary = (wep1 >= wep2);
		if (usedPrimary){
			this.setAmmo(getAmmo()-1);
			damage = wep1;
		}else{
			damage = wep2;
		}
		//luck
		double luck = (double)Weapon.luck();
		luck = this.owner.CO.passive(luck, COFlag.LUCK, getUnitType());
		damage += luck;
		String source;
		if (usedPrimary){
			source = this.weapons[0].getWeaponType();
		}else{
			source = this.weapons[1].getWeaponType();
		}
		damage = this.owner.CO.passive(damage, COFlag.ATTACK, this.getUnitType());
		target.takeDamage(damage, source);
		//attacking uncloaks
		if (this instanceof Stealth){
			Stealth me = (Stealth)(this);
			if(me.isHidden()){
				me.unHide();
			}
		}else if (this instanceof Stealth2){
			Stealth2 me = (Stealth2)(this);
			if(me.isHidden()){
				me.unHide();
			}
		}

		//counter attack if target is alive and direct fire
		if (target.getHealth()>0 && target.canCounter(this)){
			wep1 = target.weapons[0].damageCalc(this);
			if (null!=target.weapons[1]){
				wep2 = target.weapons[1].damageCalc(this);
			}else{
				wep2 = 0;
			}
			usedPrimary = wep1 >= wep2;
			if (usedPrimary){
				target.ammo--;
				damage = wep1;
			}else{
				damage = wep2;
			}
			//luck
			luck = Math.random()*10+0.1;
			//CO power affects luck
			luck = target.owner.CO.passive(luck, COFlag.LUCK, target.getUnitType());
			damage += luck;
			if (usedPrimary){
				source = target.weapons[0].getWeaponType();
			}else{
				source = target.weapons[1].getWeaponType();
			}
			damage = target.owner.CO.passive(damage, COFlag.COUNTER, this.getUnitType());
			this.takeDamage(damage, source);
		}
	}
	//movement
	/**
	 * Not a lazy boolean flag, as ability to move involves a myriad of
	 * factors including  {@link fuel}, participation in
	 * combat, transport, and other special circumstances like {@link CO} powers and electronic 
	 * warfare (EW)
	 */
	protected boolean canMove;
	public boolean hasMoved;
	private double fuel;
	private int dailyCost;
	public void deductDailyCost(){
		this.setFuel(getFuel()-getDailyCost());
	}
	/**
	 *As you may have noticed, this has void return type.
	 *This method is usually invoked upon fuel reaching or dropping below 0,
	 *by the <i>{@link setFuel()}</i> method
	 * This is public because it can also be invoked by other special 
	 * circumstances like {@link CO} powers and electronic warfare (EW)
	 */
	public abstract void outOfFuel();
	private double mobility;
	public void setMobility(double x){
		if(x>=0){
			this.mobility = x;
		}else{
			throw new IllegalArgumentException("bad attempt to set negative mobility");
		}
	}
	/**
	 * @return Current {@link mobility} of the {@link Unit}
	 */
	public double getMobility(){
//		System.out.println("line 346 Unit: mobility="+this.mobility+" canMove = "+canMove);
		return this.mobility;
	}

	private double maxMobility;
	/**
	 * @return Maximum {@link mobility} of the {@link Unit}
	 * {@link mobility} is set to this value at the start of each turn 
	 * by <i>{@link resetMovement()}</i>  or by special circumstances like {@link CO} powers
	 */
	public double getMaxMobility(){
		return this.maxMobility;
	}

	/**
	 *sets the maximum mobility of the unit to maxMobility
	 *should invoked from the constructor
	 * Precondition: maxMobility is a positive integer
	 * Postcondition: this.maxMobility is set to maxMobility
	 */
	private void setMaxMobility(double maxMobility){
		if(maxMobility>=0){
			this.maxMobility = maxMobility;
		}else{
			throw new IllegalArgumentException("bad attempt to max mobility (0 or negative)");
		}
	}
	/**
	 * loads the maximum mobility for this unit from a resourcebundle
	 * for use in constructor for setMaxMobility()
	 * @return maximum mobility value for the unit
	 */
	private double loadMaxMobility(){
		ResourceBundle b = ResourceBundle.getBundle("resources/unit_move");
		return (double)((int)(Double.parseDouble(b.getString(getType()))+0.5));
	}

	/**
	 * loads the daily fuel cost for this unit from a resourcebundle
	 * for use in constructor for setDailyCost()
	 * @return maximum mobility value for the unit
	 */
	private int loadDailyCost(){
		if(this instanceof HiddenUnit){
			HiddenUnit me = (HiddenUnit) this;
			Unit cont = (me).getContainedUnit();
			return (cont.loadDailyCost());
		}
		ResourceBundle b = ResourceBundle.getBundle("resources/unit_daily_fuel");
		try {
			double ans = Double.parseDouble(b.getString(getType()));
			return (int)(this.owner.CO.passive(ans, COFlag.DAILY_COST, getUnitType()));
		} catch (NumberFormatException e) {
			System.out.println(e.getStackTrace());
			System.out.println("Method: Unit.loadDailyCost()");
			throw new RuntimeException("Corrupt File");
		}
	}

	/**
	 * @return the daily fuel cost of the unit
	 * override this method to "set" a different daily fuel cost of the unit
	 * should be 0 for land units, different for air and sea units
	 */
	public int getDailyCost(){
		return dailyCost;
	}

	/**
	 *sets the daily fuel cost of the unit to cost
	 *should invoked from the constructor
	 * Precondition: fuelCost is a positive integer
	 * Postcondition: this.dailyCost is set to fuelCost
	 */
	private void setDailyCost(int x){
		this.dailyCost = x;
	}
	/**
	 * @return The {@link MoveType} of this {@link Unit}
	 */
	public abstract MoveType getMovementType();
	/**
	 * @return Amount of fuel remaining. Fuel is required to move, 
	 * and is consumed when moving. {@link fuel} is truncated to integer at the start of each turn by
	 * the resetMovement() method.
	 */
	public double getFuel(){
		return this.fuel;
	}
	/**
	 * @return Amount of fuel remaining. Mobility is required to move, and is consumed when moving.  
	 *  maxMobility fuel is converted into mobility at the start of each turn by resetMovement().
	 * fuel is truncated to integer at the start of each turn by the resetMovement() method.
	 */
	public void setFuel(double d){
		this.fuel = d;
		if(fuel<0){
			outOfFuel();
		}
	}
	/**
	 * @return Whether or not this unit can still move. A {@link Unit} can move 
	 * until they runs out of {@link mobility}, participates in combat, 
	 * or is dropped off from a carrying unit. A {@link Unit} currently carried cannot move, 
	 * but will move with the carrying unit.
	 */
	public boolean canMove(){
		if(this.getMobility()==0){
			immobilize();
			return false;
		}
		return this.canMove;
	}

	/**
	 * sets canMove to false, changes colors
	 * @return whether or not the unit was already UNABLE to move
	 */
	public boolean immobilize(){
		if(!canMove){
			return true;
		}else{
			canMove = false;
			if(getColor()!=null){
				setColor(getColor().darker());
			}
			return false;
		}
	}

	/**
	 * Resets movement-related variables for new day or {@link CO} power, namely:
	 * <p>{@link canMove}<p>{@link fuel}
	 *  Postcondition: The daily fuel cost of the unit is deducted from fuel.
	 * up to maxMobility is deducted from fuel and the same amount is 
	 *   added to mobility. 
	 */
	public void resetMovement(){
		//this.hasMoved = false;
		if(!canMove&&getColor()!=null){
			setColor(getColor().brighter());
		}
		this.canMove = true;
		this.fuel = (int)(getFuel());
		if(null!=getLocation()){//If not carried
			this.setFuel(this.getFuel()-(this.maxMobility-mobility));
			this.deductDailyCost();
		}if(getFuel()>0){
			this.mobility = this.maxMobility;
		}
	}
	/**
	 * Precondition: the unit has enough mobility to make it to the Terrain toMoveto
	 * Postcondition: the unit moves to the Terrain toMoveTo, traversing each terrain in a good path
	 * @throws Exception 
	 */
	public void move(Terrain toMoveTo,boolean...realMove) throws Exception{
		Terrain t = (Terrain) getLocation();
		boolean moveSuccess = realMove.length==0?
				move(findPathTo(toMoveTo)):move(findPathTo(toMoveTo),realMove[0]);
				if(moveSuccess){
					immobilize();
					t = (Terrain) getLocation();
					for(Terrain checkingForHidden: t.getAllAdjacentTerrains()){
						Actor occ = getGrid().get(checkingForHidden);
						if(occ instanceof HiddenUnit&&((HiddenUnit) occ).getContainedUnit().getOwner()!=this.getOwner()){
							HiddenUnit hu = (HiddenUnit) occ;
							Unit aha = hu.unBox();
							aha.immobilize();
							if(aha instanceof Stealth){
								((Stealth) aha).unHide();
							}else if(aha instanceof Stealth2){
								((Stealth2) aha).unHide();
							}
						}
					}
				}else{
					System.out.println("move failed, see line 441 of Unit");
				}
	}

	/**
	 * uses DFS
	 * @return a set of Terrains that the unit can move to, given constraints of the surrounding
	 * terrain, units, and current mobility level
	 */
	public Set<Terrain> getValidMoveSpaces(){
		//FIXME pretty sure this is very inefficient, uncomment sysouts to see
		Set<Terrain> ans = new HashSet<Terrain>();
		ans.add((Terrain) this.getLocation());
		if(this.canMove()==false){
			return ans;
		}
		Stack<Terrain> toCheck = new Stack<Terrain>();
		toCheck.push((Terrain) this.getLocation());
		Map<Terrain, Double> distances = new HashMap<>();
		distances.put((Terrain) this.getLocation(), 0.0);
		while(0!=toCheck.size()){
			//System.out.println("stack size is "+toCheck.size());
			double distTo;
			Terrain current = toCheck.pop();
			ArrayList<Terrain> adjacent = current.getAllAdjacentTerrains();
			for(Terrain t:adjacent){
				//				System.out.println("movement type: "+this.getMovementType());
				if((distTo = distances.get(current)+t.getMoveCost(this.getMovementType()))<=mobility){
					if(null==getGrid().get(t)){
						if(!distances.containsKey(t)||distTo<distances.get(t)){
							//							System.out.println("mobility is "+mobility+", "
							//									+ "greater or euqal vs total movement cost of "+distTo+" for "+t);
							distances.put(t,distTo);
							toCheck.push(t);
						}
						ans.add(t);
					}else{
						Unit u = (Unit) getGrid().get(t);
						if(u.getOwner()==this.getOwner()){
							if(distances.containsKey(t)){
								if(distTo<distances.get(t)){
									distances.put(t,distTo);
									toCheck.push(t);
								}
							}else{
								//								System.out.println("mobility is "+mobility+", "
								//										+ "greater or euqal vs total movement cost of "+distTo+" for "+t);
								distances.put(t,distTo);
								toCheck.push(t);
							}
							if(u instanceof Carry){
								Carry c = (Carry) u;
								if(c.canCarry(this)){
									ans.add(t);
								}
							}
						}
					}
				}
				else{
					//					System.out.println("mobility is "+mobility+",  "
					//							+ "not enough vs total movement cost of "+distTo+" for "+t);
				}
			}
		}
		//		System.out.println("\ndone\n");
		return ans;

	}
	
	private int totalCost(Queue<Terrain> path){
		if(null==path||path.isEmpty()){
			return 0;
		}else{
			int ans=0;
			for(Terrain t: path){
				ans+=t.getMoveCost(getMovementType());
			}return ans;
		}
	}

//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	private Queue<Terrain> findpathTo_2(Terrain target) throws IllegalArgumentException{
//		if(null==target){
//			throw new IllegalArgumentException("target is null");
//		}
//		Map<Terrain, Integer> shortDistances = new HashMap<>();
//		Map<Terrain, Terrain> previousLink = new HashMap<>();
//		Terrain current = (Terrain) getLocation();
//		Comparator<Terrain> comp = new Comparator<Terrain>() {
//			/**
//			 * Approximates which has shorter path to get to
//			 */
//			@Override
//			public int compare(Terrain arg0, Terrain arg1) {
//				int x;
//				//heuristic will never overestimate total cost
//				Integer c0 = ((-1==(x = calculatePathCost(arg0))?Integer.MAX_VALUE:x))+arg0.getDistanceTo(target);
//				Integer c1 = ((-1==(x = calculatePathCost(arg1))?Integer.MAX_VALUE:x))+arg1.getDistanceTo(target);
//				return c0.compareTo(c1);
//			}
//			private int calculatePathCost(Terrain t){
//				if(!previousLink.containsKey(t)){
//					return -1;
//				}
//				Terrain parent = previousLink.get(t);
//				if(null==parent){
//					return 0;
//				}else{
//					return (int) (calculatePathCost(previousLink.get(parent))+parent.getMoveCost(getMovementType()));
//				}
//			}
//			
//		};
//		PriorityQueue<Terrain> toCheck = new PriorityQueue<>(comp);
//		//add all tiles in range
//		toCheck.addAll(getValidMoveSpaces());
//		
//		
//		
//		
//	}
//	private static Queue<Terrain> reverse(Queue<Terrain> q){
//		Stack<Terrain> stack = new Stack<>();
//		Queue<Terrain> ans = new LinkedList<>();
//		while (!q.isEmpty()){
//			stack.push(q.poll());
//		}
//		while(!stack.isEmpty()){
//			ans.add(stack.pop());
//		}
//		return ans;
//	}
	private static Queue<Terrain> genPath(Terrain t,Map<Terrain,Terrain> parentMap)throws IllegalArgumentException{
		if(!parentMap.containsKey(t)){
			throw new IllegalArgumentException();
		}
		Queue<Terrain> ans = new LinkedList<>();
		Terrain parent = parentMap.get(t);
		if(parent==null){
			//we started here, no need to move
			return ans;
		}else{
			ans = genPath(parent, parentMap);
			ans.add(t);
			return ans;
		}
	}
	private int totalCost(Terrain t,Map<Terrain,Terrain> previousLink){
		if(!previousLink.containsKey(t)){
			throw new IllegalStateException("terrain not mapped to parent — how did we get here?");
		}
		Terrain parent = previousLink.get(t);
		if(null==parent){
//			return (int) t.getMoveCost(getMovementType());
			//we started here, 0
			return 0;
		}else{
			return (int) (t.getMoveCost(getMovementType())+totalCost(parent,previousLink));
		}
	}
	
	/**
	 * Precondition: the unit has enough mobility to reach the Terrain that the path is being found to.
	 * The returned path will be optimal, uses A*
	 * @return a queue of Terrains that could be traversed to reach the destination
	 * @throws Exception 
	 */
	private Queue<Terrain> findPathTo(Terrain target) throws Exception{
		Queue<Terrain> ans = new LinkedList<>();

		if(null==target){
			return new LinkedList<Terrain>();
		}


		if(target==getLocation()){
			//you are already here
			return ans;
		}
//		Map<Terrain, Queue<Terrain>> savedPaths = new HashMap<>();
		Map<Terrain, Terrain> previousLink = new HashMap<>();
		previousLink.put((Terrain) getLocation(),null);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Comparator<Terrain> comp = new Comparator(){
			@Override
			/**
			 * Approximates which path is better
			 */
			public int compare(Object o1, Object o2) {
				Terrain t1 = (Terrain) o1;
				Terrain t2 = (Terrain) o2;
//				Integer c1 = totalCost(savedPaths.get(t1))+t1.getDistanceTo(target);
//				Integer c2 = totalCost(savedPaths.get(t2))+t2.getDistanceTo(target);
				Integer c1 = totalCost(t1,previousLink)+t1.getDistanceTo(target);
				Integer c2 = totalCost(t2,previousLink)+t2.getDistanceTo(target);
				return c1.compareTo(c2);
			}
			
		};
		PriorityQueue<Terrain> toCheck= new PriorityQueue<Terrain>(comp);
		toCheck.add((Terrain) getLocation());
		while(!toCheck.isEmpty()){
			Terrain current = toCheck.poll();
			for(Terrain t : current.getAllAdjacentTerrains()){
				if(999!=t.getMoveCost(getMovementType())&&
						(null==getGrid().get(t)||((Unit)(getGrid().get(t))).getOwner()==this.getOwner())){
					
					boolean updated = false;
					if(!previousLink.containsKey(t)){
						previousLink.put(t, current);
						updated = true;
					}
					
//					Queue<Terrain> path = new LinkedList<Terrain>();
//					if(savedPaths.containsKey(current)){
//						path.addAll(savedPaths.get(current));
//					}
//					path.add(t);
					if(t==target){
//						return path;
						return genPath(t,previousLink);
					}
					
//					if(totalCost(path)<(savedPaths.containsKey(t)?totalCost(savedPaths.get(t)):998)){
					if(t.getMoveCost(getMovementType())+totalCost(current,previousLink)<totalCost(t,previousLink)){
//						savedPaths.put(t, path);
						previousLink.put(t, current);
						updated = true;
					}
					if(totalCost(t,previousLink)<=getMobility()&&updated){
						if(!toCheck.contains(t)){
							toCheck.add(t);
						}

					}
				}
			}
		}
		//if you got here, then there is no path
		//See Precondition: there is a path, checked by getValidMoveSpaces
		throw new Exception("no path, precondition not met");

	}

	/**
	 * Should only be invoked by {@code move(Queue<Terrain> path)} method
	 *  Postcondition: this.getLocation returns t
	 */
	private void traverse(Grid<Actor> gr, Terrain t){
		//		removeSelfFromGrid();
		//		putSelfInGrid(gr, t);
		double moveCost = t.getMoveCost(this.getMovementType());
		setMobility(getMobility()-moveCost);
	}
	/**
	 * Moves the {@link Unit} along a path of {@link Terrain}s.
	 * @return Whether or not {@link Unit} was successful in moving all the way along the path.
	 * For now, it should always be successful because fog of war/land mines etc are not implemented yet
	 * @param path A {@link Queue} of adjacent {@link Terrain}s
	 */
	public boolean move(Queue<Terrain> path, boolean...realMove){
		if(0==path.size()){
			//already done
			return true;
		}
		Terrain t = path.poll();
		Grid<Actor> gr = this.getGrid();
		//reached end of path, success
		if(t == null){
			return true;
		}
		//invalid Terrain, fail -- should never happen
		if(!(gr.isValid(t))){
			System.err.println("invalid terrain, not sure what happened here");
			return false;
		}
		//not enough mobility -- should never happen
		if(t.getMoveCost((this.getMovementType()))>this.getMobility()){
			System.err.println("not enough mobi -- check BFS algorithm???");
			return false;
		}
		//Terrain is occupied by something other than allied Unit, fail
		try{
			Unit u = (Unit)(gr.get(t));
			if(null!=u&&u.getOwner()!=this.getOwner()){
				System.err.println("Terrain is occupied by something other than allied Unit, fail");
				return false;
			}
		}catch(ClassCastException actorIsNotAUnit){
			System.out.println(actorIsNotAUnit.getStackTrace());
			System.err.println("why do you have non-units in the grid");
			return false;
		}
		//move on
		traverse(getGrid(),t);
		if(path.peek()==null&&realMove.length!=0){
			teleport(t);
		}
		return realMove.length==0?move(path):move(path,true);
	}
	private void teleport(Terrain t){
		Grid gr = getGrid();
		this.removeSelfFromGrid();
		this.putSelfInGrid(gr, t);
	}


	/**
	 * Precondition: Carry c canCarry(this) is true
	 * @param c the Carry that this Unit will be loaded into
	 * @throws Exception 
	 */
	public void load(Carry c) throws Exception{
		if(c.canCarry(this)){
			c.addUnit(this);
		}else{
			throw new Exception("cannot be carried, precondition not met");
		}
		immobilize();
	}
	public String getWeaponInfo(){
		if(this instanceof HiddenUnit){return null;}
		if(WeaponType.NONE.equals(getWeapons()[0].getWeaponType())){
			if(getWeapons()[1]==null){
				return "Unarmed";
			}else{
				return "∞ ammo: " + getWeapons()[1].getWeaponType();
			}
		}else{
			return getAmmo()+" ammo: "+getWeapons()[0].getWeaponType()+
					((null!=getWeapons()[1])?", ∞ ammo: "+ getWeapons()[1].getWeaponType():"");
		}
	}
	public String getInfo(){
		return getType() + ((null!=getLocation()&&getLocation() instanceof Terrain)?" at " + ((Terrain) getLocation()).getUIName() + getLocation():"")
				+" ["+ getHealth() + " HP, "+((int)getFuel())+" fuel, "+getWeaponInfo()+ " ]"+
				(((this instanceof Stealth&&((Stealth) this).isHidden())||(this instanceof Stealth2&&((Stealth2) this).isHidden()))?
						"[hidden]":"");
	}
	public String getConciseInfo(){
		return getType()+" ["+ getHealth() + " HP]";
	}

	/**
	 * @return whether or not this unit is a jet aircraft
	 */
	public boolean isJet(){
		return(this instanceof Fighter||this instanceof Bomber||this instanceof StealthBomber||
				this instanceof AdvFighter||this instanceof JSF||this instanceof CAS||this instanceof DropShip);
	}
}
