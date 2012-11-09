package edu.siren.tests.core;

import java.io.IOException;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import edu.siren.core.tile.Tile;
import edu.siren.game.Player;
import edu.siren.game.World;
import edu.siren.renderer.Font;
import edu.siren.renderer.Gui;
import edu.siren.renderer.Screen;
import edu.siren.renderer.Shader;

public class ControllableCharacterTest {
    Screen screen;

    ControllableCharacterTest() throws LWJGLException, IOException {
        screen = new Screen("Controllable Character Test", 512, 448);

        World world = new World(8096, 8096);
        
        Gui gui = new Gui();
        Shader shader = new Shader("res/tests/glsl/gui.vert",
                "res/tests/glsl/gui.frag");
        gui.bindToShader(shader);

        Font font = new Font("res/tests/fonts/nostalgia.png", 24);
        Player player = new Player("res/tests/scripts/entities/villager-justin.json");
        player.setPosition(0, 0);
        
        Tile veryCloudy = new Tile("res/tests/img/very-cloudy.png", 0, 0, 8096, 8096, 1, 1);
        Tile slightlyCloudy = new Tile("res/tests/img/slightly-cloudy.png", 0, 0, 8096, 8096, 1, 1);
        Tile pixelCloudy = new Tile("res/tests/img/pixel-cloudy.png", 0, 0, 8096, 8096, 1, 1);
        Tile plasma = new Tile("res/tests/img/plasma.png", 0, 0, 8096, 8096, 1, 1);
        Tile activeOverlay = slightlyCloudy;
        
        world.addEntity(player);
        Keyboard.create();
        while (screen.isOpened()) {
            screen.clear();   
            {    
                if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
                    world.camera.zoomIn();
                } else if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
                    world.camera.zoomOut();
                } else if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
                    world.changeEnvironment(World.Environment.NIGHT, 5000);
                    activeOverlay = veryCloudy;
                } else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
                    world.changeEnvironment(World.Environment.MORNING, 5000);
                    activeOverlay = pixelCloudy;
                } else if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
                    world.changeEnvironment(World.Environment.AFTERNOON, 5000);
                    activeOverlay = slightlyCloudy;
                } else if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
                    world.changeEnvironment(World.Environment.DUSK, 5000);
                    activeOverlay = plasma;
                }
                                    
                world.draw();
            }
            {
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
            
            screen.update();
        }
        screen.cleanup();
    }

    public static void main(String[] args) throws LWJGLException, IOException {
        new ControllableCharacterTest();
    }
}