package edu.siren.game.profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
	private static String baseDir = System.getProperty("user.dir")
			+ File.separator + "profiles" + File.separator;
	
	// File Extension
	private final String extension = ".profile";
	
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
		
		// Verify that the profile doesn't already exist
		if (exists(profile)) {
			throw new ProfileException("The Character Already exists.");
		}
		
		// Sprite Name 
		if (profile.getName().isEmpty()) {
			throw new ProfileException("You must select a sprite.");
		}
		
	}
	
	/**
	 * Get a Profile based upon Name
	 * 
	 * @param profile
	 * @throws IOException 
	 */
	public Profile get(String profileName) throws ProfileException {
		
		// The Profile to return
		Profile profile = null;
		
		// Get Location of Profile
		String profileLocation = baseDir + profileName.toLowerCase() + extension; 
		
		try {

			// Read the file containing the object
			FileInputStream inFile = new FileInputStream(profileLocation);
		
			// Open the Object
			ObjectInputStream inputStream = new ObjectInputStream(inFile);
			
			// Create Profile object
			profile = (Profile) inputStream.readObject();
			
			// Close the Stream
			inputStream.close();

		} catch (FileNotFoundException e) {

			throw new ProfileException("The Profile " + profileName + " does not exist.");
		
		} catch (IOException e) {
			
			throw new ProfileException("Could no load profile " + profileName + ".");
		} catch (ClassNotFoundException e) {

			// Let's do nothing for this error
		}
		
		// Return the Profile
		return profile;
		
	}
	
	/** 
	 * Save the profile
	 * 
	 */
	public void save(Profile profile) {
		
		try {
			
			// Save Location
			String saveLocation = this.baseDir + profile.getName().toLowerCase() + extension;
			
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
	
	/**
	 * Determines whether or not a profile already exists. 
	 * 
	 * This checks to see if the profile has been saved to disk.
	 * 
	 * @param profile
	 * @return
	 */
	public boolean exists(Profile profile) {
				
		// File Exists
		boolean exists = false;
				
		// Get File Location
		String fileLocation = baseDir;
		
		// Load File
		File dir = new File(fileLocation);
		
		// Search for the File
		for (File file : dir.listFiles() ) {
			
			String fileName = file.getName();
			System.out.println(fileName);
			
			// Is the file we are looking for?
			if (file.getName().equalsIgnoreCase(profile.getName() + extension)) {
				
				// This is the file we are looking for
				exists = true;
				break;
				
			}
			
			// That was not the file we were looking for
			
		}
		
		// Return whether or not it exists
		return exists;
		
	}
	
	
	
}
