/* 
 * AP(r) Computer Science GridWorld Case Study:
 * Copyright(c) 2005-2006 Cay S. Horstmann (http://horstmann.com)
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
 * @author Cay Horstmann
 */

package info.gridworld.gui;

import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import info.gridworld.world.AVWorld;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.beans.PropertyEditorSupport;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import cbskarmory.Player;
import cbskarmory.Runner;
import cbskarmory.TerrainGrid;
import cbskarmory.terrain.Terrain;
import cbskarmory.terrain.properties.Factory;
import cbskarmory.terrain.properties.Property;
import cbskarmory.units.Carry;
import cbskarmory.units.HiddenUnit;
import cbskarmory.units.Stealth;
import cbskarmory.units.Stealth2;
import cbskarmory.units.Unit;
import cbskarmory.units.land.Infantry;
import cbskarmory.weapons.WeaponType;

/**
 * Makes the menus for constructing new occupants and grids, and for invoking
 * methods on existing occupants. <br />
 * This code is not tested on the AP CS A and AB exams. It contains GUI
 * implementation details that are not intended to be understood by AP CS
 * students.
 */
public class MenuMaker<T> {

	/**
	 * hacky display fixer lol
	 */
	public static void noBugsPls(GridPanel display, TerrainGrid gr){
		//cheaty bugfix hopefully
		TerrainGrid tg = (TerrainGrid) gr;
		if(gr!=null){
			Location clickMeForBugFree = tg.getLocationArray()[0][0];
			display.avw.locationClicked(clickMeForBugFree);
		}
		display.avw.getWorldFrame().repaint();
	}
	/**
	 * This will be the common repaint.
	 * @param display
	 */
	public static void tryRepaint(GridPanel display) {
		//display.revalidate();
		Container c = display.getParent();
		display.revalidate();
		display.paintImmediately(display.getBounds());
		//display.avw.getWorldFrame().repaint();
		//c.repaint();
		//display.repaint();

	}

	/**
	 * Constructs a menu maker for a given world.
	 * 
	 * @param parent
	 *            the frame in which the world is displayed
	 * @param resources
	 *            the resource bundle
	 * @param displayMap
	 *            the display map
	 */
	public MenuMaker(WorldFrame<T> parent, ResourceBundle resources,
			DisplayMap displayMap) {
		this.parent = parent;
		this.resources = resources;
		this.displayMap = displayMap;
	}

	public GridPanel display;

	public Terrain newLoc;

