package edu.siren.game.characters.tests;

import java.io.IOException;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import edu.siren.core.World;
import edu.siren.game.Player;
import edu.siren.renderer.Font;
import edu.siren.renderer.Gui;
import edu.siren.renderer.Screen;
import edu.siren.renderer.Shader;

public class ControllableCharacterTest {
    Screen screen;

    ControllableCharacterTest() throws LWJGLException, IOException {
        screen = new Screen("Controllable Character Test", 512, 448);

        World world = new World(1024, 1024);
        
        Gui gui = new Gui();
        Shader shader = new Shader("res/tests/glsl/gui.vert",
                "res/tests/glsl/gui.frag");
        gui.bindToShader(shader);

        Font font = new Font("res/tests/fonts/nostalgia.png", 24);
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
            } else if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
                world.changeEnvironment(World.Environment.NIGHT, 5000);
            } else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
                world.changeEnvironment(World.Environment.MORNING, 5000);
            } else if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
                world.changeEnvironment(World.Environment.AFTERNOON, 5000);
            } else if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
                world.changeEnvironment(World.Environment.DUSK, 5000);
            }
                        
            screen.clear();            
            world.draw();
            world.shader.release();
            
            shader.use();
            font.print(world.getCurrentEnvironment().toString(), 2, 10, 10);
            shader.release();
            
            screen.update();
        }
        screen.cleanup();
    }

    public static void main(String[] args) throws LWJGLException, IOException {
        new ControllableCharacterTest();
    }
}