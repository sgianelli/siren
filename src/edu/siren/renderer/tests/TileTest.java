package edu.siren.renderer.tests;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;

import edu.siren.core.Layer;
import edu.siren.core.Tile;
import edu.siren.renderer.Screen;
import edu.siren.renderer.Shader;

public class TileTest {
    Screen screen;

    private void initializeScreen() throws LWJGLException {
        screen = new Screen("Texture Test", 640, 480);
    }

    private void setTileTest() throws LWJGLException, IOException {
        initializeScreen();

        Layer layer = new Layer();
        layer.addTile(new Tile(0.0f, 0.0f, 1.0f, 1.0f), // Center
                new Tile(-2.5f, -2.5f, 1.0f, 1.0f)); // Left
        Shader shader = new Shader("res/tests/glsl/basic.vert",
                "res/tests/glsl/basic.frag");

        while (screen.isOpened()) {
            screen.clear();
            shader.use();
            layer.draw();
            shader.release();
            screen.update();

            if (Mouse.isButtonDown(0)) {
                float x = Mouse.getX() / 640.0f;
                float y = Mouse.getY() / 480.0f;

                System.out.println("" + x + ", " + y);
                layer.addTile(new Tile(x, y, 1.0f, 1.0f));
            }
        }
        screen.cleanup();
    }

    public static void main(String[] args) throws LWJGLException, IOException {
        new TileTest().setTileTest();
    }
}