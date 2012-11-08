package edu.siren.core.geom;

import java.io.Serializable;

/**
 * The Location of a Unit on the Map
 * 
 * @author The Java Throwers
 *
 */
public class Point implements Serializable {

	// Serial ID
	private static final long serialVersionUID = 1L;
	
	// Point Location
	private int x;
	private int y;
	
	public Point(int x, int y) {
		
		this.x = x;
		this.y = y;
		
	}
	
	/**
	 * Get the X Location 
	 * 
	 * @return int
	 */
	public int getXLocation() {
		return this.x;
	}
	
	/**
	 * Get the Y Location
	 * 
	 * @return int
	 */
	public int getYLocation() {
		return this.y;
	}
	
	/**
	 * Set the x Location
	 */
	public void setXLocation(int x) {
		this.x = x;
	}
	
	/**
	 * Set the y location
	 */
	public void setYLocation(int y) {
		this.y  = y;
	}
	
}
