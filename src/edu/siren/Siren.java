package edu.siren;

import java.io.IOException;

import org.lwjgl.LWJGLException;

import edu.siren.game.GamePlay;
import edu.siren.game.gui.Intro;
import edu.siren.game.gui.Title;
import edu.siren.game.profile.Profile;
import edu.siren.game.profile.ProfileManager;
import edu.siren.gui.GameLogin;
import edu.siren.renderer.Screen;

/**
 * The Main Game Play for the Siren
 * 
 * @author georgeamaya
 *
 */
public class Siren {
	
	
	private ProfileManager profileManager;
	private Screen screen;
	private Profile currentProfile;

	private boolean gamePlay;
	
	public Siren() throws LWJGLException {
		
		// Profile Manager
		this.profileManager = new ProfileManager();
		
		// Create Screen for Game Play
		this.screen = new Screen("Siren", 512, 448);
		
		// Assume game Play to start
		gamePlay = true;
		
	}
	
	public static void main(String[] args) {
		
		try {

			Siren siren = new Siren();
			siren.play();

		
		} catch (Exception e) {
			
			// Crap!! Game Error
			System.err.println("Error Starting up the Game!");
			e.printStackTrace();
			
		}
		
	}
	
	public void play() throws IOException, LWJGLException {
		
		
		// Create Splash
		Title splashScreen = new Title(screen);

		// Show Splash screen
		gamePlay = splashScreen.show();
		
		// Play the Game
		while(gamePlay) {
						
			// Show the Profile Login Screen
			GameLogin gl = new GameLogin(screen);
			gl.run();

			// Do we Continue?
			if (!gl.loginComplete())
				break;
			
			// Get the Profile
			currentProfile = gl.getProfile();

			// Play Game 
			showGame();
			
			// Save the Profile
			if (currentProfile != null) {
				System.out.println("Saving Profile");
				profileManager.save(currentProfile);
			}
			
		}
			
					
		// Screen Cleanup
		screen.cleanup();
		
	}
	
	private void showGame() throws IOException, LWJGLException {
		
		// If we have a profile, let's play
		if (currentProfile == null)
			return;
			
			// New Profiles
			if ( currentProfile.isNewProfile()) {
				
				// Show the Intro
				Intro intro = new Intro(screen);
				while(intro.running()) {
					intro.run();
				}
				
			}
			
			// Start the Game
			GamePlay gamePlay = new GamePlay(screen, currentProfile);
			gamePlay.play();
			
	}

}