	/**
	 * Makes a menu that displays all available actions that a Unit may perform
	 * when moving to this tile
	 * 
	 * @param occupant
	 *            the unit whose available actions should be displayed
	 * @param loc
	 *            the location that the Unit would move to
	 * @return the menu to pop up
	 */
	public JPopupMenu makeMoveMenu(T occupant, Location loc) {
		this.occupant = occupant;
		// new ver
		Unit u = (Unit) occupant;

		display.currentLocation = loc;
		currentLocation = loc;
		// TODO tidy up
		//TODO put in another method
		StringBuilder dispMessage = new StringBuilder(50);
		dispMessage.append(u.getInfo());
		if (u instanceof Carry) {
			Carry c = (Carry) u;
			dispMessage.append("\nCurrently Carrying("+c.getUnits().size()+"/"+c.getMaxCapacity()+"): ");
			ArrayList<String> types = new ArrayList<>();
			for (Unit carried : c.getUnits()) {
				types.add(carried.getConciseInfo());
			}
			if (!types.isEmpty()) {
				dispMessage.append(types.toString());
			} else {
				dispMessage.append("(empty)");
			}
		} else {
			dispMessage.append("\n");
		}
		dispMessage
		.append(u.canMove() ? "\nClick highlighted tile to move, click unhighlighted to cancel"
				: "\nUnit currently immobile; wait until next turn to move again.");
		if(u instanceof HiddenUnit){
			dispMessage = new StringBuilder("Currently selected: none.\n\nUse your units to move. Click your factories to build. "+
					//					 "DO NOT use arrow keys or Enter"+
					"P1 money: "+Runner.players[1].getMoney()+"  P2 money: "+Runner.players[2].getMoney());
		}
		display.avw.setMessage(dispMessage.toString());
		display.repaint();
		// inefficient
		//if(u.getHealth()>0){ FIXME
		Set<Terrain> validMoveSpaces = u.getValidMoveSpaces();
		display.shouldBeHighlighted = validMoveSpaces;
		display.avw.resetClickedLocation();
		//}
		//TODO thread stuff
		//		new Thread(new Runnable() {
		//			@Override
		//			public void run() {
		newLoc = (Terrain) display.avw.getLocationWhenClicked();
		//			}
		//		}).start();
		if (!validMoveSpaces.contains(newLoc)) {
			display.avw.setMessage("Currently selected: none.\n\nUse your units to move. Click your factories to build. "+
					//					 "DO NOT use arrow keys or Enter"+
					"P1 money: "+Runner.players[1].getMoney()+"  P2 money: "+Runner.players[2].getMoney());
			display.shouldBeHighlighted.clear();
			return null;
		}
		System.out.println("line 133 mm" + newLoc);
		try {
			// u.move(newLoc);
			// display.shouldBeHighlighted.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("line 209 Menu: currentLocation set to loc\nloc is "
				+ newLoc.getClass().getName() + "at " + newLoc);
		System.out.println("line 211 Menu: old loc set to loc\nloc is "
				+ currentLocation.getClass().getName() + "at "
				+ currentLocation);
		JPopupMenu menu = new JPopupMenu();
		ArrayList<JMenuItem> actions = null;
		try {
			actions = getValidActions(newLoc);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		System.out.println("methods is " + actions.size() + " size.");
		for (int i = 0; i < actions.size(); i++) {
			menu.add(actions.get(i));
//			System.out.println("added method, see line 226 of MenuMaker");
		}
		return menu;
	}
	public static ImageIcon get16xIcon(URL imagePath){
		try {
			ImageIcon pic = new ImageIcon(imagePath); // load the image to a imageIcon
			Image image = pic.getImage(); // transform it 
			Image newimg = image.getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			pic = new ImageIcon(newimg);  // transform it back
			return pic;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}
	private ArrayList<JMenuItem> getValidActions(Location newLoc)
			throws NoSuchMethodException, SecurityException {
		Unit u = (Unit) occupant;
		ArrayList<JMenuItem> ans = new ArrayList<JMenuItem>();
		if(u instanceof HiddenUnit){
			return ans;
		}
		// every unit has the wait option if not loading into carry
		Unit newLocOcc = (Unit) u.getGrid().get(newLoc);
		if (u.canMove() && (null == newLocOcc||u==newLocOcc)) {
			if(!(u==newLocOcc)){//if moving somewhere
				JMenuItem waitOption = new JMenuItem();
				Action a = new Action() {
					public boolean enabled = true;

					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							u.move(((Terrain) newLoc),true);
						} catch (Exception e) {
							e.printStackTrace();
						}
						display.shouldBeHighlighted.clear();
						display.repaint();

					}

					@Override
					public void addPropertyChangeListener(
							PropertyChangeListener arg0) {
					}

					@Override
					public Object getValue(String arg0) {
						return null; // idk
					}

					@Override
					public boolean isEnabled() {
						return enabled;
					}

					@Override
					public void putValue(String key, Object value) {
					}

					@Override
					public void removePropertyChangeListener(
							PropertyChangeListener listener) {
					}

					@Override
					public void setEnabled(boolean b) {
						enabled = b;
					}
				};
				a.setEnabled(true);
				waitOption.setAction(a);
				URL imagePath = this.getClass().getClassLoader().getResource("resources/32x/wait.png");
				waitOption.setIcon(get16xIcon(imagePath));
				waitOption.setText("Wait");
				ans.add(waitOption);
			}

			// if unit is carry, can drop something off
			System.out.println("checking drop");
			ArrayList<Unit> carriedUnits = new ArrayList<>();
			Carry carryU;
			if (u instanceof Carry
					&& (!((carriedUnits = (carryU = (Carry) u).getUnits())
							.isEmpty()))) {
				for (Unit carried : carriedUnits) {
					ArrayList<Terrain> validLZs = new ArrayList<>();
					for (Terrain t : ((Terrain) newLoc)
							.getAllAdjacentTerrains()) {
						if ((null == u.getGrid().get(t)||(u==u.getGrid().get(t)/*&&!newLoc.equals(t)*/))
								&& 999 != t.getMoveCost(carried.getMovementType())
								&& 999 != ((Terrain) newLoc).getMoveCost(carried.getMovementType())) {
							validLZs.add(t);
						}
					}
					if(!validLZs.isEmpty()){
						JMenuItem tmpDropOption = new JMenuItem();
						Action tmpDropAction = new Action() {
							public boolean enabled = true;

							@Override
							public void actionPerformed(ActionEvent arg0) {

								display.shouldBeHighlighted = new HashSet<Terrain>(
										validLZs);
								display.avw.setMessage("Dropping off: "+carried.getConciseInfo()
								+"\n\nClick where you would like to drop "+carried.getType()+" off."
								+ "  Click an unhighlighted tile to cancel.");
								display.avw.resetClickedLocation();
								display.repaint();
								display.invalidate();
								new Thread(new Runnable() {

									@Override
									public void run() {
										Location LZ = display.avw
												.getLocationWhenClicked();
										if (!validLZs.contains(LZ)) {
											display.shouldBeHighlighted.clear();
											display.repaint();
											display.invalidate();
											// drop canceled
											return;
										} else {
											try {
												u.move(((Terrain) newLoc),true);
												display.repaint();
												display.invalidate();
											} catch (Exception e) {
												e.printStackTrace();
											}
											carried.putSelfInGrid(u.getGrid(), LZ);
											carried.immobilize();
											carryU.getUnits().remove(carried);
											display.shouldBeHighlighted.clear();
											display.repaint();

										}
									}
								}).start();

							}

							public void addPropertyChangeListener(
									PropertyChangeListener arg0) {
							}

							public Object getValue(String arg0) {
								return null;
							}

							public boolean isEnabled() {
								return enabled;
							}

							public void putValue(String key, Object value) {
							}

							public void removePropertyChangeListener(
									PropertyChangeListener listener) {
							}

							public void setEnabled(boolean b) {
								enabled = b;
							}
						};
						tmpDropOption.setAction(tmpDropAction);
						tmpDropOption.setText("Drop: " + carried.getType());
						tmpDropOption.setIcon(get16xIcon(this.getClass().getClassLoader().getResource(
								"resources/units/"+carried.getType()+".png")));
						ans.add(tmpDropOption);
					}
				}
			}

			// units can fire on enemies if not unarmed and in range and unit can move
			System.out.println("checking weps");
			if (u.canMove()&&u.getAmmo()>0&&u.getWeapons()[0].getWeaponType() != WeaponType.NONE
					|| null != u.getWeapons()[1]) {
				Set<Unit> targetable = new HashSet<>();
				ArrayList<Location> occupied = u.getGrid().getOccupiedLocations();
				for (Location l : occupied) {
					Unit tmp = (Unit) u.getGrid().get(l);
					if (u.couldTarget(tmp, (Terrain) newLoc)
							&& (!u.getOwner().equals(tmp.getOwner()))) {
						targetable.add(tmp);
					}
				}
				if (!targetable.isEmpty()) {

					Action fireAction = new Action() {
						public boolean enabled = true;

						@Override
						public void actionPerformed(ActionEvent e) {
							display.shouldBeHighlighted.clear();
							for(Unit targetableUnit: targetable){
								Terrain targetableTerrain = (Terrain) targetableUnit.getLocation();
								display.shouldBeHighlighted.add(targetableTerrain);
							}
							display.avw.setMessage("Click highlighted tile to attack.\n\nClick unhighlighted to cancel");
							display.repaint();
							new Thread(new Runnable() {

								@Override
								public void run() {
									Location targetLocation = display.avw.getLocationWhenClicked();
									noBugsPls(display, (TerrainGrid) u.getGrid());
									Unit targetedUnit = (Unit) u.getGrid().get(targetLocation);
									if (!targetable.contains(targetedUnit)) {
										display.shouldBeHighlighted.clear();
										tryRepaint(display);
										System.err.println("attack canceled");
										// attack canceled
										return;
									} else {
										try {
											u.move(MenuMaker.this.newLoc, true);
										} catch (Exception e1) {
											e1.printStackTrace();
										}
										u.fire(targetedUnit);
										display.paintImmediately(display.getBounds());
										URL fireIconLocation = this.getClass().getClassLoader().getResource(
												"resources/32x/fire.png");
										Set<Terrain> where = new HashSet<Terrain>();
										where.add((Terrain) targetLocation);
										if(targetedUnit.getHealth()>0&&u.getHealth()>0&&targetedUnit.canCounter(u)){
											where.add((Terrain) u.getLocation());
										}
										for (int i = 0; i < 2; i++) {
											display.showIconsOnSetOfLocations(new ImageIcon(fireIconLocation).getImage(), where);
											try {
												Thread.sleep(500); 
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
											tryRepaint(display);
										}
										noBugsPls(display, (TerrainGrid) u.getGrid());
										//display.avw.resetClickedLocation();
										//display.paintImmediately(display.getBounds());


									}
								}

							}).start();
						}

						@Override
						public void addPropertyChangeListener(
								PropertyChangeListener listener) {
						}

						@Override
						public Object getValue(String key) {
							return null;
						}

						@Override
						public boolean isEnabled() {
							return enabled;
						}

						@Override
						public void putValue(String key, Object value) {
							// TODO Auto-generated method stub

						}

						@Override
						public void removePropertyChangeListener(
								PropertyChangeListener listener) {
							// TODO Auto-generated method stub

						}

						@Override
						public void setEnabled(boolean b) {
							enabled = b;
						}
					};
					JMenuItem fireOption = new JMenuItem();
					fireOption.setAction(fireAction);
					fireOption.setText("Fire");
					fireOption.setIcon(get16xIcon(this.getClass().getClassLoader().getResource(
							"resources/32x/fire.png")));
					ans.add(fireOption);


					System.out.println("added fireOption: MM " + "line "
							+ new Throwable().getStackTrace()[0].getLineNumber());
					System.out.println("targetable = " + targetable);
				}
			}

			//check resupply
			System.out.println("checking resupply");
			if(u.canMove()&&u instanceof Carry && ((Carry) u).canResupply()){
				Terrain newTerrain = (Terrain) newLoc;
				ArrayList<Unit> adjacentAlliedUnits = new ArrayList<>();
				for(Terrain t:newTerrain.getAllAdjacentTerrains()){
					Unit tOcc = (Unit) u.getGrid().get(t);
					if(tOcc!=null&&tOcc!=u&&tOcc.getOwner()==u.getOwner()){
						adjacentAlliedUnits.add(tOcc);
					}
				}
				if(!adjacentAlliedUnits.isEmpty()){
					JMenuItem resupplyOption = new JMenuItem();
					URL supplyPicLocation = this.getClass().getClassLoader().getResource(
							"resources/32x/supply.png");
					ImageIcon supplyIcon = get16xIcon(supplyPicLocation);
					ImageIcon bigSupplyIcon = new ImageIcon(supplyPicLocation);
					Action resupplyAction = new Action() {
						public boolean enabled = true;

						@Override
						public void actionPerformed(ActionEvent e) {
							try {
								u.move((Terrain) newLoc,true);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							display.shouldBeHighlighted.clear();
							display.paintImmediately(display.getBounds());
							((Carry)u).resupply();							
							new Thread(new Runnable(){
								public void run(){
									Set<Terrain> whereToDraw= new HashSet<>();
									for(Unit tOcc:adjacentAlliedUnits){
										whereToDraw.add((Terrain) tOcc.getLocation());
									}
									whereToDraw.remove(u.getLocation());
									for(int x=0;x<4;x++){
										display.showIconsOnSetOfLocations(bigSupplyIcon.getImage(), whereToDraw);
										try {
											Thread.sleep(250);
										} catch (InterruptedException e1) {
											e1.printStackTrace();
										}
										display.paintImmediately(display.getBounds());
									}
								}
							}).start();

						}
						@Override
						public void setEnabled(boolean b) {
							enabled = b;
						}

						@Override
						public void removePropertyChangeListener(
								PropertyChangeListener listener) {}

						@Override
						public void putValue(String key, Object value) {}

						@Override
						public boolean isEnabled() {
							return enabled;
						}

						@Override
						public Object getValue(String key) {
							return null;
						}
						@Override
						public void addPropertyChangeListener(
								PropertyChangeListener listener) {}
					};
					resupplyOption.setAction(resupplyAction);
					resupplyOption.setText("resupply");
					resupplyOption.setIcon(supplyIcon);
					ans.add(resupplyOption);
				}
			}
			if(u instanceof Stealth){
				Stealth s = (Stealth) u;
				{
					JMenuItem hidingOption = new JMenuItem();
					ImageIcon hideIco = get16xIcon(MenuMaker.class.getClassLoader().getResource("resources/32x/hide.png"));
					ImageIcon hidePic = new ImageIcon(MenuMaker.class.getClassLoader().getResource("resources/32x/poof.png"));
					Action hidingAction = new Action(){
						public boolean enabled = true;
						@Override
						public void actionPerformed(ActionEvent arg0) {
							try {
								u.move((Terrain)newLoc, true);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							if(!s.isHidden()){
								s.hide();
							}else{
								s.unHide();
							}
							new Thread(new Runnable() {

								@Override
								public void run() {
									Set<Terrain> where = new HashSet<>();
									where.add((Terrain)newLoc);
									noBugsPls(display, (TerrainGrid) u.getGrid());
									for (int i = 0; i < 2; i++) {
										display.showIconsOnSetOfLocations(hidePic.getImage(), where);
										try {
											Thread.sleep(500); 
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										tryRepaint(display);
									}
									noBugsPls(display, (TerrainGrid) u.getGrid());

								}
							}).start();
							display.repaint();
						}

						@Override
						public void addPropertyChangeListener(PropertyChangeListener arg0) {
						}

						@Override
						public Object getValue(String arg0) {
							return null;
						}

						@Override
						public boolean isEnabled() {
							return enabled;
						}

						@Override
						public void putValue(String arg0, Object arg1) {
						}

						@Override
						public void removePropertyChangeListener(PropertyChangeListener arg0) {
						}

						@Override
						public void setEnabled(boolean arg0) {
							enabled = arg0;
						}
					};
					hidingOption.setAction(hidingAction);
					hidingOption.setIcon(hideIco);
					hidingOption.setText(s.isHidden()?"unhide":"hide");
					ans.add(hidingOption);
				}
			}else if(u instanceof Stealth2){
				Stealth2 s = (Stealth2) u;
				{
					JMenuItem hidingOption = new JMenuItem();
					ImageIcon hideIco = get16xIcon(MenuMaker.class.getClassLoader().getResource("resources/32x/hide.png"));
					ImageIcon hidePic = new ImageIcon(MenuMaker.class.getClassLoader().getResource("resources/32x/poof.png"));
					Action hidingAction = new Action(){
						public boolean enabled = true;
						@Override
						public void actionPerformed(ActionEvent arg0) {
							try {
								u.move((Terrain)newLoc, true);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							if(!s.isHidden()){
								s.hide();
							}else{
								s.unHide();
							}
							new Thread(new Runnable() {

								@Override
								public void run() {
									Set<Terrain> where = new HashSet<>();
									where.add((Terrain)newLoc);
									noBugsPls(display, (TerrainGrid) u.getGrid());
									for (int i = 0; i < 2; i++) {
										display.showIconsOnSetOfLocations(hidePic.getImage(), where);
										try {
											Thread.sleep(500); 
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										tryRepaint(display);
									}
									noBugsPls(display, (TerrainGrid) u.getGrid());								
								}
							}).start();
							display.repaint();
						}
						public void addPropertyChangeListener(PropertyChangeListener arg0) {
						}
						public Object getValue(String arg0) {
							return null;
						}
						public boolean isEnabled() {
							return enabled;
						}
						public void putValue(String arg0, Object arg1) {}
						public void removePropertyChangeListener(PropertyChangeListener arg0) {}
						public void setEnabled(boolean arg0) {
							enabled = arg0;
						}
					};
					hidingOption.setAction(hidingAction);
					hidingOption.setIcon(hideIco);
					hidingOption.setText(s.isHidden()?"unhide":"hide");
					ans.add(hidingOption);
				}
			}

			// infantry can capture
			if (u instanceof Infantry) {
				if (newLoc instanceof Property
						&& ((Property) newLoc).getOwner() != u.getOwner()) {
					JMenuItem captureOption = new JMenuItem();
					Action captureAction = new Action(){
						public boolean enabled = true;
						@Override
						public void actionPerformed(ActionEvent arg0) {
							Infantry i = (Infantry) u;
							try {
								i.move(((Terrain)newLoc), true);
							} catch (Exception e) {
								e.printStackTrace();
							}
							i.capture();
							URL flagIconLoc = MenuMaker.class.getClassLoader().getResource("resources/32x/capture.png");
							ImageIcon flagIco = new ImageIcon(flagIconLoc);
							new Thread(new Runnable() {

								@Override
								public void run() {
									Set<Terrain> where = new HashSet<>();
									where.add((Terrain)newLoc);
									noBugsPls(display, (TerrainGrid) u.getGrid());
									for (int i = 0; i < 2; i++) {
										display.showIconsOnSetOfLocations(flagIco.getImage(), where);
										try {
											Thread.sleep(500); 
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										tryRepaint(display);
									}
									noBugsPls(display, (TerrainGrid) u.getGrid());

								}
							}).start();
							display.repaint();
						}

						@Override
						public void addPropertyChangeListener(PropertyChangeListener arg0) {
						}

						@Override
						public Object getValue(String arg0) {
							return null;
						}

						@Override
						public boolean isEnabled() {
							return enabled;
						}

						@Override
						public void putValue(String arg0, Object arg1) {
						}

						@Override
						public void removePropertyChangeListener(PropertyChangeListener arg0) {
						}

						@Override
						public void setEnabled(boolean arg0) {
							enabled = arg0;
						}
					};
					captureOption.setAction(captureAction);
					URL capImagePath = this.getClass().getClassLoader().getResource("resources/32x/capture.png");
					captureOption.setIcon(get16xIcon(capImagePath));
					captureOption.setText("Capture");
					ans.add(captureOption);
				}
			}
		} else if (newLocOcc instanceof Carry
				&& !((Carry) newLocOcc).isFull()
				&& ((Carry) newLocOcc).canCarry(u)) {
			Carry c = (Carry) newLocOcc;
			JMenuItem loadOption = new JMenuItem();
			Action loadAction = new Action() {
				public boolean enabled = true;

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						u.move((Terrain) newLoc);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					if(u instanceof Stealth&&((Stealth) u).isHidden()){
						((Stealth) u).unHide();
					}
					c.addUnit(u);
					u.removeSelfFromGrid();
					new Thread(new Runnable(){
						public void run(){
							display.shouldBeHighlighted.clear();
							display.repaint();
						}
					}).start();

				}
				@Override
				public void setEnabled(boolean b) {
					enabled = b;
				}

				@Override
				public void removePropertyChangeListener(
						PropertyChangeListener listener) {}

				@Override
				public void putValue(String key, Object value) {}

				@Override
				public boolean isEnabled() {
					return enabled;
				}

				@Override
				public Object getValue(String key) {
					return null;
				}
				@Override
				public void addPropertyChangeListener(
						PropertyChangeListener listener) {}
			};
			loadOption.setAction(loadAction);
			URL imagePath = this.getClass().getClassLoader().getResource("resources/32x/load.png");
			loadOption.setIcon(get16xIcon(imagePath));
			loadOption.setText("Load");
			ans.add(loadOption);
		}else {
			//do nothing
		}

		//always a cancel option if anything else is do able
		if(!ans.isEmpty()){
			JMenuItem cancelOption = new JMenuItem("cancel",
					get16xIcon(this.getClass().getClassLoader().getResource("resources/32x/cancel.png")));
			ans.add(cancelOption);
		}
		return ans;

		// return (Method[]) (ans.toArray());
	}

	// Arrays.sort(methods, new Comparator<Method>()
	// {
	// public int compare(Method m1, Method m2)
	// {
	// int d1 = depth(m1.getDeclaringClass());
	// int d2 = depth(m2.getDeclaringClass());
	// if (d1 != d2)
	// return d2 - d1;
	// int d = m1.getName().compareTo(m2.getName());
	// if (d != 0)
	// return d;
	// d1 = m1.getParameterTypes().length;
	// d2 = m2.getParameterTypes().length;
	// return d1 - d2;
	// }
	//
	// private int depth(Class cl)
	// {
	// if (cl == null)
	// return 0;
	// else
	// return 1 + depth(cl.getSuperclass());
	// }
	// });
	// return methods;
	// }

	/**
	 * Makes a menu that displays all public methods of an object
	 * 
	 * @param occupant
	 *            the object whose methods should be displayed
	 * @param loc
	 *            the location of the occupant
	 * @return the menu to pop up
	 */
	public JPopupMenu makeMethodMenu(T occupant, Location loc) {

		return makeMoveMenu(occupant, loc);
		// old code

		// this.occupant = occupant;
		// this.currentLocation = loc;
		// JPopupMenu menu = new JPopupMenu();
		// Method[] methods = getMethods();
		// Class oldDcl = null;
		// for (int i = 0; i < methods.length; i++)
		// {
		// Class dcl = methods[i].getDeclaringClass();
		// if (dcl != Object.class)
		// {
		// if (i > 0 && dcl != oldDcl)
		// menu.addSeparator();
		// menu.add(new MethodItem(methods[i]));
		// }
		// oldDcl = dcl;
		// }
		// return menu;

	}

	/**
	 * Makes a menu that displays all public constructors of a collection of
	 * classes.
	 * 
	 * @param classes
	 *            the classes whose constructors should be displayed
	 * @param loc
	 *            the location of the occupant to be constructed
	 * @return the menu to pop up
	 */
	public JPopupMenu makeConstructorMenu(Collection<Class> classes,
			Location loc) {
		this.currentLocation = loc;
		JPopupMenu menu = new JPopupMenu();
		// ???
		// boolean first = true;

		//
		if (!(loc instanceof Factory)) {
			return menu;
		}else if(((Property) loc).getOwner()!=Runner.getTurnPlayer()){
			return menu;
		}
		try {
			Factory fac = (Factory) loc;
			for (Constructor<? extends Unit> constructor : fac
					.getBuildableUnits()) {
				JMenuItem tmp = new JMenuItem();
				Action a = new Action() {
					public boolean enabled = true;

					@Override
					public void actionPerformed(ActionEvent e) {
						fac.buildUnit(constructor);
						Unit occ = (Unit) fac.getHostGrid().get(fac);
						if(null!=occ){
							(occ).immobilize();
						}
						MenuMaker.noBugsPls(display, fac.getHostGrid());
					}

					@Override
					public void setEnabled(boolean b) {
						enabled = b;
					}

					@Override
					public void removePropertyChangeListener(
							PropertyChangeListener listener) {
					}

					@Override
					public void putValue(String key, Object value) {
					}

					@Override
					public boolean isEnabled() {
						return enabled;
					}

					@Override
					public Object getValue(String key) {
						return null;
					}

					@Override
					public void addPropertyChangeListener(
							PropertyChangeListener listener) {
					}
				};
				a.setEnabled(true);
				tmp.setAction(a);
				Player[] p = { null };
				Unit u = constructor.newInstance(p);
				int cost = u.getBuildCost();
				String spaces = (cost < 1000 ? "  " : "");
				String name = constructor.getDeclaringClass().getSimpleName();
				name = +cost + spaces + " | " + name;
				// ResourceBundle b = new ResourceBundle("")
				// TODO nicer naming, picture
				tmp.setText(name);
				URL imagePath = this.getClass().getClassLoader().getResource(
						"resources/units/"+constructor.getDeclaringClass().getSimpleName()+".png");
				tmp.setIcon(get16xIcon(imagePath));
				menu.add(tmp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return menu;

		//
		// Iterator<Class> iter = classes.iterator();
		// while (iter.hasNext())
		// {
		// if (first)
		// first = false;
		// else
		// menu.addSeparator();
		// Class cl = iter.next();
		// Constructor[] cons = (Constructor[]) cl.getConstructors();
		// for (int i = 0; i < cons.length; i++)
		// {
		// menu.add(new OccupantConstructorItem(cons[i]));
		// }
		// }

	}

	/**
	 * Adds menu items that call all public constructors of a collection of
	 * classes to a menu
	 * 
	 * @param menu
	 *            the menu to which the items should be added
	 * @param classes
	 *            the collection of classes
	 */
	public void addConstructors(JMenu menu, Collection<Class> classes) {
		boolean first = true;
		Iterator<Class> iter = classes.iterator();
		while (iter.hasNext()) {
			if (first)
				first = false;
			else
				menu.addSeparator();
			Class cl = iter.next();
			Constructor[] cons = cl.getConstructors();
			for (int i = 0; i < cons.length; i++) {
				menu.add(new GridConstructorItem(cons[i]));
			}
		}
	}

	private Method[] getMethods() {
		Class cl = occupant.getClass();
		Method[] methods = cl.getMethods();
		Arrays.sort(methods, new Comparator<Method>() {
			public int compare(Method m1, Method m2) {
				int d1 = depth(m1.getDeclaringClass());
				int d2 = depth(m2.getDeclaringClass());
				if (d1 != d2)
					return d2 - d1;
				int d = m1.getName().compareTo(m2.getName());
				if (d != 0)
					return d;
				d1 = m1.getParameterTypes().length;
				d2 = m2.getParameterTypes().length;
				return d1 - d2;
			}

			private int depth(Class cl) {
				if (cl == null)
					return 0;
				else
					return 1 + depth(cl.getSuperclass());
			}
		});
		return methods;
	}

	/**
	 * A menu item that shows a method or constructor.
	 */
	private class MCItem extends JMenuItem {
		public String getDisplayString(Class retType, String name,
				Class[] paramTypes) {
			StringBuffer b = new StringBuffer();
			b.append("<html>");
			if (retType != null)
				appendTypeName(b, retType.getName());
			b.append(" <font color='blue'>");
			appendTypeName(b, name);
			b.append("</font>( ");
			for (int i = 0; i < paramTypes.length; i++) {
				if (i > 0)
					b.append(", ");
				appendTypeName(b, paramTypes[i].getName());
			}
			b.append(" )</html>");
			return b.toString();
		}

		public void appendTypeName(StringBuffer b, String name) {
			int i = name.lastIndexOf('.');
			if (i >= 0) {
				String prefix = name.substring(0, i + 1);
				if (!prefix.equals("java.lang")) {
					b.append("<font color='gray'>");
					b.append(prefix);
					b.append("</font>");
				}
				b.append(name.substring(i + 1));
			} else
				b.append(name);
		}

		public Object makeDefaultValue(Class type) {
			if (type == int.class)
				return new Integer(0);
			else if (type == boolean.class)
				return Boolean.FALSE;
			else if (type == double.class)
				return new Double(0);
			else if (type == String.class)
				return "";
			else if (type == Color.class)
				return Color.BLACK;
			else if (type == Location.class)
				return currentLocation;
			else if (Grid.class.isAssignableFrom(type))
				return currentGrid;
			else {
				try {
					return type.newInstance();
				} catch (Exception ex) {
					return null;
				}
			}
		}
	}

	private abstract class ConstructorItem extends MCItem {
		public ConstructorItem(Constructor c) {
			setText(getDisplayString(null, c.getDeclaringClass().getName(),
					c.getParameterTypes()));
			this.c = c;
		}

		public Object invokeConstructor() {
			Class[] types = c.getParameterTypes();
			Object[] values = new Object[types.length];

			for (int i = 0; i < types.length; i++) {
				values[i] = makeDefaultValue(types[i]);
			}

			if (types.length > 0) {
				PropertySheet sheet = new PropertySheet(types, values);
				JOptionPane.showMessageDialog(this, sheet,
						resources.getString("dialog.method.params"),
						JOptionPane.QUESTION_MESSAGE);
				values = sheet.getValues();
			}

			try {
				return c.newInstance(values);
			} catch (InvocationTargetException ex) {
				parent.new GUIExceptionHandler().handle(ex.getCause());
				return null;
			} catch (Exception ex) {
				parent.new GUIExceptionHandler().handle(ex);
				return null;
			}
		}

		private Constructor c;
	}

	private class OccupantConstructorItem extends ConstructorItem implements
	ActionListener {
		public OccupantConstructorItem(Constructor c) {
			super(c);
			addActionListener(this);
			setIcon(displayMap.getIcon(c.getDeclaringClass(), 16, 16));
		}

		@SuppressWarnings("unchecked")
		public void actionPerformed(ActionEvent event) {
			T result = (T) invokeConstructor();
			parent.getWorld().add(currentLocation, result);
			parent.repaint();
		}
	}

	private class GridConstructorItem extends ConstructorItem implements
	ActionListener {
		public GridConstructorItem(Constructor c) {
			super(c);
			addActionListener(this);
			setIcon(displayMap.getIcon(c.getDeclaringClass(), 16, 16));
		}

		@SuppressWarnings("unchecked")
		public void actionPerformed(ActionEvent event) {
			Grid<T> newGrid = (Grid<T>) invokeConstructor();
			parent.setGrid(newGrid);
		}
	}

	private class MethodItem extends MCItem implements ActionListener {
		public MethodItem(Method m) {
			setText(getDisplayString(m.getReturnType(), m.getName(),
					m.getParameterTypes()));
			this.m = m;
			addActionListener(this);
			setIcon(displayMap.getIcon(m.getDeclaringClass(), 16, 16));
		}

		public void actionPerformed(ActionEvent event) {
			Class[] types = m.getParameterTypes();
			Object[] values = new Object[types.length];

			for (int i = 0; i < types.length; i++) {
				values[i] = makeDefaultValue(types[i]);
			}

			if (types.length > 0) {
				PropertySheet sheet = new PropertySheet(types, values);
				JOptionPane.showMessageDialog(this, sheet,
						resources.getString("dialog.method.params"),
						JOptionPane.QUESTION_MESSAGE);
				values = sheet.getValues();
			}

			try {
				Object result = m.invoke(occupant, values);
				parent.repaint();
				if (m.getReturnType() != void.class) {
					String resultString = result.toString();
					Object resultObject;
					final int MAX_LENGTH = 50;
					final int MAX_HEIGHT = 10;
					if (resultString.length() < MAX_LENGTH)
						resultObject = resultString;
					else {
						int rows = Math.min(MAX_HEIGHT,
								1 + resultString.length() / MAX_LENGTH);
						JTextArea pane = new JTextArea(rows, MAX_LENGTH);
						pane.setText(resultString);
						pane.setLineWrap(true);
						resultObject = new JScrollPane(pane);
					}
					JOptionPane.showMessageDialog(parent, resultObject,
							resources.getString("dialog.method.return"),
							JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (InvocationTargetException ex) {
				parent.new GUIExceptionHandler().handle(ex.getCause());
			} catch (Exception ex) {
				parent.new GUIExceptionHandler().handle(ex);
			}
		}

		private Method m;
	}

	private T occupant;
	private Grid currentGrid;
	private Location currentLocation;
	private WorldFrame<T> parent;
	private DisplayMap displayMap;
	private ResourceBundle resources;
}

class PropertySheet extends JPanel {
	/**
	 * Constructs a property sheet that shows the editable properties of a given
	 * object.
	 * 
	 * @param object
	 *            the object whose properties are being edited
	 */
	public PropertySheet(Class[] types, Object[] values) {
		this.values = values;
		editors = new PropertyEditor[types.length];
		setLayout(new FormLayout());
		for (int i = 0; i < values.length; i++) {
			JLabel label = new JLabel(types[i].getName());
			add(label);
			if (Grid.class.isAssignableFrom(types[i])) {
				label.setEnabled(false);
				add(new JPanel());
			} else {
				editors[i] = getEditor(types[i]);
				if (editors[i] != null) {
					editors[i].setValue(values[i]);
					add(getEditorComponent(editors[i]));
				} else
					add(new JLabel("?"));
			}
		}
	}

	/**
	 * Gets the property editor for a given property, and wires it so that it
	 * updates the given object.
	 * 
	 * @param bean
	 *            the object whose properties are being edited
	 * @param descriptor
	 *            the descriptor of the property to be edited
	 * @return a property editor that edits the property with the given
	 *         descriptor and updates the given object
	 */
	public PropertyEditor getEditor(Class type) {
		PropertyEditor editor;
		editor = defaultEditors.get(type);
		if (editor != null)
			return editor;
		editor = PropertyEditorManager.findEditor(type);
		return editor;
	}

	/**
	 * Wraps a property editor into a component.
	 * 
	 * @param editor
	 *            the editor to wrap
	 * @return a button (if there is a custom editor), combo box (if the editor
	 *         has tags), or text field (otherwise)
	 */
	public Component getEditorComponent(final PropertyEditor editor) {
		String[] tags = editor.getTags();
		String text = editor.getAsText();
		if (editor.supportsCustomEditor()) {
			return editor.getCustomEditor();
		} else if (tags != null) {
			// make a combo box that shows all tags
			final JComboBox comboBox = new JComboBox(tags);
			comboBox.setSelectedItem(text);
			comboBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent event) {
					if (event.getStateChange() == ItemEvent.SELECTED)
						editor.setAsText((String) comboBox.getSelectedItem());
				}
			});
			return comboBox;
		} else {
			final JTextField textField = new JTextField(text, 10);
			textField.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					try {
						editor.setAsText(textField.getText());
					} catch (IllegalArgumentException exception) {
					}
				}

				public void removeUpdate(DocumentEvent e) {
					try {
						editor.setAsText(textField.getText());
					} catch (IllegalArgumentException exception) {
					}
				}

				public void changedUpdate(DocumentEvent e) {
				}
			});
			return textField;
		}
	}

	public Object[] getValues() {
		for (int i = 0; i < editors.length; i++)
			if (editors[i] != null)
				values[i] = editors[i].getValue();
		return values;
	}

	private PropertyEditor[] editors;
	private Object[] values;

	private static Map<Class, PropertyEditor> defaultEditors;

	// workaround for Web Start bug
	public static class StringEditor extends PropertyEditorSupport {
		public String getAsText() {
			return (String) getValue();
		}

		public void setAsText(String s) {
			setValue(s);
		}
	}

	static {
		defaultEditors = new HashMap<Class, PropertyEditor>();
		defaultEditors.put(String.class, new StringEditor());
		defaultEditors.put(Location.class, new LocationEditor());
		defaultEditors.put(Color.class, new ColorEditor());
	}
}
