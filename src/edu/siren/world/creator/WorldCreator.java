package edu.siren.world.creator;


import java.io.IOException;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;


import edu.siren.core.tile.Layer;
import edu.siren.core.tile.Tile;
import edu.siren.game.Player;
import edu.siren.game.World;
import edu.siren.game.characters.Villager;
import edu.siren.renderer.BufferType;
import edu.siren.renderer.Font;
import edu.siren.renderer.Perspective2D;
import edu.siren.renderer.Screen;
import edu.siren.renderer.Shader;

import org.lwjgl.input.Mouse;

public class WorldCreator {

	 Screen screen;
	 

	 WorldCreator() throws LWJGLException, IOException {
		 
		 
	        screen = new Screen("World Creator", 512, 448);
	        World world = new World(8096, 8096);
	        
	        Set<Layer> layers = new TreeSet<Layer>();  
	               
	        // Create a GUI using the Perspective2D camera
	        Perspective2D gui = new Perspective2D();
	        Shader shader = new Shader("res/tests/glsl/2d-perspective.vert", "res/tests/glsl/2d-perspective.frag");
	        gui.bindToShader(shader);
	        Font font = new Font("res/tests/fonts/nostalgia.png", 24);

	        // Create a new player and put them somewhere in the world
	        Player player = new Player("res/tests/scripts/entities/villager-justin.json");
	        player.setPosition(0, 0);
	        world.addEntity(player);// Create a new player and put them somewhere in the world
	        
	        Random random = new Random();
	
	            for (int j = 0; j < 30; j++) {
	                Villager v = new Villager(
	                        "res/tests/scripts/entities/villager-justin.json");
	                v.setPosition(random.nextInt(1024), random.nextInt(1024));
	                world.addEntity(v);
	        }

	        
	        // These are some overlays that we will draw over the Gui to give
	        // the illusion of environment changes

	        
	        Tile edit = new Tile();
	        Layer layer = new Layer(BufferType.STATIC);
	        System.out.println(layer);
	        System.out.println(world.addLayer(layer));
	       
	        // The close event can take a bit to propagate
	        while (screen.isOpened()) {
	            screen.clear();   
	            
	            // Check for any debug actions (zoom, environment switches)
	            {    
	                if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
	                    world.camera.zoomIn();
	                } else if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
	                    world.camera.zoomOut();
	                } else if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
	        	        System.out.println(world.addLayer(layer));
	                    world.changeEnvironment(World.Environment.NIGHT, 5000);
	                } else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
	                    world.changeEnvironment(World.Environment.MORNING, 5000);
	                } else if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
	                    world.changeEnvironment(World.Environment.AFTERNOON, 5000);
	                } else if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
	                    world.changeEnvironment(World.Environment.DUSK, 5000);
	                } else if (Mouse.isButtonDown(0)) 
	                {
	            	    int x = Mouse.getX();
	            	    int y = Mouse.getY();
	             
	            	    System.out.println("MOUSE DOWN @ X: " + x + " Y: " + y);
	            	    layer.addTile(new Tile("res/tests/img/tree.png",x,y));
	                }
	            }
	            
	            
	            // Draw the world in its entirety
	            {
//	                float x = Mouse.getX();
//	                float y = Mouse.getY();
////
//////	                if (x < 50.0f && x > 0.0f) {
//////	                    x = -0.05f;
//////	                } else if (x > 590.0f && x < 640.0f) {
//////	                    x = 0.05f;
//////	                } else {
//////	                    x = 0;
//////	                }
//////
//////	                if (y < 50.0f && y > 0.0f) {
//////	                    y = -0.05f;
//////	                } else if (y > 430.0f && y < 480.0f) {
//////	                    y = 0.05f;
//////	                } else {
//////	                    y = 0;
//	                }
//	            	world.camera.move(x, y);
	            	
	            	
	                world.draw();
	            }
	            
	            // Active the GUI shader and draw the overlays
	            {
	                shader.use();   
	                //activeOverlay.bounds.x -= 0.5f;
	                //activeOverlay.bounds.y -= 0.75f;
//	                activeOverlay.bounds.x %= 4096;
//	                activeOverlay.bounds.y %= 4096;
//	                activeOverlay.createIndexVertexBuffer(10, 10);
	               // layer.draw();
	                font.print(world.getCurrentEnvironment().toString(), 2, 10, 10);    
	                shader.release();
	            }
	            
	            // Force the screen to update
	            {            
	                screen.update();
	            }
	        }
	        
	        // Do any sort of LWJGL cleanups
	        screen.cleanup();
	    }
	 
	    public static void main(String[] args) throws LWJGLException, IOException {
	        new WorldCreator();
	    }

}
