package edu.siren.game.profile;

import java.io.IOException;
import java.io.Serializable;

import org.lwjgl.LWJGLException;

import edu.siren.core.tile.World;
import edu.siren.game.Player;
import edu.siren.game.players.GeneratePlayer;
import edu.siren.game.worlds.ItemShop;
import edu.siren.game.worlds.Pangea;
import edu.siren.game.worlds.Rocko;
import edu.siren.game.worlds.Sushi;

public class Profile implements Serializable{

	// Serial ID
	private static final long serialVersionUID = 1L;
	public static Profile active = null;
	
	// Profile Information
	private String name;
	private String password;
	
	// Sprite Info
	private String spriteName;
	private transient Player player;
	
	// Profile
	private GameStats gameStats;
	
	// Is this a new Profile?
	private boolean newProfile;
	
	// The Current World
	private String worldName;
	private transient World world;
	
	/**
	 * Create a New Profile
	 * 
	 */
	public Profile() {
		gameStats = new GameStats();
		newProfile = true;
		Profile.active = this;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the spriteName
	 */
	public String getSpriteName() {
		return spriteName;
	}
	/**
	 * @param spriteName the spriteName to set
	 */
	public void setSpriteName(String spriteName) {
		this.spriteName = spriteName;
	}
	
	public Player getPlayer() {
		
		// Load the Player based on Character Name
		if (player == null) {
			
			// Generate the Player
			player = GeneratePlayer.build(this.spriteName);
			
		}
		
		// Return the Player
		return player;
		
	}
	/**
	 * @return the newProfile
	 */
	public boolean isNewProfile() {
		return newProfile;
	}
	/**
	 * @param newProfile the newProfile to set
	 */
	public void setNewProfile(boolean newProfile) {
		this.newProfile = newProfile;
	}
	/**
	 * @return the gameStats
	 */
	public GameStats getGameStats() {
		return gameStats;
	}
	/**
	 * @param gameStats the gameStats to set
	 */
	public void setGameStats(GameStats gameStats) {
		this.gameStats = gameStats;
	}

	/**
	 * @return the world
	 */
	public World getWorld() {

		
		if (world == null) {
		
			try {
				
				if (worldName.equals("Pangea")) {
					world = new Pangea();
				} else if (worldName.equals("Sushi")) {
					world = new Sushi();
				} else if (worldName.equals("Rocko")) {
				    world = new Rocko();
				} else if (worldName.equals("ItemShop")) {
				    world = new ItemShop();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LWJGLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return world;
	}

	/**
	 * @param world the world to set
	 */
	public void setWorld(World world) {
		this.world = world;
	}

	/**
	 * @return the worldName
	 */
	public String getWorldName() {
		return worldName;
	}

	/**
	 * @param worldName the worldName to set
	 */
	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

    public void addCoins(int n) {
        this.gameStats.setCoins(this.gameStats.getCoins() + n);
    }
}
