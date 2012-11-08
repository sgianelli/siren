package edu.siren.unit;

import java.io.Serializable;

import edu.siren.core.geom.Point;

/**
 * The Base Unit Class
 * 
 * @author Java Throwers
 *
 */
public abstract class BaseUnit implements Serializable {

	// The Current Location of a Unit
	private Point currentLocation;
	
	// Serial ID
	private static final long serialVersionUID = -3978890929306129878L;

	/**
	 * Create a Base Unit Class with a Location
	 * 
	 */
	public BaseUnit(Point location) {
		this.currentLocation = location;
	}
	
	/**
	 * Get the Current location of a Sprit
	 * 
	 */
	public Point getCurrentLocation() {
		return this.currentLocation;
	}
	
	/**
	 * The Sprite has the ability to commicate with
	 * other sprites
	 * 
	 * @return
	 */
	public abstract boolean canCommunicate();
	
	/**
	 * Is the Base Unit Magical
	 * 
	 * @return
	 */
	public abstract boolean isMagical();
	
}
