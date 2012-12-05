package edu.siren.game.items;

import java.io.Serializable;

import edu.siren.game.Player;
import edu.siren.game.battle.BattleManager;
import edu.siren.game.profile.Profile;
import edu.siren.gui.Image;

/**
 * Items that can be owned by players
 * 
 * @author georgeamaya
 *
 */
public abstract class Item implements Serializable {

	// Name of the Item
	private String name;
	
	// Icon of the item
	private String icon;
	
	// The Profile that owns the Item
	private Profile profile;
	
	public Item(String name, Profile profile) {
		this.name = name;
		this.profile = profile;
	}
	
	/**
	 * Use the Item for the owner of the item.
	 * 
	 */

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
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return the profile
	 */
	public Profile getProfile() {
		return profile;
	}

	/**
	 * @param profile the profile to set
	 */
	public void setProfile(Profile profile) {
		this.profile = profile;
	}

    public abstract void use(Player player, BattleManager manager);
	
}
