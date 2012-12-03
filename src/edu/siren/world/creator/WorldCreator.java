package edu.siren.world.creator;


import java.io.IOException;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JFrame;

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

public class WorldCreator extends JFrame {

     public static int WORLD_WIDTH = 5000;
     public static int WORLD_HEIGHT = 5000;
     public static int WINDOW_WIDTH = 512;
     public static int WINDOW_HEIGHT = 448;
     public static int GRID = 16; // default 32

	 Screen screen;
	 public String sprite = "tree.png";
	 
	 int index = 0;
	 String[] sprites = {
			 
			 "hill-top-left.png","hill-top-left-2.png","hill-top.png","hill-top-right-2.png","hill-top-right.png",
			 "hill-left.png","hill-right.png",
			 "hill-bottom-left.png","hill-bottom.png","hill-bottom-right.png","stone.png",
			 "grass.png","stairs.png",
			 
			 "water-top-left.png","water-top.png","water-top-right.png","water-left.png","water.png","water-right.png",
			 "water-bottom-left.png","water-bottom.png","water-bottom-right.png",
			 
	 
	 
	 
	 
	 
	 };
	 String dir = "res/game/sprites/tiles/";
	 

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
	                if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
	                	index -= 1;
	                	if (index == -1)
	                		index = sprites.length - 1;
	                	
	                    try {
							Thread.sleep(100);
							System.out.println("Boop");
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}  
	                }
	                if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
	                	index += 1;
	                	if (index == sprites.length)
	                		index = 0;
	                	
	                    try {
							Thread.sleep(100);
							System.out.println("Boop");
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}  
	                }
	                if (Mouse.isButtonDown(0)) 
	                {	            	    	
	            	    int x = (int)(-world.getCamera().getX() + world.getCamera().getZoomLevel() * (Mouse.getX() - world.getCamera().getWidth()/2));
	            	    int y = (int)(-world.getCamera().getY() + world.getCamera().getZoomLevel() * (Mouse.getY() - world.getCamera().getHeight()/2));

	                    
	            	    if(x >= 0)
		            	    x = x / GRID * (GRID );
	            	    else
		            	    x = x / GRID * (GRID) - GRID;
	            	    
	            	    
	            	    if(y >= 0)
		            	    y = y / GRID * (GRID);
	            	    else
		            	    y = (y / GRID * (GRID)) - GRID;
	            	    
	            	    


	                    Tile tile = new Tile(dir+sprites[index],x,y);
	                    
	                    // Add the tile to the current layer
	                    layer.addTile(tile);
	                    
	                    
	                }
	                if (Mouse.isButtonDown(1)) 
	                {

	                	layer.remove(
	                    		-world.getCamera().getX() + world.getCamera().getZoomLevel() * (Mouse.getX() - world.getCamera().getWidth()/2),
	                    		-world.getCamera().getY() + world.getCamera().getZoomLevel() * (Mouse.getY() - world.getCamera().getHeight()/2) );
	                    
	                    
	                }
	    
	            
	            }
	            
	            
	            // Draw the world in its entirety
	            {
	            	
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
	                font.print(sprites[index], 2, 10, 10); 
	                
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
		  	WorldCreator wc = new WorldCreator();
	    	
	    }

}
