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
import edu.siren.core.tile.World;
import edu.siren.game.characters.Villager;
import edu.siren.game.players.Link;
import edu.siren.game.worlds.BasicWorld;
import edu.siren.renderer.BufferType;
import edu.siren.renderer.Font;
import edu.siren.renderer.Perspective2D;
import edu.siren.renderer.Screen;
import edu.siren.renderer.Shader;

import org.lwjgl.input.Mouse;

public class WorldCreator {

     public static int WORLD_WIDTH = 5000;
     public static int WORLD_HEIGHT = 5000;
     public static int WINDOW_WIDTH = 512;
     public static int WINDOW_HEIGHT = 448;
     public static int GRID = 64;

	 Screen screen;
	 

	 WorldCreator() throws LWJGLException, IOException {
	        Random random = new Random();
		 
	        screen = new Screen("World Creator", WINDOW_WIDTH, WINDOW_HEIGHT);
	        
	        World world = new BasicWorld(WORLD_WIDTH, WORLD_HEIGHT);
	        
	        // Create a new layer
	        Layer layer = new Layer();
	        
	        


	        System.out.println(layer);
	        System.out.println(world.addLayer(layer));
	               
	        // Create a GUI using the Perspective2D camera
	        Perspective2D gui = new Perspective2D();
	        Shader shader = new Shader("res/tests/glsl/2d-perspective.vert", "res/tests/glsl/2d-perspective.frag");
	        gui.bindToShader(shader);
	        Font font = new Font("res/tests/fonts/nostalgia.png", 24);

	        // Create a new player and put them somewhere in the world
//	        Player player = new Link();
//	        player.setPosition(0, 0);
//	        world.addEntity(player);// Create a new player and put them somewhere in the world
//	        
	
	            for (int j = 0; j < 30; j++) {
	                Villager v = new Villager(
	                        "res/tests/scripts/entities/villager-justin.json");
	                v.setPosition(random.nextInt(WORLD_WIDTH), random.nextInt(WORLD_HEIGHT));
	                world.addEntity(v);
	        }

	        
	        // These are some overlays that we will draw over the Gui to give
	        // the illusion of environment changes

	        System.out.println(layer);
	        System.out.println(world.addLayer(layer));
	       
	        // The close event can take a bit to propagate
	        while (screen.isOpened()) {
	            screen.clear();   
	            
	            // Check for any debug actions (zoom, environment switches)
	            {    
	                if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
	                    world.zoomIn();
	                } if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
	                    world.zoomOut();
	                }  if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
	        	        System.out.println(world.addLayer(layer));
	                    world.changeEnvironment(World.Environment.NIGHT, 5000);
	                } if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
	                    world.changeEnvironment(World.Environment.MORNING, 5000);
	                } if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
	                    world.changeEnvironment(World.Environment.AFTERNOON, 5000);
	                } if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
	                    world.changeEnvironment(World.Environment.DUSK, 5000);
	                }if (Keyboard.isKeyDown(Keyboard.KEY_A)) {//Move the world
	                	world.move(-5, 0);
	                }if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
	                	world.move(5, 0);
	                } if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
	                	world.move(0, -5);
	                }if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
	                	world.move(0, 5);
	                }
	                if (Mouse.isButtonDown(0)) 
	                {
	                	System.out.println(Mouse.getX() + "/" + WINDOW_HEIGHT);
	                	
	                	
	                	float xPercent = ((float)Mouse.getX()) /((float)WINDOW_HEIGHT);
	                	
	                	System.out.println("camera" + -world.getCamera().getX());
	                	System.out.println(xPercent);
	                	
	                	
	                	float xStart = (float) (-world.getCamera().getX() + world.getCamera().getWidth()/2);
	                	
	                	float yPercent = Mouse.getY()/(world.getCamera().getZoomLevel() * world.getCamera().getHeight());
	                	int yStart = -world.getCamera().getY() + world.getCamera().getHeight()/2;
	                	
	               
	            	    int x = Mouse.getX();
	            	    int y = Mouse.getY();
	            	    System.out.println("x="+xPercent + " Start = " + xStart);
//	            	    
//	            	    System.out.println(world.getCamera().getX());
//	            	    System.out.println("MOUSE DOWN @ X: " + x + " Y: " + y);
	                    // Place the tree somewhere in the world
	            	    
	            	  //  for(Tile tile : (Tile) layer.tiles)//check for intercecting tiles
	            	    	
	    
	            	    	
	                    Tile tile = new Tile("res/tests/img/tree.png",
	                    		
	                    		(((int)( xStart - (1-xPercent)*world.getCamera().getWidth())) / GRID * GRID) - 36 ,  
	                    		(((int)( yStart - (1-yPercent)*world.getCamera().getHeight())) / GRID * GRID) - 36 );
//	                    
//	                    System.out.println("Mouse: " + x + ", Camera: " + world.getCamera().getX() + ", Zoom: " + world.getCamera().getZoomLevel());
	                    
	                    
	                    // Add the tile to the current layer
	                    layer.addTile(tile);
	                    
	                    
	                }
	                if (Mouse.isButtonDown(1)) 
	                {
	                	
	                	float xPercent = ((float)Mouse.getX()) /((float)WINDOW_HEIGHT);
	                	float xStart = (float) (-world.getCamera().getX() + world.getCamera().getWidth()/2);
	                	
	                	float yPercent = Mouse.getY()/(world.getCamera().getZoomLevel() * world.getCamera().getHeight());
	                	int yStart = -world.getCamera().getY() + world.getCamera().getHeight()/2;

	                	layer.remove(
	                    		((( xStart - (1-xPercent)*world.getCamera().getWidth()))) - 36 ,  
	                    		((( yStart - (1-yPercent)*world.getCamera().getHeight()))) - 36 );
	                    
	                    
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
