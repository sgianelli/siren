package edu.siren.tests.game;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import edu.siren.core.tile.World;
import edu.siren.game.Player;
import edu.siren.game.menu.HelpMenu;
import edu.siren.game.menu.Menu;
import edu.siren.game.players.Pikachu;
import edu.siren.game.profile.Profile;
import edu.siren.game.status.GameStatus;
import edu.siren.game.worlds.TestBox;
import edu.siren.gui.GuiContainer;
import edu.siren.gui.Image;
import edu.siren.gui.Text;
import edu.siren.renderer.Font;
import edu.siren.renderer.Perspective2D;
import edu.siren.renderer.Screen;
import edu.siren.renderer.Shader;

public class MenuTest {
	
	public static void main(String[] args) {
		
		try {
			
			// Create a Screen
			Screen screen = new Screen("Screen", 512, 448);
	        
	        World world = new TestBox(1024, 1024);
	        GameStatus gameStatus = new GameStatus(screen); 
	        
	        // Create a GUI using the Perspective2D camera
	        

	        
	        
	        // Add the player
	        Player player = new Pikachu();
	        player.setPosition(0, 0);
	        player.follow = true;
	        world.addEntity(player);		
	        
			// Create Test World
			world.getCamera().enable();
			Menu helpMenu = new HelpMenu(screen);
						
		
			

			
			

			

			
			
	        // The close event can take a bit to propagate
		
	        while (screen.nextFrame()) {     

	        	if (Keyboard.isKeyDown(Keyboard.KEY_Z))
            		world.zoomIn();
	        	if (Keyboard.isKeyDown(Keyboard.KEY_X)) 
	        		world.zoomOut();
	        	
	        	
	        	while(Keyboard.next()) {
	        		if (Keyboard.getEventKeyState()) {
			            if (Keyboard.getEventKey() == Keyboard.KEY_1) {
			                world.changeEnvironment(World.Environment.NIGHT, 5000);
	
			            } else if (Keyboard.getEventKey() == Keyboard.KEY_2) {
			                world.changeEnvironment(World.Environment.MORNING, 5000);
	
			            } else if (Keyboard.getEventKey() == Keyboard.KEY_3) {
			                world.changeEnvironment(World.Environment.AFTERNOON, 5000);
	
			            } else if (Keyboard.getEventKey() == Keyboard.KEY_4) {
			                world.changeEnvironment(World.Environment.DUSK, 5000);
	
			            } else if(Keyboard.getEventKey() == Keyboard.KEY_P) {
			            	world.pause();
			            } else if (Keyboard.getEventKey() == Keyboard.KEY_C) {
			            	world.play();
			            } else if (Keyboard.getEventKey() == Keyboard.KEY_H) {
			            	world.pause();
			            	helpMenu.show();
			            } else if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
			            	world.play();
			            } else if (Keyboard.getEventKey() == Keyboard.KEY_K ) {
			            	
			            	//gameStatus.setHealth(gameStatus.getHealth() - 1);
			            	
			            	
			            } else if (Keyboard.getEventKey() == Keyboard.KEY_L) {
			            	
			            	//gameStatus.setHealth(gameStatus.getHealth()+1);
			            }
	        		}
	        	}
	            
	            
	            // Draw the World
	            world.draw();
	            
	            //shader.use();
	
	            

	            gameStatus.setTimeOfDay(world.getCurrentEnvironment().name());
	            
	            
	            //gameStatus.update();
	            //shader.release();
	        }
	        
	        // Cleanup the Screen
	        screen.cleanup();
			
			
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
