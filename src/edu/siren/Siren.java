package edu.siren;

import edu.siren.game.GamePlay;
import edu.siren.game.gui.Title;
import edu.siren.game.profile.Profile;
import edu.siren.gui.GameLogin;
import edu.siren.renderer.Screen;

/**
 * The Main Game Play for the Siren
 * 
 * @author georgeamaya
 *
 */
public class Siren {
	
	
	public static void main(String[] args) {
		
		try {
			
			// Create Screen for Game Play
			Screen screen = new Screen("Gui Standalone Test", 512, 448);

			// Display the Game Splash
			Title splashScreen = new Title(screen);
			
			// Game Profile
			Profile profile = null;
			
			// If User wants to play.. 
			if ( splashScreen.show() ) {
				
				// Show the Profile Login Screen
				GameLogin gl = new GameLogin(screen);
				
				// Run the Game Login
				gl.run();
									
				// Get the Profile
				profile = gl.getProfile();
				
				// If we have a profile, let's play
				if (profile != null) {	
					
					// Start the Game
					GamePlay gamePlay = new GamePlay(screen, profile);
					gamePlay.play();
					
				}
				
			}
			
			// Cleanup 
			screen.cleanup();
		
		} catch (Exception e) {
			
			// Crap!! Game Error
			System.err.println("Error Starting up the Game!");
			e.printStackTrace();
			
		}
		
	}
	

}
