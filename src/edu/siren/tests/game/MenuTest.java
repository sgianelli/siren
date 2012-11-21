package edu.siren.tests.game;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import edu.siren.core.tile.World;
import edu.siren.game.Player;
import edu.siren.game.gui.Menu;
import edu.siren.game.worlds.TestBox;
import edu.siren.gui.Gui;
import edu.siren.renderer.Screen;

public class MenuTest {
	
	public static void main(String[] args) {
		
		try {
			
			// Create a Screen
			Screen screen = new Screen("Screen", 800, 600, true);
	        
	        World world = new TestBox(1024, 1024);
	        
	        // Add the player
	        Player player = new Player("res/tests/scripts/entities/villager-justin.json");
	        player.setPosition(0, 0);
	        world.addEntity(player);		
	        
			// Create Test World
			world.getCamera().enable();
			
			Gui menu = new Menu(screen);
			
	        // The close event can take a bit to propagate
	        while (screen.nextFrame()) {            
	            if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
	                world.zoomIn();
	            } else if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
	            	world.zoomOut();
	            }
	            world.draw();
	            //menu.run();

	        }
			
			
		} catch (LWJGLException e) {

			// Print Error and kill program
			System.err.println("Error Testing Menu: ");
			System.err.println(e.getMessage());
			System.exit(1);
		
		} catch (IOException e) {

			// Print Error and kill program
			System.err.println("Error Testing Menu: ");
			System.err.println(e.getMessage());
			System.exit(1);
		}
		
		
	}

}
