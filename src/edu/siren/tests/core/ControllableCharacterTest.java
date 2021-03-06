package edu.siren.tests.core;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import edu.siren.core.tile.Tile;
import edu.siren.core.tile.World;
import edu.siren.game.Player;
import edu.siren.game.players.Diglett;
import edu.siren.game.players.Link;
import edu.siren.game.players.Pikachu;
import edu.siren.game.worlds.TestBox;
import edu.siren.renderer.Font;
import edu.siren.renderer.Perspective2D;
import edu.siren.renderer.Screen;
import edu.siren.renderer.Shader;

public class ControllableCharacterTest {
    Screen screen;

    ControllableCharacterTest() throws LWJGLException, IOException {
        screen = new Screen("Controllable Character Test", 512, 448);
        World world = new TestBox(8096, 8096);
               
        // Create a GUI using the Perspective2D camera
        Perspective2D gui = new Perspective2D();
        Shader shader = new Shader("res/tests/glsl/2d-perspective.vert", "res/tests/glsl/2d-perspective.frag");
        gui.bindToShader(shader);
        Font font = new Font("res/tests/fonts/nostalgia.png", 24);

        // Create a new player and put them somewhere in the world
        Player player = new Pikachu();
        player.follow = true;
        
        Player player2 = new Link();
        Player player3 = new Diglett();
        player.setPosition(0, 0);
        player2.setPosition(0, 25);
        player3.setPosition(0, 50);
        world.addEntity(player);
        world.addEntity(player2);
        world.addEntity(player3);
        
        // These are some overlays that we will draw over the Gui to give
        // the illusion of environment changes
        Tile veryCloudy = new Tile("res/tests/img/very-cloudy.png", 0, 0, 8096, 8096, 1, 1);
        Tile slightlyCloudy = new Tile("res/tests/img/slightly-cloudy.png", 0, 0, 8096, 8096, 1, 1);
        Tile pixelCloudy = new Tile("res/tests/img/pixel-cloudy.png", 0, 0, 8096, 8096, 1, 1);
        Tile plasma = new Tile("res/tests/img/plasma.png", 0, 0, 8096, 8096, 1, 1);
        Tile activeOverlay = slightlyCloudy;
       
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
            }
            
            // Draw the world in its entirety
            world.draw();
            
            // Active the GUI shader and draw the overlays
            shader.use();   
            activeOverlay.bounds.x -= 0.5f;
            activeOverlay.bounds.y -= 0.75f;
            activeOverlay.bounds.x %= 4096;
            activeOverlay.bounds.y %= 4096;
            activeOverlay.createIndexVertexBuffer(10, 10);
            activeOverlay.draw();
            font.print(world.getCurrentEnvironment().toString(), 2, 10, 10);    
            shader.release();            
        }
        
        // Do any sort of LWJGL cleanups
        screen.cleanup();
    }

    public static void main(String[] args) throws LWJGLException, IOException {
        new ControllableCharacterTest();
    }
}