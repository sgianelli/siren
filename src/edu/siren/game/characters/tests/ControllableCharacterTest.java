package edu.siren.game.characters.tests;

import java.io.IOException;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import edu.siren.core.World;
import edu.siren.game.Player;
import edu.siren.renderer.Screen;

public class ControllableCharacterTest {
    Screen screen;

    ControllableCharacterTest() throws LWJGLException {
        screen = new Screen("Controllable Character Test", 512, 448);
        screen.sync = 60;

        World world = new World(1024, 1024);

        Player player = new Player("res/tests/scripts/entities/villager-justin.json");
        player.setPosition(0, 0);
        
        world.addEntity(player);

        Keyboard.create();
        while (screen.isOpened()) {
          
            world.shader.use();
            if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
                world.camera.zoomIn();
            } else if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
                world.camera.zoomOut();
            }
            
            screen.clear();
            world.draw();
            world.shader.release();
            screen.update();
        }
        screen.cleanup();
    }

    public static void main(String[] args) throws LWJGLException, IOException {
        new ControllableCharacterTest();
    }
}