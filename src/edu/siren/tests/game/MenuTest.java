package edu.siren.tests.game;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import edu.siren.core.tile.Tile;
import edu.siren.core.tile.World;
import edu.siren.game.Player;
import edu.siren.game.menu.Menu;
import edu.siren.game.players.Pikachu;
import edu.siren.game.worlds.TestBox;
import edu.siren.gui.Element;
import edu.siren.gui.ElementEvent;
import edu.siren.gui.GuiContainer;
import edu.siren.gui.Image;
import edu.siren.gui.Text;
import edu.siren.gui.Window;
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
	        Window overlay = new Window("Info Overlay"); 
	        overlay.dimensions(screen.width, screen.height);
	        GuiContainer guiContainer = new GuiContainer();
	        
	        // Create a GUI using the Perspective2D camera
	        Perspective2D gui = new Perspective2D();
	        Shader shader = new Shader("res/tests/glsl/2d-perspective.vert", "res/tests/glsl/2d-perspective.frag");
	        gui.bindToShader(shader);
	        Font font = new Font("res/tests/fonts/proggy.png");
	        
	        // Add the player
	        Player player = new Pikachu();
	        player.setPosition(0, 0);
	        player.follow = true;
	        world.addEntity(player);		
	        
			// Create Test World
			world.getCamera().enable();
			Menu menu = new Menu(screen);
						
			// Image Left - Info
			Image leftInfo = new Image("res/game/menu/left-info.png", "");
			leftInfo.position(5,  7);			
			
			Text life = new Text("TIME OF DAY", 2);
			life.xy(20, 35);
			life.priority(100);
			overlay.add(life);
			
			
			overlay.add(leftInfo);
			guiContainer.add(overlay);
			

			
			
	        // The close event can take a bit to propagate
	        while (screen.nextFrame()) {            
	            if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
	                world.zoomIn();
	            } else if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
	                world.zoomOut();
	            } else if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
	                world.changeEnvironment(World.Environment.NIGHT, 5000);
	                //activeOverlay = veryCloudy;
	            } else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
	                world.changeEnvironment(World.Environment.MORNING, 5000);
	                //activeOverlay = pixelCloudy;
	            } else if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
	                world.changeEnvironment(World.Environment.AFTERNOON, 5000);
	                //activeOverlay = slightlyCloudy;
	            } else if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
	                world.changeEnvironment(World.Environment.DUSK, 5000);
	                //activeOverlay = plasma;
	            } else if(Keyboard.isKeyDown(Keyboard.KEY_P)) {
	            	world.pause();
	            } else if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
	            	world.play();
	            } else if (Keyboard.isKeyDown(Keyboard.KEY_M)) {
	            	world.pause();
	            	menu.run();
	            } else if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
	            	world.play();
	            }
	            
	            // Draw the World
	            world.draw();
	            
	            shader.use();
	            font.print(world.getCurrentEnvironment().name(), 2, 10, 10);
	            
	            shader.release();
	            guiContainer.draw();
	            

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
