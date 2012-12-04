package edu.siren.game.items;

import edu.siren.game.profile.GameStats;
import edu.siren.game.profile.Profile;

/**
 * Healing potion that heals the player
 * 
 * 
 * @author georgeamaya
 *
 */
public class HealingPotion extends Item {

	// Healing Power for this Potion
	private int healingPower;
	
	private static final int DEFAULT_HEALING = 1;

	
	private static final long serialVersionUID = 1L;
	
	

	public HealingPotion(Profile profile) {

		// Set Default Healing Power
		this(profile, DEFAULT_HEALING);
		
	}
	
	/**
	 * Set the Healing Power of the Potion
	 * 
	 * @param profile
	 * @param healingPower
	 */
	public HealingPotion(Profile profile, int healingPower) {

		// Call Super Constructor
		super("Healing Potion", profile);

		// Set Healing Power
		this.healingPower = healingPower;
		
	}
	
	
	@Override
	public void use() {
		
		// Get Health
		int health = getProfile().getGameStats().getHealth(); 
		
		// Heal the Player if they are not at the Max
		if ( health < GameStats.MAX_HEALTH) {
			getProfile().getGameStats().setHealth(health + this.healingPower);
		}		
		
	}


	/**
	 * @return the healingPower
	 */
	public int getHealingPower() {
		return healingPower;
	}


	/**
	 * @param healingPower the healingPower to set
	 */
	public void setHealingPower(int healingPower) {
		this.healingPower = healingPower;
	}

}
