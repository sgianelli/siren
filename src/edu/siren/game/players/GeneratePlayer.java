package edu.siren.game.players;

import edu.siren.game.Player;

public class GeneratePlayer {

	// Location of Players
	private final static String PLAYERS_LOCATION = "edu.siren.game.players.";
	
	/**
	 * Generates a Player based upon Name
	 * 
	 * @param name
	 * @return
	 */
	public static Player build(String name) {
		
		// Initailize the Player
		Player player = null;
		
		try {
			
			// Create Class Object
			Class playerClass =  Class.forName(PLAYERS_LOCATION + name);
			if ( playerClass == null) {
				System.err.println("Player Class Not Created");
			}
			
			// Create New Instance of the Player
			player = (Player)playerClass.newInstance();
			if (player == null) {
				System.err.println("Player was not initialized.");
			}
			
		} catch (Exception e) {

			// Uh Oh!
			System.err.println("Could not create Player for " + name);
			e.printStackTrace();
			
		}
		
		// Return the Player
		return player;
		
	}
	
}
