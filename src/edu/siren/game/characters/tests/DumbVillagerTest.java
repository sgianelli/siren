package edu.siren.game.characters.tests;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import edu.siren.core.World;
import edu.siren.game.characters.Villager;
import edu.siren.renderer.Screen;

public class DumbVillagerTest {
    Screen screen;

    DumbVillagerTest() throws LWJGLException {
        screen = new Screen("Dumb Villager Test", 640, 480);
        screen.sync = 60;

        World world = new World(1024, 1024);
        world.addEntity(new Villager(
                "res/tests/scripts/entities/villager-justin.json"));

        Keyboard.create();
        while (screen.isOpened()) {
            float x = Mouse.getX();
            float y = Mouse.getY();

            if (x < 50.0f && x > 0.0f) {
                x = -15f;
            } else if (x > 590.0f && x < 640.0f) {
                x = 15f;
            } else {
                x = 0;
            }

            if (y < 50.0f && y > 0.0f) {
                y = -15f;
            } else if (y > 430.0f && y < 480.0f) {
                y = 15f;
            } else {
                y = 0;
            }

            world.shader.use();
            boolean forceUpdate = true;
            if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
                world.camera.zoomIn();
            } else if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
                world.camera.zoomOut();
            } else {
                forceUpdate = false;
            }
            screen.clear();
            world.camera.move(x, y);
            world.draw();
            world.shader.release();
            screen.update();
        }
        screen.cleanup();
    }

    public static void main(String[] args) throws LWJGLException, IOException {
        new DumbVillagerTest();
    }
}