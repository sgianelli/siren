package edu.siren.game.profile;

import java.io.Serializable;


/**
 * Profile Manager creates and/or updates profiles.
 * 
 * 
 * 
 * @author georgeamaya
 *
 */
public class ProfileManager implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Validate that a profile is valid... 
	 * 
	 * That is, a sprite has been chosen and a name doens't exist
	 * 
	 * @return
	 */
	public void validate(Profile profile) throws ProfileException {
		
		// Profile Name can't be empty
		if (profile.getName().isEmpty()) {
			throw new ProfileException("Character Name cannot be empty! Duh!");
		}
		
		// Verify that Only Characters and Numbers have been used....
		System.out.println("Profile Name: >" + profile.getName() + "<");
		if (!profile.getName().matches("\\w+")) {
			throw new ProfileException("Character Name invalid.");
		}
		
	}

	
	
}
