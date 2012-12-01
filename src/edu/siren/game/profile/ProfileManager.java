package edu.siren.game.profile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

	// Base Directory for Saving Objects
	public static String baseDir = System.getProperty("user.dir")
			+ File.separator + "profiles" + File.separator;
	
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
		if (!profile.getName().matches("\\w+")) {
			throw new ProfileException("Character Name invalid.");
		}
		
		// Sprite Name 
		if (profile.getName().isEmpty()) {
			throw new ProfileException("You must select a sprite.");
		}
		
	}
	
	/** 
	 * Save the profile
	 * 
	 */
	public void save(Profile profile) {
		
		try {
			
			// Save Location
			String saveLocation = this.baseDir + profile.getName() + ".profile";
			
			// Write out the profile
			FileOutputStream outFile = new FileOutputStream( saveLocation );
			
			// Write the Object
			ObjectOutputStream outputStream = new ObjectOutputStream(outFile);
			outputStream.writeObject(profile);
			outputStream.close();

		} catch (IOException ioe) {
	
			String message = "Error writing objects to disk: " + "\n" + ioe
					+ "\nHope you had data backed up...";
			System.err.println(message);
		}
		
	}
	
	
	
}
